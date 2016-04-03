//package org.leibnizcenter.rechtspraak.docs;
//
//import org.leibnizcenter.rechtspraak.TrainWithMallet;
//import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
//import org.leibnizcenter.rechtspraak.tokens.TokenList;
//
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.File;
//import java.util.List;
//
///**
// * Created by maarten on 22-3-16.
// */
//public class RandomDocs {
//    private static final int INT = 20;
//
//    public static void main(String[] args) throws ParserConfigurationException {
//        List<File> xmlFiles = RechtspraakCorpus.listXmlFiles(TrainWithMallet.xmlFiles, 500, true);
//        for (File f : xmlFiles) {
//            System.out.println(
//                    "file://" +
//                            f.getAbsolutePath());
//        }
//        for (TokenList doc : new TokenList.FileIterable(new LabeledTokenList.FileIteratorFromXmlStructure(xmlFiles))) {
//            for (TokenTreeLeaf token : doc) {
//                String text = token.getToken().normalizedText;
//            }
//        }
//    }
//}
