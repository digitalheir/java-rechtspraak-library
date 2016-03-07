package org.leibnizcenter.rechtspraak.markup;

import org.leibnizcenter.rechtspraak.util.TextBlockInfo;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by maarten on 28-2-16.
 */
public class RechtspraakTokenList extends ArrayList<RechtspraakToken> {
    // Labels
//    public static final String INFO = "*.info";
//    public static final String SECTION_PARA = "section";
//    public static final String SECTION_TITLE = "section-title";
//    public static final String OUT = "out";
//    public static final String LABEL_NR = "nr";

    public static final String SOURCE_NODE = "SOURCE_NODE";
    public static final String INFO_SUFFIX = ".info";

    ////////////////////////////////////////////////////////
    // Features
    private static final String POSITION = "POSITION";
    private static final String TAG_SECTION = "section";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TEXT = "text";
    private static final String TAG_NR = "nr";
    private final String ecli;

    public RechtspraakTokenList(String ecli, int initialCapacity) {
        super(initialCapacity);
        this.ecli = ecli;
    }

    public RechtspraakTokenList(String ecli) {
        this.ecli = ecli;
    }

    public RechtspraakTokenList(String ecli, Collection<? extends RechtspraakToken> c) {
        super(c);
        this.ecli = ecli;
    }

    public static RechtspraakTokenList from(Document doc) {
        return from(null, doc);
    }

    public static RechtspraakTokenList from(String ecli, Node doc) {
        return new RechtspraakTokenList(ecli, textInPreorder(doc, null, null, false));
    }

    public static List<RechtspraakToken> textInPreorder(Node root, Node parent, Label rootDescendentOf, boolean includeWhiteSpace) {
        NodeList children = root.getChildNodes();
        List<RechtspraakToken> texts = new ArrayList<>(children.getLength());

        String parentName = root.getNodeName();

        // Make sure we have a reference to all children as they are now, as the XML tree might change.
        Node[] childz = new Node[children.getLength()];
        for (int i = 0; i < children.getLength(); i++) {
            childz[i] = children.item(i);
        }

        for (Node child : childz) {
            Label childDescendentOf = rootDescendentOf == null ? Label.OUT : rootDescendentOf; // Default: out
            if (parentName.endsWith(INFO_SUFFIX)) {
                childDescendentOf = Label.INFO;
//            } else if (
//                    isElement(child)
//                            && TAG_NR.equals(child.getNodeName())) {
//                // i.e. <section> <title ><nr/> </section>
//                childDescendentOf = LABEL_NR;
            } else if (
                    Xml.isElement(child) && TAG_SECTION.equals(parentName)
                            //&& TAG_TEXT.equals(parentName)
                            && TAG_TITLE.equals(child.getNodeName())) {
                // i.e. <section> <title /> </section>
                childDescendentOf = Label.SECTION_TITLE;
            } else if (parentName.equals(TAG_SECTION)) {
                childDescendentOf = Label.SECTION_PARA;
            } else if (Label.SECTION_PARA.equals(rootDescendentOf)
                    && parentName.equals(TAG_TITLE)) {
                childDescendentOf = Label.SECTION_TITLE;
            } else if (parentName.endsWith(TAG_SECTION)) {
                childDescendentOf = Label.SECTION_PARA;
            }

            if (child.getNodeType() == Node.TEXT_NODE
                    ||
                    (child.getNodeType() == Node.ELEMENT_NODE && ((Element) child).getTagName().matches("nr|para|title"))
                    ) {
                if (includeWhiteSpace || !TextBlockInfo.Regex.ALL_WHITESPACE.matcher(child.getTextContent()).matches()) {
                    Element childAsElement = getElement(child); // if this is a text node, wrap in element
                    RechtspraakToken textBlockWithLabel = new RechtspraakToken(childAsElement, childDescendentOf);
                    //child = textBlockWithLabel.getToken(); // XML tree may have changed
                    //System.out.println(texts.size());
                    texts.add(textBlockWithLabel);
                }
            } else {
                List<RechtspraakToken> childrenTexts = textInPreorder(child, root, childDescendentOf, includeWhiteSpace);
                texts.addAll(childrenTexts);
            }
        }
        return texts;
    }

    private static Element getElement(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
//                System.out.println("Wrapped text block in element:\t" + "(" + label + ")\t" + node.getTextContent());
            node = Xml.wrapSubstringInElement((Text) node, 0,
                    node.getTextContent().length(), TAG_TEXT);
        }
        return (Element) node;
    }

    public void setFeatures(String ecli, Node doc) {
//        /**
//         * Get document node nodes as a linear list
//         */
//        List<RechtspraakToken> textsWithLabels = Xml.textInPreorder(Xml.getContentRoot(doc), null, null, false);
//
//        //        System.out.println(textsWithLabels.size() + " tokens; adding features");
//        List<NumberingNumber> numbers = new ArrayList<>();
//        int size = textsWithLabels.size();
//        for (int i = 0; i < size; i++) {
//            RechtspraakToken entry = textsWithLabels.get(i);
//            String textContent = entry.node.getTextContent().trim();
//
//            // Add observation
//            token.setFeatureValue("IS_FOOTNOTE", dbl("footnote".equals(entry.node.getNodeName())));
//
//            // Token position
//            token.setFeatureValue("FIRST_QUARTILE", dbl(i / size <= 0.25));
//            token.setFeatureValue("LAST_QUARTILE", dbl(i / size > 0.75));
//
//            token.setFeatureValue("BEFORE_4_TOKENS", dbl(i < 4));
//            token.setFeatureValue("BEFORE_8_TOKENS", dbl(i < 8));
//            token.setFeatureValue("BEFORE_12_TOKENS", dbl(i < 12));
//
//
//            if ((i < 4) && !entry.label.equals(INFO)) {
//                System.out.println("https://rechtspraak.cloudant.com/docs/" + ecli + "/data.xml");
//            }
//
//            //TODO   away with enum iterators / lambdas
//            TextBlockInfo.setFeaturesForTrimmedTextContent(textContent, token, ecli);
//            TextBlockInfo.setFeaturesForFullTextContent(textContent, token, ecli);
//
//            // Number
//            TextBlockInfo.setNumberFeature(numbers, textContent, token, (i > 0 ? observations.get(i - 1) : null), ecli);
//
//            if (preserveInformation) token.setProperty(SOURCE_NODE, entry);
//
//            observations.add(token);
//
////            token.setFeatureValue(entry.label, 1.0);
//
//            // Add label
//            labels.add(entry.label);
//        }
    }
}
