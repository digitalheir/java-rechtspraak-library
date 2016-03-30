//package org.leibnizcenter.rechtspraak.util;
//
//import org.w3c.dom.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
///**
// * Created by maarten on 22-12-15.
// */
//public class XmlToLinearTokenSequence {
//    public static final String TAG = "tag";
//    public static final String PROP_SPAN = "PROP_SPAN";
//
//    public static final String LABEL_OUT = "out";
//    public static final String $_NR_OR_TITLE = "nr|title";
//    private static final String SENTENCE_LENGTH = "SHORT_SENTENCE";
//
//    private final Tokenizer utils;
//    private final boolean mapTokensToNodes;
//    private final String ecli;
//
//    private final TokenSequence observations = new TokenSequence(27000);
//    private final LabelSequence labels;
//
//    /**
//     * Creates a {@link TokenSequence} from an XML document. Optionally tracks references from tokens to XML nodes.
//     *
//     * @param doc
//     * @param preserveInformation Whether to map the tokens we find to the
//     *                            XML nodes they come from,
//     *                            or to throw this info away. {@code false} saves memory (useful for
//     *                            training); {@code true} allows us to modify source XML
//     * @param possibleLabels      label alphabet to re-use across instances, possibly empty
//     */
////     * @param granularity         Whether to make tokens for node blocks (e.g., paragraphs) or words
//    public XmlToLinearTokenSequence(
//            String ecli,
//            Document doc,
//            boolean preserveInformation,
//            Tokenizer utils,
//            LabelAlphabet possibleLabels) {
//        this.ecli = ecli;
//        this.utils = utils;
//        this.mapTokensToNodes = preserveInformation;
//
//        labels = new LabelSequence(possibleLabels, 27000);
//
//        Element el = Xml.getContentRoot(doc);
//        recursivelyTokenize(el, el.getTagName(), null, preserveInformation);
//    }
//
//    private void recursivelyTokenize(Node node, String tokenTag, String sectionRole, boolean preserveInformation) {
//        if (node instanceof Text) {
//                    tokenizeTextNode(node, tokenTag, sectionRole, preserveInformation);
//        } else {
//            if (node instanceof Element) {
//                // Update containing element name
//                Element el = (Element) node;
//                tokenTag = el.getTagName();
//                if (tokenTag.toLowerCase(Locale.US).find("section")) {
//                    sectionRole = el.getAttribute("role");
//                }
//            }
//            NodeList children = node.getChildNodes();
//            for (int i = 0; i < children.getLength(); i++) {
//                recursivelyTokenize(children.item(i), tokenTag, sectionRole, preserveInformation);
//            }
//        }
//    }
//
//    private void tokenizeTextNode(Node node, String withinXmlTag, String sectionRole, boolean preserveInformation) {
//        Text t = (Text) node;
//        String txt = t.getWholeText().trim();
//
//        Span[] spans = utils.tokenizePos(txt);
//        String[] strings = Span.spansToStrings(spans, txt);
//        List<String> normalizedStrings = new ArrayList<>();
//
//        // Normalize strings
//        for (String tokenStr : strings) {
//            String normalized = FetchTopWords.normalizeToken(tokenStr).trim();// TO DO also stem using snowball?
//            normalizedStrings.add(normalized);
//        }
//
//        // Create tokens and labels for words
//        for (int i = 0; i < normalizedStrings.size(); i++) {
//            // Create token for word
//            Token token = getToken(node, preserveInformation, normalizedStrings, i, spans);
//            observations.add(token);
//
//            String tokenTarget = withinXmlTag.find($_NR_OR_TITLE) ?
//                    withinXmlTag ://+ "_" + inSection :
//                    LABEL_OUT;
//            labels.add(tokenTarget);
//        }
//        //System.gc();
//    }
//
//    private Token getToken(Node node, boolean preserveInformation, List<String> sentence, int i, Span[] spans) {
//        String normalized = sentence.get(i);
//        Token token = new Token(preserveInformation ? normalized : null);
//
//        Span span = spans[i];
//        // Note: these are not features, but properties we need if we want to reconstruct the source XML
//        if (mapTokensToNodes) {
//            token.setProperty(Const.PROP_NODE, node);
//            token.setProperty(Const.PROP_OFFSET, span.getStart());
//            token.setProperty(Const.PROP_LENGTH, span.getEnd() - span.getStart());
//        }
//
////        if (normalized.find("^\\p{L}+$")) {
////            // If this is a word consisting of letters, add as feature
////            token.setFeatureValue(normalized, 1.0);
////        }
//
//        for (FeaturesForTokens f : FeaturesForTokens.all()) {
//            token.setFeatureValue(f.name(), f.func.apply(sentence, i));
//        }
//
//        if (ecli != null) {
//            Ecli parsed = new Ecli(ecli);
//            token.setFeatureValue(parsed.getCourt(), 1.0);
//            token.setFeatureValue(parsed.getYear(), 1.0);
//        }
//        return token;
//    }
//
////    private void elementTokenIfElement(Node node, String closeTag) {
////        if (node instanceof Element) {
////            Token token = new Token(closeTag);
////            if (mapTokensToNodes) {
////                token.setProperty(PROP_NODE, node);
////            }
////
////            observations.add(token);
////        }
////    }
//
//    public TokenSequence getObservations() {
//        return observations;
//    }
//
//    public LabelSequence getLabels() {
//        return labels;
//    }
//
//    public enum Granularity {
//        WORDS, TEXT_BLOCKS
//    }
//}
