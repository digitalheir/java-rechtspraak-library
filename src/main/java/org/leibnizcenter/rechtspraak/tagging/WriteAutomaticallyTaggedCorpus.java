package org.leibnizcenter.rechtspraak.tagging;

import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags.LeibnizTags;
import org.leibnizcenter.util.Xml;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.leibnizcenter.rechtspraak.tokens.TokenList.getEcliFromFileName;

/**
 * Created by maarten on 1-5-16.
 */
public class WriteAutomaticallyTaggedCorpus {
    private static File xmlFolder = new File(Xml.IN_FOLDER_AUTOMATIC_TAGGING);
    private static File out = new File(Xml.OUT_FOLDER_AUTOMATIC_TAGGING);

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        List<File> xmlFiles = Xml.listXmlFiles(xmlFolder, -1, true);
        for (File inFile : xmlFiles) {
            File outFile = new File(out, inFile.getName());
            if (!outFile.exists()) {
                System.out.println(inFile.getName());
                String ecli = getEcliFromFileName(inFile);
                Document doc = TokenList.getDoc(builder, inFile);

                TokenList currentDoc = TokenList.parse(ecli, doc, Xml.getContentRoot(doc));
                List<Label> labels = DeterministicTagger.tag(currentDoc);
                setTags(currentDoc, labels);

                Annotator.printToFile(doc, outFile);
//            } else {
//                System.err.println(inFile.getName() + " already exists");
            }
        }
    }

    private static void setTags(TokenList currentDoc, List<Label> labels) {
        for (int i = 0; i < currentDoc.size(); i++) {
            Label label = labels.get(i);
            TokenTreeLeaf leaf = currentDoc.get(i);
            if (leaf instanceof RechtspraakElement) {
                ((RechtspraakElement) leaf).setAttribute(LeibnizTags.Attr.manualAnnotation, label.name());
            } else if (leaf instanceof Newline) {
                // It should be CLEAR that this is a newline
            } else {
                throw new IllegalStateException();
            }
        }
}
}
