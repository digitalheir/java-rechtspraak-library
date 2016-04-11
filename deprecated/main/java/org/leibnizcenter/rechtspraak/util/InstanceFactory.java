//package org.leibnizcenter.rechtspraak.util;
//
//import cc.mallet.types.*;
//import org.leibnizcenter.rechtspraak.predict_markup.util.numbering.NumberingNumber;
//import org.w3c.dom.Document;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.leibnizcenter.rechtspraak.predict_markup.util.TextBlockInfo.dbl;
//
///**
// * Created by maarten on 22-12-15.
// */
//public class InstanceFactory {

//
//    //    private final Tokenizer utils;
//    private final boolean preserveInformation;
//    private final LabelAlphabet outputLabels;
//
//    public InstanceFactory(
//            boolean preserveInformation, LabelAlphabet outputAlphabet
//    ) {
////        this.utils = new Tokenizer(Tokenizer.Language.nl);
//        this.preserveInformation = preserveInformation;
//        outputLabels = outputAlphabet;
////        observationAlphabet = new Alphabet();
//    }
//
//    @Deprecated
//    public Instance makeInstanceTokenized(String ecli, Document doc) {
//        XmlToLinearTokenSequence linearChain = new XmlToLinearTokenSequence(
//                preserveInformation ? ecli : null,
//                doc,
//                preserveInformation,
//                null,
//                outputLabels
//        );
//
//
//        return new Instance(
//                linearChain.getObservations(),
//                linearChain.getLabels(),
//                preserveInformation ? ecli : null,
//                preserveInformation ? linearChain.getObservations() : null
//        );
//    }
//
//
//    public Instance makeInstanceForTextBlocks(String ecli, Document doc) {
//        /**
//         * Get document node nodes as a linear list
//         */
//        Xml.TextBlockSequence textsWithLabels = Xml.textInPreorder(Xml.getContentRoot(doc), null, null, false);
//
//        /**
//         * Text nodes as tokens with mostlikelytreefromlist set
//         */
//        TokenSequence observations = new TokenSequence(textsWithLabels.size());
//        /**
//         * Text node labels
//         */
//        LabelSequence labels = new LabelSequence(outputLabels, textsWithLabels.size());
//
////        System.out.println(textsWithLabels.size() + " tokens; adding mostlikelytreefromlist");
//        List<NumberingNumber> numbers = new ArrayList<>();
//        int size = textsWithLabels.size();
//        for (int i = 0; i < size; i++) {
//            Xml.TextBlockWithLabel entry = textsWithLabels.get(i);
//            String textContent = entry.node.getTextContent().trim();
//
//            // Add observation
//            Token token = new Token(preserveInformation ? textContent : null);
//
//            token.setFeatureValue("IS_FOOTNOTE", dbl("footnote".equals(entry.node.getNodeName())));
//            // Token position
//            token.setFeatureValue("FIRST_QUARTILE", dbl(i / size <= 0.25));
//            token.setFeatureValue("LAST_QUARTILE", dbl(i / size > 0.75));
//
//            token.setFeatureValue("BEFORE_4_TOKENS", dbl(i < 4));
//            token.setFeatureValue("BEFORE_8_TOKENS", dbl(i < 8));
//            token.setFeatureValue("BEFORE_12_TOKENS", dbl(i < 12));
//
//
//            if ((i < 4) && !entry.label.equals(INFO)) {
//                System.out.println("https://rechtspraak.cloudant.com/docs/" + ecli + "/data.xml");
//            }
//
//            //NOTE (or to do)   away with enum iterators / lambdas
//            TextBlockInfo.setFeaturesForTrimmedTextContent(textContent, token, ecli);
//            TextBlockInfo.setFeaturesForFullTextContent(textContent, token, ecli);
//
//            // Number
//            TextBlockInfo.setNumberFeature(numbers, textContent, token, (i > 0 ? observations.get(i - 1) : null), ecli);
//
//            if (preserveInformation) token.setProperty(SOURCE_NODE, entry);
//
//            observations.add(token);
//
////            token.setFeatureValue(entry.label, 1.0);
//
//            // Add label
//            labels.add(entry.label);
//        }
////        System.out.println("Features added");
//
////        FeatureVectorSequence featureVectorSequence = new FeatureVectorSequence(observationAlphabet, observations);
//        return new Instance(
//                observations,
//                labels,
//                preserveInformation ? ecli : null,
//                preserveInformation ? doc : null
//        );
//    }
//
//}
