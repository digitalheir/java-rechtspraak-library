package org.leibnizcenter.rechtspraak.enricher;

import cc.mallet.fst.CRF;
import cc.mallet.types.Instance;
import cc.mallet.types.Sequence;

import org.leibnizcenter.cfg.category.Category;
import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;
import org.leibnizcenter.cfg.earleyparser.ParseTree;
import org.leibnizcenter.cfg.earleyparser.ParseTreeWithScore;
import org.leibnizcenter.cfg.earleyparser.Parser;
import org.leibnizcenter.cfg.grammar.Grammar;
import org.leibnizcenter.cfg.token.Token;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tagging.crf.ApplyCrf;
import org.leibnizcenter.rechtspraak.tagging.crf.TrainCrf;
import org.leibnizcenter.rechtspraak.tokens.LabeledToken;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags.LeibnizTags;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Helpers functions enriching Rechtspraak XML
 * <p>
 * Created by Maarten on 2016-04-01.
 */
public class Enrich {
    private final CRF crf;

    public Enrich() throws IOException, ClassNotFoundException, URISyntaxException {
        this.crf = ApplyCrf.loadCrf(Enrich.class.getClassLoader()
                        .getResourceAsStream(Const.RECHTSPRAAK_MARKUP_TAGGER_CRF_TRAINED_ON_MANUALLY_ANNOTATED)
        );
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
        List<Instance> instances = Collections.singletonList(new Instance(TrainCrf.getTokenSequence(tokenList, true), null, null, null));
        Iterator<Instance> instanceIterator = crf.getInputPipe().newIteratorFrom(instances.iterator());
        while (instanceIterator.hasNext()) {
            Instance piped = instanceIterator.next();
            Sequence labels = crf.transduce((Sequence) piped.getData());
            //List<Label> tags = DeterministicTagger.tag(tokenList);
            List<Label> tags = new ArrayList<>(labels.size());
            for (int i = 0; i < labels.size(); i++) {//noinspection SuspiciousMethodCalls
                tags.add(Label.fromString.get(labels.get(i)));
                //System.out.println(labels.get(i) + ": " + tokenList.get(i));
            }
            parseTree(contentRoot, tokenList, tags);
        }
    }

    public static void parseTree(Element contentRoot, TokenList tokenList, List<Label> tags) {
        // Make sectioning tree if there is no section tag already
        //TODO
//        if (!Xml.containsTag(contentRoot, "section")) {
        Grammar dg = DocumentGrammar.grammar;

        List<Token<LabeledToken>> words = new ArrayList<>(tokenList.size());
//        long start = System.currentTimeMillis();
        for (int i = 0; i < tags.size(); i++)
            words.add(new Token<>(
                    new LabeledToken(tokenList.get(i), tags.get(i))
            ));
        int wordsNr = words.size();
//        words.stream().map(w->w.obj.getTag().toString()).forEach(System.out::println);
        ParseTreeWithScore viterbi = Parser.getViterbiParseWithScore(DocumentGrammar.DOCUMENT, dg, words);
        if (viterbi == null)
            throw new NullPointerException("Could not find parse for given document. Please update the document grammar.");

        setNewXmlStructure(viterbi, contentRoot);
//        long end = System.currentTimeMillis();
        //System.out.println(wordsNr + "," + (end - start));
//        // Set tag values on elements (we can do this another way)
//        Collections3.zip(tokenList.stream(), tags.stream()).forEach(pair -> {
//            Label label = pair.getValue();
//            TokenTreeLeaf token = pair.getKey();
//            if (token instanceof RechtspraakElement) {
//                Element element = ((RechtspraakElement) token).getElement();
//                switch (label) {
//                    case NEWLINE:
//                        break;
//                    case NR:
//                        element = Xml.setElementNameTo(element, null, "nr");
//                        break;
//                    case SECTION_TITLE:
//                        element = Xml.setElementNameTo(element, null, "text");
//                        break;
//                    case TEXT_BLOCK:
//                        switch (element.getTagName()) {
//                            case LeibnizTags.TAG_POTENTIAL_NR:
//                            case LeibnizTags.TAG_QUOTE:
//                            case LeibnizTags.TAG_TEXT:
//                                Xml.dissolveTag(element);
//                                element = null;
//                                break;
//                            default:
//                                break;
//                        }
//                        break;
//                    default:
//                        throw new IllegalStateException();
//                }
//            }
//        });
//
//        // Dissolve intermediate tags
//        cleanUp(tokenList.getSource().getDocumentElement());
    }

//    private static CYK.ParseTreeContainer flattenTree(CYK.ParseTreeContainer t) {
//        if(t==null)throw new NullPointerException();
//        List<TypeContainer> flattenedTree = flattenTreeR(t);
//
//        if (flattenedTree.size() == 1 && flattenedTree.get(0) instanceof Terminal) {
//            return new CYK.ParseTreeContainer(
//                    PLACE_HOLDER_RULE,
//                    (Terminal) flattenedTree.get(0)
//            );
//        } else {
//            CYK.ParseTreeContainer[] inputs = new CYK.ParseTreeContainer[flattenedTree.size()];
//            for (int i = 0; i < inputs.length; i++) inputs[i] = (CYK.ParseTreeContainer) flattenedTree.get(i);
//            return new CYK.ParseTreeContainer(
//                    PLACE_HOLDER_RULE,
//                    inputs
//            );
//        }
//    }
//
//    private static List<TypeContainer> flattenTreeR(CYK.ParseTreeContainer t) {
//        List<TypeContainer> streamz = new ArrayList<>(100);
//
//        for(TypeContainer tt :t.getInputs()){
//            if (DocumentGrammar.SECTION_BLOB.equals(tt.getType()))
//                return flattenTreeR((CYK.ParseTreeContainer) tt);
//            if (DocumentGrammar.SINGLE_NUMBERING.equals(tt.getType()))
//                return flattenTreeR((CYK.ParseTreeContainer) tt);
//            if (DocumentGrammar.SECTION_TITLE_TEXT.equals(tt.getType()))
//                return flattenTreeR((CYK.ParseTreeContainer) tt);
//            if (DocumentGrammar.TEXT_BLOB.equals(tt.getType()))
//                return flattenTreeR((CYK.ParseTreeContainer) tt);
//            if (DocumentGrammar.SECTION_CONTENT.equals(tt.getType()))
//                return flattenTreeR((CYK.ParseTreeContainer) tt);
//
//            if (tt instanceof Terminal) return Collections.singletonList(tt);
//
//
//            List<TypeContainer> inputs = flattenTreeR((CYK.ParseTreeContainer) tt);
//            if (inputs.size() == 1 && inputs.get(0) instanceof Terminal) {
//                streamz.add(new CYK.ParseTreeContainer(
//                        new StandardRule((NonTerminal) tt.getType(), new RightHandSide(), 1.0),
//                        (Terminal) inputs.get(0)
//                ));
//            } else {
//                CYK.ParseTreeContainer[] inputz = new CYK.ParseTreeContainer[inputs.size()];
//                for (int i = 0; i < inputz.length; i++) inputz[i] = (CYK.ParseTreeContainer) inputs.get(i);
//                streamz.add(new CYK.ParseTreeContainer(
//                        new StandardRule((NonTerminal) tt.getType(), new RightHandSide(), 1.0),
//                        inputz
//                )    );
//            }
//        }
//        return streamz;
//    }

