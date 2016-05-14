package org.leibnizcenter.rechtspraak.markup.docs;

import org.leibnizcenter.rechtspraak.tagging.crf.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.markup.docs.features.HasCloseAdjacentNumbering;
import org.leibnizcenter.rechtspraak.markup.docs.tokentree.TokenTree;
import org.leibnizcenter.util.Xml;
import org.leibnizcenter.util.numbering.Numbering;
import org.leibnizcenter.util.numbering.NumberingNumber;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
import java.util.stream.Collectors;

/**
 * Created by maarten on 28-2-16.
 */
public class LabeledTokenList extends ArrayList<LabeledToken> {
    // Labels
//    public static final String INFO = "*.info";
//    public static final String SECTION_PARA = "section";
//    public static final String SECTION_TITLE = "section-title";
//    public static final String TEXT_BLOCK = "out";
//    public static final String LABEL_NR = "nr";

    public static final String SOURCE_NODE = "SOURCE_NODE";
    public static final String INFO_SUFFIX = ".info";

    ////////////////////////////////////////////////////////

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

    public static LabeledTokenList fromAnnotations(String ecli, Document document, Element root) {
        try {
            LabeledTokenList tokenList = new LabeledTokenList(
                    ecli,
                    document,
                    TokenTree.labelFromAnnotation(new TokenTree(root, false).terminalsInPreOrder())
            );
            setGlobalFeatures(tokenList);
            return tokenList;
        } catch (NullPointerException e) {
            throw new NullPointerException(ecli + ": " + e.getMessage());
        }
    }

    public static LabeledTokenList fromOriginalTags(String ecli, Document document, Element root) {
        LabeledTokenList tokenList = new LabeledTokenList(
                ecli,
                document,
                TokenTree.labelFromXmlTags(new TokenTree(root, false).terminalsInPreOrder())
        );
        setGlobalFeatures(tokenList);
        return tokenList;
    }

    private static void setGlobalFeatures(LabeledTokenList tokenList) {
        List<RechtspraakElement> unlabeled = tokenList.stream().map(LabeledToken::getToken).collect(Collectors.toList());


        // Decide whether elements are close to another numbered item that is in sequence
        for (int indexInSequence = 0; indexInSequence < tokenList.size(); indexInSequence++) {
            tokenList.get(indexInSequence).getToken().closeToAdjacentNumbering =
                    HasCloseAdjacentNumbering.value(tokenList, indexInSequence);
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
                        && !token.closeToAdjacentNumbering
                        ) {
                    token.setHighConfidenceNumberedTitleFoundAndIsNumbered(true);
                }
            });
        }

        // Decide whether this is a plausible numbering
        for (int ix = 0; ix < tokenList.size(); ix++) {
            unlabeled.get(ix).isPlausibleNumbering = unlabeled.get(ix).numbering != null &&
                    !Numbering.looksLikeNumberingButProbablyIsnt(unlabeled, ix);
        }
    }

    public static Document getDoc(DocumentBuilder builder, File xmlFile) throws SAXException, IOException {
        FileInputStream is = new FileInputStream(xmlFile);
        return builder.parse(new InputSource(new InputStreamReader(is)));
//                assert ecli != null;
//                assert doc != null;
        //System.out.println("Parsed " + ecli);
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


    public String getEcli() {
        return ecli;
    }

    public void setFeatures(String ecli, Node doc) {
//        /**
//         * Get document node nodes as a linear list
//         */
//        List<LabeledToken> textsWithLabels = Xml.textInPreorder(Xml.getContentRoot(doc), null, null, false);
//
//        //        System.out.println(textsWithLabels.size() + " tokens; adding mostlikelytreefromlist");
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

    public abstract static class FileIterator implements java.util.Iterator<LabeledTokenList> {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        private DocumentBuilder builder = factory.newDocumentBuilder();
        private File[] files;
        private int index = 0;

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
                String ecli = RechtspraakCorpus.getEcliFromFileName(file);
                Document doc = getDoc(builder, file);
                return getLabeledDoc(ecli, doc, Xml.getContentRoot(doc));
            } catch (SAXException | IOException e) {
                throw new Error();
            }
        }

        protected abstract LabeledTokenList getLabeledDoc(String ecli, Document doc, Element contentRoot);

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
        private final FileIterator iterator;

        public FileIterable(FileIterator iterator) {
            this.iterator = iterator;
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
            return iterator;
        }

    }

    public static class FileIteratorFromAnnotations extends FileIterator {
        public FileIteratorFromAnnotations(File... files) throws ParserConfigurationException {
            super(files);
        }

        public FileIteratorFromAnnotations(List<File> xmlFiles) throws ParserConfigurationException {
            super(xmlFiles.toArray(new File[xmlFiles.size()]));
        }

        @Override
        protected LabeledTokenList getLabeledDoc(String ecli, Document doc, Element contentRoot) {
            return fromAnnotations(ecli, doc, Xml.getContentRoot(doc));
        }
    }

    public static class FileIteratorFromXmlStructure extends FileIterator {
        public FileIteratorFromXmlStructure(File... files) throws ParserConfigurationException {
            super(files);
        }

        public FileIteratorFromXmlStructure(List<File> files) throws ParserConfigurationException {
            super(files.toArray(new File[files.size()]));
        }

        @Override
        protected LabeledTokenList getLabeledDoc(String ecli, Document doc, Element contentRoot) {
            return fromOriginalTags(ecli, doc, Xml.getContentRoot(doc));
        }
    }
}
