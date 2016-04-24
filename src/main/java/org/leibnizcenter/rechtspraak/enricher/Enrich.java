package org.leibnizcenter.rechtspraak.enricher;

import cc.mallet.fst.CRF;
import org.leibnizcenter.rechtspraak.cfg.CYK;
import org.leibnizcenter.rechtspraak.cfg.Grammar;
import org.leibnizcenter.rechtspraak.cfg.rule.RightHandSide;
import org.leibnizcenter.rechtspraak.cfg.rule.StandardRule;
import org.leibnizcenter.rechtspraak.cfg.rule.TypeContainer;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminalImpl;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.interfaces.NonTerminal;
import org.leibnizcenter.rechtspraak.crf.ApplyCrf;
import org.leibnizcenter.rechtspraak.leibnizannotations.DeterministicTagger;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags.LeibnizTags;
import org.leibnizcenter.util.Collections3;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helpers functions enriching Rechtspraak XML
 * <p>
 * Created by Maarten on 2016-04-01.
 */
public class Enrich {
    private final CRF crf;

    public Enrich() throws IOException, ClassNotFoundException, URISyntaxException {
        this(new File(Enrich.class.getClassLoader().getResource(Const.RECHTSPRAAK_MARKUP_TAGGER_CRF).toURI()));
    }

    public Enrich(File crfModelFile) throws IOException, ClassNotFoundException {
        if (!crfModelFile.exists())
            throw new InvalidParameterException("CRF model not found at " + crfModelFile.getAbsolutePath());
        this.crf = ApplyCrf.loadCrf(crfModelFile);
    }

    @SuppressWarnings("WeakerAccess")
    public void enrich(String ecli, Document doc) {
        // Tokenize 
        Element contentRoot = Xml.getContentRoot(doc);
        TokenList tokenList = TokenList.parse(ecli, doc, contentRoot);

        // Apply tagging
        List<Label> tags = DeterministicTagger.tag(tokenList);

        // Make sectioning tree if there is no section tag already
//        if (!Xml.containsTag(contentRoot, "section")) {
        Grammar dg = new DocumentGrammar();
        List<Terminal> words = new ArrayList<>(tags.size());
        for (int i = 0; i < tags.size(); i++) {
            words.add(new Terminal(tags.get(i), tokenList.get(i)));
        }
        CYK.ParseTreeContainer bestParseTree = CYK.getBestParseTree(words, dg);
        bestParseTree = flattenTree(bestParseTree);

        if (bestParseTree == null)
            throw new NullPointerException();
        setNewXmlStructure(bestParseTree, contentRoot);
//        }

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
                        element = Xml.setElementNameTo(element, null, "text");
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

    private static final NonTerminalImpl a = new NonTerminalImpl(" "); // placeholder
    private static final StandardRule PLACE_HOLDER_RULE = new StandardRule(a, new RightHandSide(a, a), 1.0);

    private static CYK.ParseTreeContainer flattenTree(CYK.ParseTreeContainer t) {
        return new CYK.ParseTreeContainer(
                PLACE_HOLDER_RULE,
                flattenTreeR(t).collect(Collectors.toList())
        );
    }

    private static Stream<TypeContainer> flattenTreeR(CYK.ParseTreeContainer t) {
        return t.getInputs().stream().flatMap(tt -> {
                    if (DocumentGrammar.SECTION_BLOB.equals(tt.getType()))
                        return flattenTreeR((CYK.ParseTreeContainer) tt);
                    if (DocumentGrammar.SINGLE_NUMBERING.equals(tt.getType()))
                        return flattenTreeR((CYK.ParseTreeContainer) tt);
                    if (DocumentGrammar.SECTION_TITLE_TEXT.equals(tt.getType()))
                        return flattenTreeR((CYK.ParseTreeContainer) tt);
                    if (DocumentGrammar.TEXT_BLOB.equals(tt.getType()))
                        return flattenTreeR((CYK.ParseTreeContainer) tt);
                    if (DocumentGrammar.SECTION_CONTENT.equals(tt.getType()))
                        return flattenTreeR((CYK.ParseTreeContainer) tt);

                    if (tt instanceof Terminal) return Stream.of(tt);

                    return Stream.of(new CYK.ParseTreeContainer(
                            new StandardRule((NonTerminal) tt.getType(), new RightHandSide(), 1.0),
                            flattenTreeR((CYK.ParseTreeContainer) tt).collect(Collectors.toList())
                    ));
                }
        );
    }

    /**
     * We assume the nodes in the tree are title for sections that span up to and including all children, up to the
     * next sibling node
     *
     * @param tree
     */
    private void setNewXmlStructure(CYK.ParseTreeContainer tree, Element contentRoot) {
//        for (Node n : Xml.getChildren(contentRoot))
//            contentRoot.removeChild(n);
        Element newContentRoot = contentRoot.getOwnerDocument().createElement(contentRoot.getTagName());
        Xml.copyAttributes(contentRoot, newContentRoot);
        recursiveCreateXml(tree, newContentRoot);
        contentRoot.getParentNode().replaceChild(newContentRoot, contentRoot);
    }

    private void recursiveCreateXml(TypeContainer root, Node addTo) {
        if (root instanceof CYK.ParseTreeContainer) {
            if (root.getType().equals(DocumentGrammar.SECTION)) {
                Element newElement = addTo.getOwnerDocument().createElement("section");
                addTo.appendChild(newElement);
                addTo = newElement;
            } else if (root.getType().equals(DocumentGrammar.SECTION_TITLE)) {
                Element newElement = addTo.getOwnerDocument().createElement("title");
                addTo.appendChild(newElement);
                addTo = newElement;
            }
            for (TypeContainer inp : ((CYK.ParseTreeContainer) root).getInputs()) {
                recursiveCreateXml(inp, addTo);
            }
        } else if (root instanceof Terminal) {
            TokenTreeLeaf data = (TokenTreeLeaf) ((Terminal) root).getData();
            if (data == null) throw new NullPointerException();
            addTo.appendChild(data.getNode());
        } else
            throw new InvalidParameterException();
    }

    /**
     * Append child and all preceding nodes (which are automatically removed from the original container)
     *
     * @param newElement element to add to
     * @param child      child to add
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
