package org.leibnizcenter.rechtspraak.markup;

import org.crf.utilities.TaggedToken;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by maarten on 28-2-16.
 */
public class RechtspraakCorpus extends ArrayList<List<? extends TaggedToken<RechtspraakElement, Label>>> {

    public RechtspraakCorpus(int initialCapacity) {
        super(initialCapacity);
    }

    public RechtspraakCorpus() {
    }

    public RechtspraakCorpus(Collection<List<RechtspraakToken>> c) {
        super(c);
    }

    public RechtspraakCorpus(List<File> xmlFiles) {
        super(xmlFiles.size());
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            for (File xmlFile : xmlFiles) {
                String ecli = xmlFile.getName().replaceAll("\\.xml$", "").replaceAll("\\.", ":");
                try {
                    FileInputStream is = new FileInputStream(xmlFile);
                    Document doc = builder.parse(new InputSource(new InputStreamReader(is)));
//                assert ecli != null;
//                assert doc != null;
                    System.out.println("Parsed " + ecli);
                    RechtspraakTokenList instance = RechtspraakTokenList.from(ecli, Xml.getContentRoot(doc));
//                System.out.println("Instance made for " + ecli + "("
//                        + ((TokenSequence) instance.getData()).size() + ")");
//                System.out.println("Added " + ecli + " through pipe");
                    add(instance);
                } catch (IOException e) {
                    throw new Error(e);
                } catch (SAXException e) {
                    System.err.println("Could not parse " + ecli + ", deleting it");
                    //noinspection ResultOfMethodCallIgnored
                    xmlFile.delete();
                    throw new Error(e);
                }
            }
        } catch (ParserConfigurationException e) {
            throw new Error(e);
        }
    }

    public static List<File> listXmlFiles(File folder) {
        return listXmlFiles(folder, -1);
    }

    public static List<File> listXmlFiles(File folder, int max) {
        return listXmlFiles(folder, max, true);
    }

    public static List<File> listXmlFiles(File folder, int max, boolean shuffle) {
        File[] files = folder.listFiles();
        if (files == null) throw new NullPointerException();

        List<File> xmls = new ArrayList<>(files.length);
        if (shuffle) {
            for (File file : files) {
                addXmlToList(xmls, file);
            }
            Collections.shuffle(xmls);
        } else {
            for (File file : files) {
                addXmlToList(xmls, file);
                if (max > 0 && xmls.size() >= max) break;
            }
        }
        if (max > 0 && max < xmls.size()) {
            return xmls.subList(0, max);
        }
        return xmls;
    }

    private static void addXmlToList(List<File> xmls, File file) {
        //            if (file.isDirectory()) {
//                xmls.addAll(listXmlFiles(file));
//            } else
        if (file.getAbsolutePath().endsWith(".xml")) {
//                System.out.println(file.getAbsolutePath());
            xmls.add(file);
        }
    }
}
