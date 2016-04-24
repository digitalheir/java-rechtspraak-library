//package org.leibnizcenter.rechtspraak.docs;
//
//import org.leibnizcenter.rechtspraak.TrainWithMallet;
//import org.leibnizcenter.rechtspraak.markup.docs.*;
//
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.File;
//import java.util.List;
//
///**
// * Created by maarten on 22-3-16.
// */
//public class PrintTitleFalsePositives {
//
//    public static void main(String[] args) throws ParserConfigurationException {
//        List<File> xmlFiles = RechtspraakCorpus.listXmlFiles(TrainWithMallet.xmlFiles, 500, true);
//        for (LabeledTokenList doc : new LabeledTokenList.FileIterable(new LabeledTokenList.FileIteratorFromXmlStructure(xmlFiles))) {
//            for (LabeledToken t : doc) {
//                RechtspraakElement token = t.getToken();
//                if (token.highConfidenceNumberedTitleFoundAndIsNumbered() && !t.getTag().equals(Label.SECTION_TITLE)) {
//                    System.out.println(t.getToken());
//                }
//            }
//        }
//    }
//}
