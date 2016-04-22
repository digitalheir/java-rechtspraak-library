package org.leibnizcenter.rechtspraak.enricher;

import cc.mallet.fst.CRF;
import org.leibnizcenter.rechtspraak.cfg.CYK;
import org.leibnizcenter.rechtspraak.cfg.Grammar;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.crf.ApplyCrf;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.ExhaustiveMostLikelyTreeFromList;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.GreedyMostLikelyTreeFromList;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.PenaltyCalculatorImpl;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.abstracts.MostLikelyTreeFromList;
import org.leibnizcenter.rechtspraak.leibnizannotations.DeterministicTagger;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags.LeibnizTags;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.Const;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.LabeledTokenNode;
import org.leibnizcenter.rechtspraak.util.immutabletree.NamedImmutableTree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Helpers functions enriching Rechtspraak XML
 * <p>
 * Created by Maarten on 2016-04-01.
 */
public class Enrich {
    private final CRF crf;

    public Enrich() throws IOException, ClassNotFoundException {
        this(new File(Const.RECHTSPRAAK_MARKUP_TAGGER_CRF));
    }

    public Enrich(File crfModelFile) throws IOException, ClassNotFoundException {
        if (!crfModelFile.exists())
            throw new InvalidParameterException("CRF model not found at " + crfModelFile.getAbsolutePath());
        this.crf = ApplyCrf.loadCrf(crfModelFile);
    }

    public void enrich(String ecli, Document doc) {
        // Tokenize 
        Element contentRoot = Xml.getContentRoot(doc);
        TokenList tokenList = TokenList.parse(ecli, doc, contentRoot);

        // Apply tagging
        List<Label> tags = DeterministicTagger.tag(tokenList);

        // Make sectioning tree if there is no section tag already
        if (!Xml.containsTag(contentRoot, "section")) {
            Grammar dg=new DocumentGrammar();
            List<Terminal> words = new ArrayList<>(tags.size());
            for (int i = 0; i < tags.size(); i++) {
                words.add(new Terminal(tags.get(i), tokenList.get(i)));
            }
            CYK.ParseTreeContainer bestParseTree = CYK.getBestParseTree(words, dg);
            setNewXmlStructure(bestParseTree, contentRoot);
        }

        // Set tag values on elements (we can do this another way)
        Collections3.zip(tokenList.stream(), tags.stream()).forEach(pair -> {
            Label label = pair.getValue();
            TokenTreeLeaf token = pair.getKey();
            if (token instanceof RechtspraakElement) {
                Element element = ((RechtspraakElement) token).getElement();
                switch (label) {
                    case NEWLINE:
                        break;
                    case NR:
                        element = Xml.setElementNameTo(element, null, "nr");
                        break;
                    case SECTION_TITLE:
                        element = Xml.setElementNameTo(element, null, "title");
                        break;
                    case TEXT_BLOCK:
                        switch (element.getTagName()) {
                            case LeibnizTags.TAG_POTENTIAL_NR:
                            case LeibnizTags.TAG_QUOTE:
                            case LeibnizTags.TAG_TEXT:
                                Xml.dissolveTag(element);
                                element = null;
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }
        });

        // Dissolve intermediate tags
        cleanUp(doc.getDocumentElement());
    }

    /**
     * We assume the nodes in the tree are title for sections that span up to and including all children, up to the
     * next sibling node
     *
     * @param tree
     */
    private void setNewXmlStructure(CYK.ParseTreeContainer tree, Element contentRoot) {
        // TODO less destructive XML change
//        for (Node n : Xml.getChildren(contentRoot)) contentRoot.removeChild(n);
//
//        for (int i = 0; i < tree.getChildren().size(); i++) {
//            contentRoot.appendChild(recursiveCreateXml(tree.getChildren().get(i), contentRoot.getOwnerDocument()));
//        }
    }

    private Node recursiveCreateXml(ImmutableTree root, Document ownerDocument) {
        if (root instanceof NamedImmutableTree) {
            Element newElement = ownerDocument.createElement(((NamedImmutableTree) root).getName());
            if (!Collections3.isNullOrEmpty(root.getChildren()))
                for (int i = 0; i < root.getChildren().size(); i++) {
                    ImmutableTree child = root.getChildren().get(i);
                    if (child instanceof LabeledTokenNode)
                        appendPrevSiblings(newElement, ((LabeledTokenNode) child).token.getToken().getNode());
                    newElement.appendChild(recursiveCreateXml(child, ownerDocument));
                }
            return newElement;
        } else if (root instanceof LabeledTokenNode) {
            return ((LabeledTokenNode) root).token.getToken().getNode();
        } else throw new InvalidParameterException();
    }

    /**
     * Append child and all preceding nodes (which are automatically removed from the original container)
     *
     * @param newElement
     * @param child
     */
    private void appendPrevSiblings(Element newElement, Node child) {
        Deque<Node> previousSiblings = new ArrayDeque<>(child.getParentNode().getChildNodes().getLength());
        while (child.getPreviousSibling() != null) {
            child = child.getPreviousSibling();
            previousSiblings.push(child);
        }

        while (previousSiblings.size() > 0) newElement.appendChild(previousSiblings.pop());
    }

    public static void cleanUp(Node n) {
        Node[] children = Xml.getChildren(n);

        for (Node c : children) cleanUp(c);

        if (n instanceof Element) {
            Element element = ((Element) n);
            // If this element has a leibniz namespace, either dissolve the tag or make it an 'official' tag
            if (LeibnizTags.hasLeibnizNameSpace(element)) {
                if (!element.hasAttributeNS(LeibnizTags.LEIBNIZ_NAMESPACE, LeibnizTags.Attr.manualAnnotation))
                    throw new InvalidParameterException("No attr found: " + LeibnizTags.Attr.manualAnnotation);
            }

            element.removeAttributeNS(LeibnizTags.LEIBNIZ_NAMESPACE, LeibnizTags.Attr.manualAnnotation);
        }
    }
}
