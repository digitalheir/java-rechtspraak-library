package org.leibnizcenter.rechtspraak.util;

import deprecated.org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by maarten on 11-2-16.
 */
public class TextBlockInfo {
    //new TextPattern("PREP", Pattern.compile("^(van|in|op)", Pattern.CASE_INSENSITIVE)),
    //new TextPattern("FACT", Pattern.compile(".*\\bfeit.*", Pattern.CASE_INSENSITIVE)),
    //new TextPattern("GROUND", Pattern.compile(".*\\bgrond.*", Pattern.CASE_INSENSITIVE)),
    //new TextPattern("JUDGMENT", Pattern.compile(".*\\b(slotsom|beslis).*", Pattern.CASE_INSENSITIVE)),
    //new TextPattern("CONSIDERED", Pattern.compile(".*\\b(middel|bewijs|straf|maatregel|sanctie|hoofdzaak|overige|incident)\\b.*", Pattern.CASE_INSENSITIVE)),
    //new TextPattern("CONSIDERATION", Pattern.compile(".*\\b((bewijs|straf)?overweg|beoordel|(straf|bewijs)?motivering|beschouwing).*", Pattern.CASE_INSENSITIVE)),
    //new TextPattern("PUNCT", Pattern.compile(".*(:|\\.).*", Pattern.CASE_INSENSITIVE)),

//    private static final String INCREMENTING_NUMBER = "INCREMENTING_NUMBER";
//    private static final String SUBSECTION_NUMBER = "SUBSECTION_NUMBER";
//    private static final String SECTION_NUMBER = "SECTION_NUMBER";
//    private static final String STARTS_WITH_WHITESPACE = "STARTS_WITH_WHITESPACE";
//    private static final String ALL_CAPS = "ALL_CAPS";
//    private static final String LESS_THAN_5_WORDS = "LESS_THAN_5_WORDS";
//    private static final String LESS_THAN_10_WORDS = "LESS_THAN_10_WORDS";
//    private static final String HAS_DATE = "HAS_DATE";
//    private static final String CONTAINS_COURT_INFO = "CONTAINS_COURT_INFO";
//    private static final String S_P_A_C_E_D = "S_P_A_C_E_D";
//    private static final String START_W_AFDELING_OR_SECTOR = "START_W_AFDELING_OR_SECTOR";
//    private static final String AND_OR_AGAINST = "AND_OR_AGAINST";
//    private static final String START_W_HIERNA = "START_W_HIERNA";
//    private static final String START_W_LOCATIE = "START_W_LOCATIE";
//    private static final String LIKELY_ZAAKNR = "LIKELY_ZAAKNR";
//    private static final String END_W_KAMER = "END_W_KAMER";
//    private static final String START_W_DATUM = "START_W_DATUM";
//    private static final String START_END_W_INZAKE = "START_END_W_INZAKE";
//    private static final String WONEND_GEVESTIGD = "WONEND_GEVESTIGD";
//    private static final String PERSON_ROLE = "PERSON_ROLE";
//    private static final String START_W_IN_DE_ZAAK_VAN = "START_W_IN_DE_ZAAK_VAN";
//    private static final String BRACKETED_TEXT = "BRACKETED_TEXT";
//    private static final String ART = "ART";
//    private static final String LEGAL_QUALIFIER = "LEGAL_QUALIFIER";
//    private static final String CASE = "CASE";
//    private static final String PREP = "PREP";
//    private static final String FACT = "FACT";
//    private static final String GROUND = "GROUND";
//    private static final String JUDGMENT = "JUDGMENT";
//    private static final String CONSIDERED = "CONSIDERED";
//    private static final String CONSIDERATION = "CONSIDERATION";
//    private static final String PROCEEDINGS = "PROCEEDINGS";
//    private static final String MORE_THAN_15_WORDS = "MORE_THAN_15_WORDS";
//    private static final String PART_OF_LIST = "PART_OF_LIST";
//    private static final String INT_NUM = "INT_NUM";
//
//    public static void getElementPositionRespectiveToParent() {
//        //TODO
//    }
//
//
//    public static void setFeaturesForTrimmedTextContent(String s, Token token, String ecli) {
//        //
//        // General features
//        //
//        token.setFeatureValue(ALL_CAPS, dbl(Regex.ALL_CAPS.matcher(s).find()));
//        String[] words = s.split("\\s+");// Simple split on whitespace
//        token.setFeatureValue(LESS_THAN_5_WORDS, dbl(words.length < 5));
//        token.setFeatureValue(LESS_THAN_10_WORDS, dbl(words.length < 10));
//        token.setFeatureValue(MORE_THAN_15_WORDS, dbl(words.length > 15));
//        token.setFeatureValue(HAS_DATE, dbl(s.find(".*\\b(?:19|20)[0-9]{2}\\b.*")));
//        token.setFeatureValue(S_P_A_C_E_D, dblMatches(s, Regex.S_P_A_C_E_D));
//
//        //
//        // Features specifically about *.info
//        //
//        setInfoFeatures(s, token);
//
//        //
//        // Features specifically about section titles
//        //
//        setSectionTitlesFeatures(s, token);
//    }
//
//    public static void setInfoFeatures(String s, Token token) {
//        token.setFeatureValue(CONTAINS_COURT_INFO, dblMatches(s, Regex.Courts));
//        /**
//         * Indicating metadata about the branch of law
//         */
//        token.setFeatureValue(START_W_AFDELING_OR_SECTOR, dblMatches(s, Regex.START_W_AFDELING, Regex.START_W_SECTOR));
//        /**
//         * Indicating that we are introducing the parties
//         */
//        token.setFeatureValue(AND_OR_AGAINST, dblMatches(s, Regex.AND_OR_AGAINST));
//        /**
//         * 'Hierna te noemen', indicating we're introducing the parties
//         */
//        token.setFeatureValue(START_W_HIERNA, dblMatches(s, Regex.START_W_HIERNA));
//        token.setFeatureValue(START_W_LOCATIE, dblMatches(s, Regex.START_W_LOCATIE));
//        token.setFeatureValue(LIKELY_ZAAKNR, dblMatches(s, Regex.ZAAK_ROL_NR, Regex.ZAAKNR_FORMAT));
//        token.setFeatureValue(END_W_KAMER, dblMatches(s, Regex.END_W_KAMER));
//        token.setFeatureValue(START_W_DATUM, dblMatches(s, Regex.START_W_DATUM));
//        token.setFeatureValue(START_END_W_INZAKE, dblMatches(s, Regex.START_END_W_INZAKE));
//        token.setFeatureValue(WONEND_GEVESTIGD, dblMatches(s, Regex.WONEND_GEVESTIGD));
//        token.setFeatureValue(PERSON_ROLE, dblMatches(s, Regex.PERSON_ROLE));
//        token.setFeatureValue(START_W_IN_DE_ZAAK_VAN, dblMatches(s, Regex.START_W_IN_DE_ZAAK_VAN));
//        /**
//         * Indiciating we're talking about anonymized people, so maybe introducing parties
//         */
//        token.setFeatureValue(BRACKETED_TEXT, dblMatches(s, Regex.BRACKETED_TEXT));
//    }
//
//    public static void setSectionTitlesFeatures(String s, Token token) {
//
//
//        // Generated code:
//        FoundTitles.setAsFeatures(s, token);
//    }
//
//    public static void setFeaturesForFullTextContent(String textContent, Token token, String ecli) {
//        token.setFeatureValue(STARTS_WITH_WHITESPACE, dbl(Regex.START_W_WHITESPACE.matcher(textContent).find()));
//    }
    ///TODO /|\

