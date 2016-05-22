package org.leibnizcenter.rechtspraak.enricher;

import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;
import org.w3c.dom.Document;
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

import static org.leibnizcenter.rechtspraak.tokens.TokenList.getDoc;
import static org.leibnizcenter.rechtspraak.tokens.TokenList.getEcliFromFileName;

/**
 * Created by Maarten on 2016-05-22.
 */
public class Experiment {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, URISyntaxException, ClassNotFoundException, IllegalAccessException, TransformerException, InstantiationException {
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

            Xml.writeToStream(doc, new FileWriter(new File(outFolder,file.getName())));
        }


    }
}
