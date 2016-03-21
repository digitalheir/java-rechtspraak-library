package org.leibnizcenter.rechtspraak.manualannotation;

import cc.mallet.types.LabelAlphabet;
import org.leibnizcenter.rechtspraak.markup.docs.Const;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static org.leibnizcenter.rechtspraak.markup.docs.RechtspraakCorpus.listXmlFiles;


/**
 */
public class ManuallyAnnotateRichDocs {
    private static final String IN_FOLDER = "/media/maarten/Media/rechtspraak-rich-docs-20160221/";
    private static final String OUT_FOLDER = "/media/maarten/Media/rechtspraak-rich-docs-20160221-annotated/";
    private static final String _SKIP = "#_SKIP";
    private static final String _BACK = "#_BACK";
    private static final int MAX_DOCS = 100;
    private static final File xmlFolder = new File(Const.PATH_TRAIN_TEST_XML_FILES_LINUX);

    public static void main(String[] args) throws IOException, TransformerException {
        LabelAlphabet outputAlphabet = new LabelAlphabet();

        List<File> xmlFiles = listXmlFiles(xmlFolder, MAX_DOCS, true);

        File out = new File(OUT_FOLDER);
        assert out.exists();
        assert out.isDirectory();
        for (LabeledTokenList doc : new LabeledTokenList.FileIterable(xmlFiles)) {
            String ecli = doc.getEcli();
            String fileName = ecli.replace(':', '.') + ".xml";
            File outFile = new File(out, fileName);
            boolean skipped = false;
            if (!outFile.exists()) {
                System.out.println(doc.getEcli());
                for (int i = 0; i < doc.size(); i++) {
                    RechtspraakElement token = doc.get(i).getToken();
                    Label tag = doc.get(i).getTag();
                    System.out.println();
                    System.out.println(tag + "\t\t" + token.getTextContent());

                    String answer;
                    System.out.println(
                            "a(fdeling), " +
                                    "d(atum), " +
                                    "f(unction), " +
                                    "g(ebied), " +
                                    "i(nfo), " +
                                    "k(ind), " +
                                    "n(umber), " +
                                    "p(erson name), " +
                                    "r(echtbank), " +
                                    "s(ection), " +
                                    "t(itle), " +
                                    "v(oorgaande zaak), " +
                                    "w(oonplaats), " +
                                    "z(ittingsplaats)"
//                                    +"\n -OR- \n"+""
//                                     +"default (ENTER)?"
                    );

                    int read = System.in.read();
                    answer = getAnswer(read, tag.label);
                    //noinspection ResultOfMethodCallIgnored
                    System.in.skip(System.in.available());

                    if (null == (answer)) {
                        System.err.println(read);
                    } else {
                        switch (answer) {
                            case _BACK:
                                i--;
                                break;
                            case _SKIP:
                                skipped = true;
                                break;
                        }
                    }

                    System.out.println("Answer: " + answer);

                    if (skipped) break;
                    token.setAttribute("manualAnnotation", answer);
//                System.out.println(printToString(srcNode.node));
                }

                if (!skipped) {
                    printToFile(doc.getSource(), outFile);
                    System.out.println("Written " + fileName + "\n\n");
                }
            } else {
                System.out.println(fileName + " already existed");
            }
        }
    }

    private static String getAnswer(int read, String defaultVal) {
        switch (read) {
            case 10:
                return defaultVal;
            case 60:
                return _BACK;
            case 96:
                return _SKIP;
            case 97: // a(fdeling)
                return "afdeling";
            case 100: // d(atum)
                return "date";
            case 102: // f(unction)
                return "function";
            case 103: // (juridisch) g(ebied)
                return "legal-field";
            case 105: // i(nfo)
                return Label.INFO.name();
            case 107: // k(ind)
                return "kind";
            case 108: // l(awyer)
                return "lawyer";
            case 110: // n(umber)
                return "number";
            case 112: // p(erson name)
                return "person";
            case 114: // r(echtbank)
                return "court";
            case 115: // s(ection)
                return Label.OUT.name();
            case 116: // (section) t(itle)
                return Label.SECTION_TITLE.name();
            case 118: // v(oorgaande zaak)
                return "about";
            case 119: // w(oonplaats)
                return "person-location";
            case 122: // z(ittingsplaats)
                return "zittingsplaats";
            default:
                return null;
        }
    }


    private static String printToString(Node root) {
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            tf.transform(new DOMSource(root), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException e) {
            throw new Error(e);
        }
    }

    private static void printToFile(Node root, File file) {
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            FileOutputStream fos = new FileOutputStream(file);
            tf.transform(new DOMSource(root), new StreamResult(fos));
            fos.close();
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
            file.delete();
        }
    }


}