    /**
     * @param s    Input string
     * @param ptts Regex patterns
     * @return 1.0 if find any pattern, 0.0 if it doesn't match anything
     */
    public static double dblMatches(String s, Pattern... ptts) {
        return dbl(matchesAny(s, ptts));
    }

    /**
     * @param s    Input string
     * @param ptts Regex patterns
     * @return 1.0 if find any pattern, 0.0 if it doesn't match anything
     */
    public static boolean matchesAny(String s, Pattern... ptts) {
        if (ptts.length == 0) {
            return true;
        }
        if (ptts.length == 1) {
            return (ptts[0].matcher(s).matches());
        }
        if (ptts.length == 2) {
            return (ptts[0].matcher(s).matches()) || (ptts[1].matcher(s).matches());
        }
        for (Pattern ptt : ptts) {
            if (ptt.matcher(s).matches()) return true;
        }
        return false;
    }

    public static double dbl(boolean b) {
        return b ? 1.0 : 0.0;
    }

    public static boolean isAllWhitespace(String s) {
        return Regex.ALL_WHITESPACE.matcher(s).matches();
    }

    public static boolean hasElementsOrLinebreaksAsChildren(Element child) {
        NodeList childNodes = child.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if ((childNodes.item(i).getNodeType() == Element.PROCESSING_INSTRUCTION_NODE
                    && ((ProcessingInstruction) childNodes.item(i)).getTarget().equals("linebreak"))
                    ||
                    Xml.isElement(childNodes.item(i))) {
                return true;
            }
        }
        return false;
    }

    public static class Feature {
        public final double value;
        public final String name;

        public Feature(String name, double value) {
            this.value = value;
            this.name = name;
        }
    }

    public static class Space {
        public static final Map<Label, Feature> features = new EnumMap<>(Label.class);
        public static final Map<Label, Filter> filters = new EnumMap<>(Label.class);

        static {
            for (Label l : Labels.set) {
                features.put(l, new Feature(l));
                filters.put(l, new Filter(l));
            }
        }

        public static String unspace(String normalizedText) {
            return normalizedText.replaceAll("\\s", "");
        }

        public static boolean isSpaced(String normalizedText) {
            return S_P_A_C_E_D.matcher(normalizedText).matches();
        }

        public static class Feature extends CrfFeature<RechtspraakElement, Label> {
            private final Label label;

            public Feature(Label l) {
                this.label = l;
            }

            @Override
            public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
                return Doubles.asDouble(sequence[indexInSequence].isSpaced);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Feature feature = (Feature) o;

                return label == feature.label;

            }

            @Override
            public int hashCode() {
                return label.hashCode();
            }
        }

        public static class Filter extends deprecated.org.crf.crf.filters.Filter<RechtspraakElement, Label> {
            private final Label label;

            public Filter(Label l) {
                this.label = l;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Filter filter = (Filter) o;

                return label == filter.label;

            }

            @Override
            public int hashCode() {
                return label != null ? label.hashCode() : 0;
            }
        }
    }

    public static class Regex {
        public static final Pattern ALL_WHITESPACE =
                Pattern.compile("^\\s*$");

        public static final Pattern NON_ALPHANUMERIC = Pattern.compile("^" + STR_NON_ALPHANUMERIC);
        public static final Pattern YYYY_MM_DD = Pattern.compile("^[0-9]{4}-[01]?[0-9]-[0-3]?[0-9]\\b");
        public static final Pattern DD_MON_YYYY = Pattern.compile("^[0-3]?[0-9]\\s{0,3}(jan|feb|maart|apr|mei|jun|jul|aug|sep|okt|nov|dec)\\w{0,20}(?:[0-9]{4})?\\b");
        /**
         * Starts with whitespace, then a decimal or roman numeral
         */
        public static final Pattern START_WITH_NUM = Pattern.compile(
                "^\\s*(" +
                        // Leading parenthesis
                        "(?:\\( {0,2})?" +
                        "(" +
                        // Leading numbers
                        "(?:" +
                        "(?:([0-9]+)" +
                        "|(?:i{1,3}\\b)" +
                        "|(?:i?vi{0,3}\\b)" +
                        "|(?:i?xi{0,3}\\b))\\.)*" +
                        //
                        "(?:(?:[0-9]+)" +
                        "|(?:i{1,3}\\b)" +
                        "|(?:i?vi{0,3}\\b)" +
                        "|(?:[a-z]\\b)" +
                        "|" + STR_NON_ALPHANUMERIC + ")" +
                        ")" +
                        // Whitespace
                        "\\s{0,3}" +
                        // Terminal character
                        "([:\\.\\-;#\\)°º•%&@!?><\\+=\\]]{0,2}))",
                Pattern.CASE_INSENSITIVE);
    }

}
