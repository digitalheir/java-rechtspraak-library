package org.leibnizcenter.rechtspraak.tagging.experiments;

import org.leibnizcenter.rechtspraak.tagging.crf.TrainCrf;
import org.leibnizcenter.util.Xml;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by maarten on 13-5-16.
 */
public class Train {
    public static void main(String[] args) throws IOException, ClassNotFoundException, ParserConfigurationException {
        Arrays.stream(ExperimentConditions.values())
                .parallel()
                .forEach((condition) -> {
                            // Load a corpus
                            try {
                                TrainCrf.train(Xml.listXmlFiles(condition.xmlFolder,-1,true), condition.writeCrfToFile, condition.useNewlines);
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            }
                        }
                );
    }
}
