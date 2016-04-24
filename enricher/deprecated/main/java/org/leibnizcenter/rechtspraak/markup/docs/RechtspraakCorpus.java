package org.leibnizcenter.rechtspraak.markup.docs;

import deprecated.org.crf.utilities.TaggedToken;

import java.io.File;
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

    public RechtspraakCorpus(Collection<List<LabeledToken>> c) {
        super(c);
    }

//    public RechtspraakCorpus(List<File> xmlFiles) {
//        super(xmlFiles.size());
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            for (File xmlFile : xmlFiles) {
//                String ecli = getEcliFromFileName(xmlFile);
//                try {
//                    Document doc = LabeledTokenList.getDoc(builder, xmlFile);
//                    LabeledTokenList instance = LabeledTokenList.fromAnnotations()
////                System.out.println("Instance made for " + ecli + "("
////                        + ((TokenSequence) instance.getData()).size() + ")");
////                System.out.println("Added " + ecli + " through pipe");
//                    add(instance);
//                } catch (IOException e) {
//                    throw new Error(e);
//                } catch (SAXException e) {
//                    System.err.println("Could not parse " + ecli + ", deleting it");
//                    //noinspection ResultOfMethodCallIgnored
//                    xmlFile.delete();
//                    throw new Error(e);
//                }
//            }
//        } catch (ParserConfigurationException e) {
//            throw new Error(e);
//        }
//    }

    public static String getEcliFromFileName(File xmlFile) {
        String name = xmlFile.getName();
        return name.replaceAll("\\.xml$", "").replaceAll("\\.", ":");
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
                addToListIfXmlFile(xmls, file);
            }
            Collections.shuffle(xmls);
        } else {
            for (File file : files) {
                addToListIfXmlFile(xmls, file);
                if (max > 0 && xmls.size() >= max) break;
            }
        }
        if (max > 0 && max < xmls.size()) {
            return xmls.subList(0, max);
        }
        for (File f : xmls) if (f == null || f.getName() == null) throw new Error();
        return xmls;
    }

    private static void addToListIfXmlFile(List<File> xmls, File file) {
        if (file.getAbsolutePath().endsWith(".xml")) {
            xmls.add(file);
        }
    }

    public static List<File> listXmlFiles() {
        return listXmlFiles(new File(Const.PATH_TRAIN_TEST_XML_FILES_LINUX), -1, false);
    }
}
