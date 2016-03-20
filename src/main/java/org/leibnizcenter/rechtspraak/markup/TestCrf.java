package org.leibnizcenter.rechtspraak.markup;

import com.google.common.collect.Lists;
import org.crf.crf.CrfModel;
import org.crf.crf.run.CrfInferencePerformer;
import org.crf.crf.run.ExampleMain;
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
import java.util.List;

import static org.leibnizcenter.rechtspraak.markup.RechtspraakCorpus.listXmlFiles;


/**
 * :p
 * Created by maarten on 28-2-16.
 */
public final class TestCrf implements Runnable {
    private final static File xmlFiles = new File(Const.PATH_TRAIN_TEST_XML_FILES_LINUX);


    public static void main(String[] a) {
        TestCrf app = new TestCrf();
        app.run();
    }


    @Override
    public void run() {
        // Save the model into the disk.
        File file = new File("example.ser");

        ////////

        // Later... Load the model from the disk
        // noinspection unchecked
        CrfModel<RechtspraakElement, Label> crf = (CrfModel<RechtspraakElement, Label>) ExampleMain.load(file);

        // Create a CrfInferencePerformer, to find tags for test data
        CrfInferencePerformer<RechtspraakElement, Label> inferencePerformer = new CrfInferencePerformer<>(crf);

        // Test:
        List<File> testFiles = listXmlFiles(xmlFiles, 1);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            for (File xmlFile : testFiles) {
                String ecli = xmlFile.getName().replaceAll("\\.xml$", "").replaceAll("\\.", ":");
                FileInputStream is = new FileInputStream(xmlFile);
                Document doc = builder.parse(new InputSource(new InputStreamReader(is)));

                List<RechtspraakElement> instance =
                        Lists.transform(RechtspraakTokenList.from(ecli, doc, Xml.getContentRoot(doc)), TaggedToken::getToken);
                List<TaggedToken<RechtspraakElement, Label>> result = inferencePerformer.tagSequence(instance);

                // Print the result:
                result.stream()
                        //.filter(taggedToken -> taggedToken.getTag() != Label.INFO)
                        .forEach(taggedToken -> System.out.println(
                                "[" + taggedToken.getTag() + "] " + taggedToken.getToken().getTextContent().trim()
                        ));
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new Error(e);
        }
    }
}
