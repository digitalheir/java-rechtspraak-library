package org.leibnizcenter.rechtspraak.tokens;

import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
 * A list of tokens plus some metadata like source doc and ECLI
 *
 * Created by maarten on 28-2-16.
 */
public class TokenList extends ArrayList<TokenTreeLeaf> {
    public static final String SOURCE_NODE = "SOURCE_NODE";
    public static final String INFO_SUFFIX = ".info";

    ////////////////////////////////////////////////////////

    private final String ecli;
    private final Document doc;

    public TokenList(int initialCapacity, String ecli, Document document) {
        super(initialCapacity);
        this.ecli = ecli;
        this.doc = document;
    }

    public TokenList(String ecli, Document document) {
        this(10, ecli, document);
    }

    public TokenList(String ecli, Document document, Collection<? extends TokenTreeLeaf> c) {
        super(c);
        this.ecli = ecli;
        this.doc = document;
    }

    public static TokenList parse(String ecli, Document document, Element root) {
        try {
            return new TokenList(
                    ecli,
                    document,
                    new TokenTree(root).leafsInPreOrder()
            );
        } catch (NullPointerException e) {
            throw new NullPointerException(ecli + ": " + e.getMessage());
        }
    }

    public static Document getDoc(DocumentBuilder builder, File xmlFile) throws SAXException, IOException {
        FileInputStream is = new FileInputStream(xmlFile);
        return builder.parse(new InputSource(new InputStreamReader(is)));
//                assert ecli != null;
//                assert doc != null;
        //System.out.println("Parsed " + ecli);
    }

//    private static Optional<NumberingNumber> highConfidenceNumberedTitlesFound(LabeledTokenList tokenList) {
//        for (LabeledToken t : tokenList) {
//            RechtspraakElement token = t.getToken();
//            if (token.numbering != null && TitlePatterns.TitlesNormalizedMatchesHighConf.matchesAny(token)) {
//                return Optional.of(token.numbering);
//            }
//        }
//        return Optional.empty();
//    }


    public String getEcli() {
        return ecli;
    }

    public Document getSource() {
        if (doc == null) throw new NullPointerException();
        return doc;
    }

    public static class FileIterator implements Iterator<TokenList> {
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
        public TokenList next() {
            try {
                File file = files[index];
                files[index] = null; // Allow to be garbage collected
                index++;
                if (index % 1000 == 0) System.out.println("Cycled through " + index);
                String ecli = getEcliFromFileName(file);
                Document doc = getDoc(builder, file);
                return parse(ecli, doc, Xml.getContentRoot(doc));
            } catch (SAXException | IOException e) {
                throw new Error();
            }
        }

        @Override
        public void remove() {
            throw new Error("not implemented");
        }

        @Override
        public void forEachRemaining(Consumer<? super TokenList> action) {
            Objects.requireNonNull(action);
            while (hasNext()) action.accept(next());
        }
    }

    public static class FileIterable implements Iterable<TokenList> {
        private final FileIterator iterator;

        public FileIterable(FileIterator iterator) {
            this.iterator = iterator;
        }

        @Override
        public void forEach(Consumer<? super TokenList> action) {
            Objects.requireNonNull(action);
            for (TokenList rechtspraakToken : this) action.accept(rechtspraakToken);
        }

        @Override
        public Spliterator<TokenList> spliterator() {
            throw new Error("Not implemented");
        }


        @Override
        public FileIterator iterator() {
            return iterator;
        }

    }

    public static String getEcliFromFileName(File xmlFile) {
        String name = xmlFile.getName();
        return getEcliFromFileName(name);
    }

    private static String getEcliFromFileName(String name) {
        return name.replaceAll("\\.xml$", "").replaceAll("\\.", ":");
    }
}
