package org.leibnizcenter.rechtspraak.markup.docs;

import org.leibnizcenter.rechtspraak.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.markup.docs.features.IsPartOfList;
import org.leibnizcenter.rechtspraak.util.TextBlockInfo;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.leibnizcenter.rechtspraak.util.numbering.NumberingNumber;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by maarten on 28-2-16.
 */
public class LabeledTokenList extends ArrayList<LabeledToken> {
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
//    private static final String POSITION = "POSITION";
    private static final String TAG_SECTION = "section";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TEXT = "text";
    //    private static final String TAG_NR = "nr";
    private final String ecli;
    private final Document doc;

    public LabeledTokenList(String ecli, int initialCapacity, Document document) {
        super(initialCapacity);
        this.ecli = ecli;
        this.doc = document;
    }

    public LabeledTokenList(String ecli, Document document) {
        this.ecli = ecli;
        this.doc = document;
    }

    public LabeledTokenList(String ecli, Document document, Collection<? extends LabeledToken> c) {
        super(c);
        this.ecli = ecli;
        this.doc = document;
    }

    public static LabeledTokenList from(String ecli, Document document, Node root) {
        LabeledTokenList tokenList = new LabeledTokenList(ecli, document, textInPreorder(root, null, null, false));


        // Decide whether elements are likley list items (as opposed to section titles)
        for(int indexInSequence=0;indexInSequence<tokenList.size();indexInSequence++) {
            tokenList.get(indexInSequence).getToken().likelyPartOfList =
                    IsPartOfList.isPartOfList(tokenList, indexInSequence);
        }

        // find if there are numbered high likelihood-titles
        Optional<NumberingNumber> numb = highConfidenceNumberedTitlesFound(tokenList);
        if (numb.isPresent()) {
            NumberingNumber n = numb.get();
            tokenList.forEach((t) -> {
                RechtspraakElement token = t.getToken();
                if (token.numbering != null
                        // Check if they're both the same class (ie both arabic; both roman)
                        && token.numbering.getClass().equals(n.getClass())
                        // Check if they're both the same class (ie both arabic; both roman)
                        && !token.likelyPartOfList
                        ) {
                    token.setHighConfidenceNumberedTitleFoundAndIsNumbered(true);
                }
            });
        }


        return tokenList;
    }

    public static LabeledTokenList from(DocumentBuilder builder, File xmlFile, String ecli) throws SAXException, IOException {
        FileInputStream is = new FileInputStream(xmlFile);
        Document doc = builder.parse(new InputSource(new InputStreamReader(is)));
//                assert ecli != null;
//                assert doc != null;
        //System.out.println("Parsed " + ecli);
        return from(ecli, doc, Xml.getContentRoot(doc));
    }

    private static Optional<NumberingNumber> highConfidenceNumberedTitlesFound(LabeledTokenList tokenList) {
        for (LabeledToken t : tokenList) {
            RechtspraakElement token = t.getToken();
            if (token.numbering != null && TitlePatterns.TitlesNormalizedMatchesHighConf.matchesAny(token)) {
                return Optional.of(token.numbering);
            }
        }
        return Optional.empty();
    }

    public static List<LabeledToken> textInPreorder(Node root, Node parent, Label rootDescendentOf, boolean includeWhiteSpace) {
        NodeList children = root.getChildNodes();
        List<LabeledToken> texts = new ArrayList<>(children.getLength());

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
                childDescendentOf = Label.OUT;//SECTION_PARA;
            } else if (Label.OUT.equals(rootDescendentOf)
                    && parentName.equals(TAG_TITLE)) {
                childDescendentOf = Label.SECTION_TITLE;
            } else if (parentName.endsWith(TAG_SECTION)) {
                childDescendentOf = Label.OUT;//SECTION_PARA;
            }

            if (child.getNodeType() == Node.TEXT_NODE
                    ||
                    (child.getNodeType() == Node.ELEMENT_NODE && ((Element) child).getTagName().matches("nr|para|title"))
                    ) {
                if (includeWhiteSpace || !TextBlockInfo.Regex.ALL_WHITESPACE.matcher(child.getTextContent()).matches()) {
                    Element childAsElement = getElement(child); // if this is a text node, wrap in element
                    LabeledToken textBlockWithLabel = new LabeledToken(childAsElement, childDescendentOf);
                    //child = textBlockWithLabel.getToken(); // XML tree may have changed
                    //System.out.println(texts.size());
                    texts.add(textBlockWithLabel);
                }
            } else {
                List<LabeledToken> childrenTexts = textInPreorder(child, root, childDescendentOf, includeWhiteSpace);
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

    public String getEcli() {
        return ecli;
    }

    public void setFeatures(String ecli, Node doc) {
//        /**
//         * Get document node nodes as a linear list
//         */
//        List<LabeledToken> textsWithLabels = Xml.textInPreorder(Xml.getContentRoot(doc), null, null, false);
//
//        //        System.out.println(textsWithLabels.size() + " tokens; adding features");
//        List<NumberingNumber> numbers = new ArrayList<>();
//        int size = textsWithLabels.size();
//        for (int i = 0; i < size; i++) {
//            LabeledToken entry = textsWithLabels.get(i);
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
//            //TO DO   away with enum iterators / lambdas
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

    public Document getSource() {
        if (doc == null) throw new NullPointerException();
        return doc;
    }

    public static class FileIterator implements java.util.Iterator<LabeledTokenList> {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        private File[] files;
        private int index = 0;
        private DocumentBuilder builder = factory.newDocumentBuilder();

        public FileIterator(File... files) throws ParserConfigurationException {
            this.files = files;
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = index < files.length;
            if (!hasNext) files = null; // Allow to be garbage collected
            return hasNext;
        }

        @Override
        public LabeledTokenList next() {
            try {
                File file = files[index];
                files[index] = null; // Allow to be garbage collected
                index++;
                if (index % 1000 == 0) System.out.println("Cycled through " + index);
                return from(builder, file, RechtspraakCorpus.getEcliFromFileName(file));
            } catch (SAXException | IOException e) {
                throw new Error();
            }
        }

        @Override
        public void remove() {
            throw new Error("not implemented");
        }

        @Override
        public void forEachRemaining(Consumer<? super LabeledTokenList> action) {
            Objects.requireNonNull(action);
            while (hasNext()) action.accept(next());
        }
    }

    public static class FileIterable implements Iterable<LabeledTokenList> {
        private File[] files;

        public FileIterable(List<File> xmlFiles) {
            this.files = xmlFiles.toArray(new File[xmlFiles.size()]);
        }

        public FileIterable(File... files) {
            this.files = files;
        }


        @Override
        public void forEach(Consumer<? super LabeledTokenList> action) {
            Objects.requireNonNull(action);
            for (LabeledTokenList rechtspraakToken : this) action.accept(rechtspraakToken);
        }

        @Override
        public Spliterator<LabeledTokenList> spliterator() {
            throw new Error("Not implemented");
        }


        @Override
        public FileIterator iterator() {
            try {
                return new FileIterator(files);
            } catch (ParserConfigurationException e) {
                throw new Error(e);
            }
        }
    }
}
