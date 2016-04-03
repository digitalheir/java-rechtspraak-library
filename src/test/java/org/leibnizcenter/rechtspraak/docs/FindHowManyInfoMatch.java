//package org.leibnizcenter.rechtspraak.docs;
//
//import com.google.common.base.Charsets;
//import com.google.common.collect.HashMultiset;
//import com.google.common.collect.Multiset;
//import com.google.common.collect.Multisets;
//import com.google.common.collect.UnmodifiableIterator;
//import com.google.common.io.Files;
//import org.leibnizcenter.rechtspraak.TrainWithMallet;
//import org.leibnizcenter.rechtspraak.features.info.InfoPatterns;
//import org.leibnizcenter.rechtspraak.markup.docs.Label;
//import org.leibnizcenter.rechtspraak.markup.docs.LabeledToken;
//import org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList;
//import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakCorpus;
//import org.leibnizcenter.rechtspraak.markup.docs.features.Patterns;
//
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
///**
// * Creates a F1 performance summary for our info regex patterns
// * Created by maarten on 17-3-16.
// */
//public class FindHowManyInfoMatch {
//    public static void main(String[] a) throws IOException, ParserConfigurationException {
//        Multiset<String> falseNegatives = HashMultiset.create();
//        Multiset<String> falsePositives = HashMultiset.create();
//        int truePositives = 0, falsePositive = 0, totalPositives = 0, totalTokens = 0;
//        List<File> xmlFiles = RechtspraakCorpus.listXmlFiles(TrainWithMallet.xmlFiles, -1, false);
//        for (LabeledTokenList doc : new LabeledTokenList.FileIterable(new LabeledTokenList.FileIteratorFromXmlStructure(xmlFiles))) {
//            for (LabeledToken token : doc) {
//                String text = token.getToken().normalizedText;
//                boolean matches = false;
//                // Check if this title find any of our patterns
//                // TODO other ones
//                for (Patterns.NormalizedTextMatches pattern : InfoPatterns.InfoPatternsNormalizedMatches.set) {
//                    if (Patterns.matches(pattern, token.getToken())) {
//                        matches = true;
//                        break;
//                    }
//                }
//                //todo
////                if (token.getTag() == Label.INFO) {
////                    if (matches) {
////                        truePositives++;
////                    } else {
////                        falseNegatives.add(text);
////                    }
////                    totalPositives++;
////                    if (totalPositives % 5000 == 0) System.out.println(totalPositives);
////                } else {
////                    if (matches) {
////                        falsePositive++;
////                        falsePositives.add(text);
////                    }
////                }
//                totalTokens++;
//            }
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("Info count: ").append(totalTokens).append("\n");
//        sb.append("Info count: ").append(totalPositives).append("\n");
//        sb.append("True  positive: ").append(truePositives).append("\n");
//        sb.append("False positive: ").append(falsePositive).append("\n");
//        sb.append("___true positive___% ").append((((double) truePositives) * 10.0) / ((double) totalPositives) * 10.0).append("\n");
//        sb.append("___true negative___% ").append(((double) ((totalTokens - totalPositives) - falsePositive) * 10.0) /
//                ((double) (totalTokens - totalPositives)) * 10.0).append("\n");
//        sb.append("________________________________________________________").append("\n");
//        sb.append("Top 1000 false negatives: ").append("\n");
//        // Should be sorted?
//        UnmodifiableIterator<Multiset.Entry<String>> it = Multisets.copyHighestCountFirst(falseNegatives).entrySet().iterator();
//        for (int i = 0; i < 1000; i++) {
//            if (it.hasNext()) {
//                Multiset.Entry<String> entry = it.next();
//                sb.append(entry.getElement()).append(" (").append(entry.getCount()).append(")").append("\n");
//            } else {
//                break;
//            }
//        }
//        sb.append("________________________________________________________").append("\n");
//        sb.append("Top 1000 false positives: ").append("\n");
//        it = Multisets.copyHighestCountFirst(falsePositives).entrySet().iterator();
//        for (int i = 0; i < 1000; i++) {
//            if (it.hasNext()) {
//                Multiset.Entry<String> entry = it.next();
//                sb.append(entry.getElement()).append(" (").append(entry.getCount()).append(")").append("\n");
//            } else {
//                break;
//            }
//        }
//
//        /////////////
//
//        File file = new File("info_patterns_performance.txt");
//        Files.write(sb.toString(), file, Charsets.UTF_8);
//    }
//}
