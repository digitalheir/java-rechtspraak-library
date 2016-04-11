package org.leibnizcenter.rechtspraak.enricher;

import cc.mallet.fst.CRF;
import org.leibnizcenter.rechtspraak.crf.ApplyCrf;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.MostLikelyTreeFromListImpl;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.PenaltyCalculatorImpl;
import org.leibnizcenter.rechtspraak.leibnizannotations.DeterministicTagger;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags.LeibnizTags;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.Const;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
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
            ImmutableTree mostLikelySectionTree = MostLikelyTreeFromListImpl.getMostLikelyTree(
                    tokenList,
                    tags,
                    new PenaltyCalculatorImpl()
            );
            setSectionTags(mostLikelySectionTree, tokenList);
        }

        // Set tag values on elements (we can do this another way)
        Collections3.zip(tokenList.stream(), tags.stream()).forEach(paire -> {
            Label label = paire.getValue();
            TokenTreeLeaf token = paire.getKey();
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
     * @param tokenList
     */
    private void setSectionTags(ImmutableTree tree, TokenList tokenList) {
//        for (int i = 0; i < tree.children.size(); i++) {

            Xml.wrapSubTreeInElement(firstElementInSection, lastElementInSection, TokenTree.TAG_SECTION);
        }
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
