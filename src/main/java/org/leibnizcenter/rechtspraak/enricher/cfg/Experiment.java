package org.leibnizcenter.rechtspraak.enricher.cfg;

import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.enricher.Enrich;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags.LeibnizTags;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.leibnizcenter.rechtspraak.tokens.TokenList.getDoc;
import static org.leibnizcenter.rechtspraak.tokens.TokenList.getEcliFromFileName;

/**
 * Created by Maarten on 2016-05-22.
 */
public class Experiment {
    /**
     * Precision: 0.8714285714285714
     Recall: 0.8714285714285714
     F1: 0.8714285714285714
     Precision: 0.9361702127659575
     Recall: 0.9166666666666666
     F1: 0.9263157894736843
     Precision: 0.9365079365079365
     Recall: 0.9365079365079365
     F1: 0.9365079365079365
     Precision: 1.0
     Recall: 1.0
     F1: 1.0
     Precision: 0.9629629629629629
     Recall: 0.9629629629629629
     F1: 0.9629629629629629
     Precision: 0.8888888888888888
     Recall: 0.8888888888888888
     F1: 0.8888888888888888
     Precision: 0.8445945945945946
     Recall: 0.8278145695364238
     F1: 0.8361204013377926
     Precision: 0.9459459459459459
     Recall: 0.9459459459459459
     F1: 0.9459459459459459
     Precision: 0.9298245614035088
     Recall: 0.8833333333333333
     F1: 0.905982905982906
     Precision: 0.9512195121951219
     Recall: 0.9512195121951219
     F1: 0.9512195121951219
     ---
     Precision: 0.9267543186693488
     Recall: 0.9184768387465849
     F1: 0.922537291472381
     */
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();


        File automaticallyParsed = new File("C:\\Users\\Maarten\\Downloads\\rechtspraak-rich-docs-20160221-auto-parsed-doc");
        File manuallyParsed = new File("C:\\Users\\Maarten\\Downloads\\rechtspraak-rich-docs-20160221-auto-parsed-doc\\checked");

        double precTotal=0;
        double recTotal=0;
        double f1Total=0;
        int i=0;
        for (File f : manuallyParsed.listFiles()) {
            File auto = new File(automaticallyParsed, f.getName());

            Set<ConstituentTree> goldParse = getConstituentTrees(new ConstituentTree(Xml.getContentRoot(getDoc(builder, f)))).collect(Collectors.toSet());
            Set<ConstituentTree> candidateParse = getConstituentTrees(new ConstituentTree(Xml.getContentRoot(getDoc(builder, auto)))).collect(Collectors.toSet());

            //totalConstituentNumber =
            Sets.SetView<ConstituentTree> correctConsituents = Sets.intersection(candidateParse, goldParse);
            //Sets.SetView<ConstituentTree> allInAutoButNotInManual = Sets.difference(candidateParse, goldParse);
            //Sets.SetView<ConstituentTree> allInManualButNotInAuto = Sets.difference(goldParse,candidateParse);


            double precision = ((double) correctConsituents.size()) / (double) candidateParse.size();
            System.out.println("Precision: " + precision);
            double recall = ((double) correctConsituents.size()) / (double) goldParse.size();
            System.out.println("Recall: " + recall);
            double f1 = 2 * ((precision * recall) / (precision + recall));
            System.out.println("F1: " + f1);

            precTotal += precision;
            recTotal += recall;
            f1Total += f1;
            i++;
        }
        System.out.println("---");
            System.out.println("Precision: " + (precTotal/i));
            System.out.println("Recall: " + (recTotal/i));
            System.out.println("F1: " + (f1Total/i));

    }

    private static Stream<TerminalConstituent> streamTerminals(Constituent source) {
        if (source instanceof ConstituentTree)
            return ((ConstituentTree) source).children.stream().flatMap(Experiment::streamTerminals);
        else if (source instanceof TerminalConstituent) return Stream.of((TerminalConstituent) source);
        else throw new Error();
    }

    private static Stream<ConstituentTree> getConstituentTrees(ConstituentTree source) {
        return source.children.stream()
                .filter(s -> s instanceof ConstituentTree)
                .flatMap(constTree -> {
                    Stream<ConstituentTree> stream1 = Stream.of((ConstituentTree) constTree);
                    return Stream.concat(stream1, getConstituentTrees((ConstituentTree) constTree));
                });
    }

    private interface Constituent {
        static Constituent from(Element e) {
            boolean isTerminalNode = e.hasAttribute(LeibnizTags.Attr.manualAnnotation);
            if (!isTerminalNode) {
                return new ConstituentTree(e);
            } else {
                Label label = Label.fromString.get(e.getAttribute(LeibnizTags.Attr.manualAnnotation));
                //System.out.println();
                switch (label) {
                    case NEWLINE:
                        return new TerminalConstituent(label, "\n");
                    case NR:
                        return new TerminalConstituent(label, e.getTextContent().trim());
                    case SECTION_TITLE:
                        return new TerminalConstituent(label, e.getTextContent().trim());
                    case TEXT_BLOCK:
                        return new TerminalConstituent(label, e.getTextContent().trim());
                    default:
                        throw new Error();
                }
            }
        }
    }

    private static class TerminalConstituent implements Constituent {

        private final String s;
        private final Label l;

        @Override
        public String toString() {
            return l + "{" + s + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TerminalConstituent that = (TerminalConstituent) o;

            if (!s.equals(that.s)) return false;
            return l == that.l;

        }

        @Override
        public int hashCode() {
            int result = s.hashCode();
            result = 31 * result + l.hashCode();
            return result;
        }

        public TerminalConstituent(Label l, String s) {
            this.s = s;
            this.l = l;
        }
    }

    private static class ConstituentTree implements Constituent {
        private final List<Constituent> children;
        private final String name;

        @Override
        public String toString() {
            return name + " {" +
                    children +
                    '}';
        }

        public ConstituentTree(Element root) {
            name = root.getTagName();
            children = Xml.getElementChildren(root).stream().map(Constituent::from).collect(Collectors.toList());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ConstituentTree that = (ConstituentTree) o;

            if (!name.equals(that.name)) return false;
            return this.yield().equals(that.yield());

        }

        private List<TerminalConstituent> yield() {
            return streamTerminals(this).collect(Collectors.toList());
        }

        @Override
        public int hashCode() {
            int result = children.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }

    public static void writeTestFiles() throws ParserConfigurationException, IOException, SAXException, URISyntaxException, ClassNotFoundException, IllegalAccessException, TransformerException, InstantiationException {
        File outFolder = new File(Const.IN_FOLDER_TESTING);
        if (!outFolder.exists()) //noinspection ResultOfMethodCallIgnored
            outFolder.mkdir();

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();


        List<File> xmlFiles = Xml.listXmlFiles(new File(Const.IN_FOLDER_TESTING));
        File[] fileIterator = xmlFiles.toArray(new File[xmlFiles.size()]);
        for (File file : fileIterator) {
            String ecli = getEcliFromFileName(file);
            Document doc = getDoc(builder, file);
            TokenList tokens = TokenList.parse(ecli, doc, Xml.getContentRoot(doc));
            List<Label> actualLabels = TokenTree.labelFromAnnotation(tokens);

            Enrich.parseTree(Xml.getContentRoot(doc), tokens, actualLabels);

            Xml.writeToStream(doc, new FileWriter(new File(outFolder, file.getName())));
        }


    }
}