    /**
     * <strike>We assume the nodes in the tree are title for sections that span up to and including all children, up to the
     * next sibling node</strike>
     *
     * @param tree
     */
    private static void setNewXmlStructure(ParseTreeWithScore tree, Element contentRoot) {
        for (Node n : Xml.getChildren(contentRoot))
            contentRoot.removeChild(n);

        // Make copy of <uitspraak>/<conclusie>
        Element newContentRoot = contentRoot.getOwnerDocument().createElement(contentRoot.getTagName());
        Xml.copyAttributes(contentRoot, newContentRoot);

        // Add XML nodes
        editXmlRecursive(tree.getParseTree(), newContentRoot);

        // Replace old <uitspraak>/<conclusie> with new one
        contentRoot.getParentNode().replaceChild(newContentRoot, contentRoot);
    }

    private static void editXmlRecursive(ParseTree root, Node addTo) {
        Category cat = root.getCategory();
        if (root instanceof ParseTree.Token) {
            LabeledToken data = (LabeledToken) ((ParseTree.Token) root).token.obj;
            if (data == null) throw new NullPointerException();
            Node node = data.getToken().getNode();
            if (node instanceof Element) {
                if (cat.equals(DocumentGrammar.SINGLE_NUMBERING)
                        || DocumentGrammar.TERMINAL_NUMBERING == cat/*todo create equals method*/) {
//                    System.out.println("setting nr");
                    node = Xml.setName((Element) node, "nr");
                }
            }
            addTo.appendChild(node);
        } else {
            if (!(root instanceof ParseTree.NonToken)) throw new Error();
            // Container
            Category type = cat;

            // See if this should be a new XML element
            Element newElement;
            if (type.equals(DocumentGrammar.SECTION))
                newElement = addTo.getOwnerDocument().createElement("section");
            else if (type.equals(DocumentGrammar.SECTION_TITLE))
                newElement = addTo.getOwnerDocument().createElement("title");
            else //addTo.getOwnerDocument().createElement("div");
                newElement = null;

            //
            if (newElement != null) {
                addTo.appendChild(newElement);
                addTo = newElement;
            }
            if (root.hasChildren()) for (ParseTree child : root.getChildren())
                editXmlRecursive(child, addTo);
        }
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
