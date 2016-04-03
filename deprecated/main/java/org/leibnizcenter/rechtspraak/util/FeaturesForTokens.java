//package org.leibnizcenter.rechtspraak.util;
//
//import java.util.regex.Pattern;
//
///**
// * Created by maarten on 28-1-16.
// */
//public enum FeaturesForTokens {
//    //    describes a number (a sequence of digits, or a roman numeral)
//    NUM((s, i) -> toDouble(Regex.NUM.matcher(s.get(i)).find())),
//
//    PUNCT((s, i) -> toDouble(Regex.PUNCT, s.get(i))),
//
//    //    describes an article (the/a)
//    ART((s, i) -> toDouble(Regex.ART, s.get(i))),
//
////    //    describes a preposition
////    PREP((s, i) -> toDouble(Regex.PREP, s.get(i))),
////
////    MISC_LEGAL_TERMS((s, i) -> toDouble(Regex.LEGAL_QUALIFIER, s.get(i))),
////
////    CASE((s, i) -> toDouble(Regex.CASE, s.get(i))),
////
////    //    stem of 'feit' ('fact')
////    FACT((s, i) -> toDouble(Regex.FACT, s.get(i))),
////
////    //    stem of 'grond' ('ground')
////    GROUND((s, i) -> toDouble(Regex.GROUND, s.get(i))),
////
////    //    stem of words pertaining to a 'judgment' section
////    JUDGMENT((s, i) -> toDouble(Regex.JUDGMENT, s.get(i))),
////
////    //    stem of words pertaining to a 'considerations' section
////    CONSIDERATION((s, i) -> toDouble(Regex.CONSIDERATION, s.get(i))),
////
////    //    stem of words pertaining to a 'proceedings' section
////    PROCEEDINGS((s, i) -> toDouble(Regex.PROCEEDINGS, s.get(i))),
////
////
////    CONSIDERED((s, i) -> toDouble(Regex.CONSIDERED, s.get(i))),
//
//    SHORT_SENTENCE(((s, i) -> toDouble(s.size() < 6))),
//
//    WITHIN_FIRST_5_WORDS((s, i) -> toDouble(i < 5)),
//    WITHIN_FIRST_3_WORDS((s, i) -> toDouble(i < 3)),
//    SENTENCE_UP_TO_6((s, i) -> toDouble(s.size() <= 6)),
//    SENTENCE_UP_TO_10((s, i) -> toDouble(s.size() <= 10)),
//    FIRST_WORD((s, i) -> toDouble(i == 0));
//
//    private static double toDouble(Pattern art, String s) {
//        return toDouble(art.matcher(s).find());
//    }
//
//
//    public static double toDouble(boolean b) {
//        return b ? 1.0 : 0.0;
//    }
//
//    FeaturesForTokens(FeatureFunctionTokenized regex) {
//        this.func = regex;
//    }
//
//    public FeatureFunctionTokenized func;
//
//    public static FeaturesForTokens[] all() {
//        return values();
//    }
//
//    private static class Regex {
//        private static final Pattern NUM = Pattern.compile("^(([0-9]+)|(i{1,3})|(i?vi{0,3})|(i?xi{0,3}))$", Pattern.CASE_INSENSITIVE);
//        private static final Pattern ART = Pattern.compile("^(de|het|een)$", Pattern.CASE_INSENSITIVE);
//        private static final Pattern LEGAL_QUALIFIER = Pattern.compile("(eerst|tweed|verdere|vaststaande|terbeschikkingstelling)", Pattern.CASE_INSENSITIVE);
//        private static final Pattern CASE = Pattern.compile("(geschil|geding|hoger|beroep|aanleg)", Pattern.CASE_INSENSITIVE);
//        private static final Pattern PREP = Pattern.compile("^(van|in|op)$", Pattern.CASE_INSENSITIVE);
//        private static final Pattern FACT = Pattern.compile("feit", Pattern.CASE_INSENSITIVE);
//        private static final Pattern GROUND = Pattern.compile("grond", Pattern.CASE_INSENSITIVE);
//        private static final Pattern JUDGMENT = Pattern.compile("(slotsom|beslis)", Pattern.CASE_INSENSITIVE);
//        private static final Pattern CONSIDERED = Pattern.compile("(middel|bewijs|straf|maatregel|sanctie|hoofdzaak|overige|incident)", Pattern.CASE_INSENSITIVE);
//        private static final Pattern CONSIDERATION = Pattern.compile("((bewijs|straf)?overweg|beoordel|(straf|bewijs)?motivering|beschouwing)", Pattern.CASE_INSENSITIVE);
//        private static final Pattern PROCEEDINGS = (Pattern.compile("(voor)?geschied|onsta|((proces)?(verlo|gang)|procedu|ontstaan|loop)", Pattern.CASE_INSENSITIVE));
//        private static final Pattern PUNCT = Pattern.compile("(:|\\.)");
//    }
//
//}
