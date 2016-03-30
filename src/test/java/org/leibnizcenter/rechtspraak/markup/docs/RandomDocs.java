package org.leibnizcenter.rechtspraak.markup.docs;

import org.leibnizcenter.rechtspraak.TrainWithMallet;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.List;

/**
 * Created by maarten on 22-3-16.
 */
public class RandomDocs {
    private static final int INT = 20;

    public static void main(String[] args) throws ParserConfigurationException {
        List<File> xmlFiles = RechtspraakCorpus.listXmlFiles(TrainWithMallet.xmlFiles, 500, true);
        for (File f : xmlFiles) {
            System.out.println(
                    "file://" +
                            f.getAbsolutePath());
        }
        for (LabeledTokenList doc : new LabeledTokenList.FileIterable(new LabeledTokenList.FileIteratorFromXmlStructure(xmlFiles))) {
            for (LabeledToken token : doc) {
                String text = token.getToken().normalizedText;
            }
        }
    }
}
