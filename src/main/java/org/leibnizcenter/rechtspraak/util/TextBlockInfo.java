package org.leibnizcenter.rechtspraak.util;

import com.google.common.collect.Sets;
import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.Label;
import org.leibnizcenter.rechtspraak.markup.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.features.TokenMatch;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by maarten on 11-2-16.
 */
public class TextBlockInfo {
    public static Collection<TextPattern> patterns = Sets.newHashSet(
            /**
             * starts with rechtbank/gerechthof with location
             */
            new TextPattern("START_WITH_COURT", Pattern.compile(
                    "^*(de|het)?\\s*(?:ge)?recht(?:bank|shof) ?(?:te )?(.*)",
                    Pattern.CASE_INSENSITIVE)),
            /**
             * council of the state
             */
            new TextPattern("RAAD_VAN_STATE", Pattern.compile(
                    "^*(de|het)?\\s*raad\\s+van\\s+state",
                    Pattern.CASE_INSENSITIVE)),
            /**
             * starts with high/central council, e.g. 'hoge raad van beroep'
             */
            new TextPattern("STARTS_WITH_RAAD", Pattern.compile(
                    "^*(de|het)?\\s*(?:hoge|centrale)\\s+raad.*",
                    Pattern.CASE_INSENSITIVE)),
            //new TextPattern("START_W_WHITESPACE", Pattern.compile("^(\\s+).*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("AND_OR_AGAINST", Pattern.compile("^(en|tegen|contra)", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_AFDELING", Pattern.compile("^afdeling.*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_SECTOR", Pattern.compile("^sector.*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_HIERNA", Pattern.compile("^hierna.*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_LOCATIE", Pattern.compile("^locatie.*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_ZAAK_ROL_NR", Pattern.compile("(?:kenmerk|(?:(?:zaak|rol).{0,15})?n(?:umme)?[ro]\\.?).*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_ZAAKNR_FORMAT", Pattern.compile("(?:(?:zaak|rol|parket|ro).{0,15})?n(?:umme)?[ro]\\.?.*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("END_W_KAMER", Pattern.compile(".*kamer$", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_DATUM", Pattern.compile("^datum.*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_END_W_INZAKE", Pattern.compile("^inzake$", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_WONEND_GEVESTIGD", Pattern.compile("^.{0,15}wonend|gevestigd.*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("END_W_PERSON_ROLE", Pattern.compile(".*ge(daagd|machtigd)e?$", Pattern.CASE_INSENSITIVE)),
            new TextPattern("START_W_IN_DE_ZAAK_VAN", Pattern.compile("^(?:in|op) (?:de|het) (?:zaak|beroep) .*", Pattern.CASE_INSENSITIVE)),
            new TextPattern("CONTAINS_BRACKETED_TEXT", Pattern.compile(".*\\[[a-z ]+\\].*", Pattern.CASE_INSENSITIVE)),
            ///////////////////////
            // Patterns for titles
            ///////////////////////
            new TextPattern("START_W_LEGAL_QUALIFIER", Pattern.compile("^(eerst|tweed|verdere|vaststaande|terbeschikkingstelling).*", Pattern.CASE_INSENSITIVE))
            //new TextPattern("PREP", Pattern.compile("^(van|in|op)", Pattern.CASE_INSENSITIVE)),
            //new TextPattern("FACT", Pattern.compile(".*\\bfeit.*", Pattern.CASE_INSENSITIVE)),
            //new TextPattern("GROUND", Pattern.compile(".*\\bgrond.*", Pattern.CASE_INSENSITIVE)),
            //new TextPattern("JUDGMENT", Pattern.compile(".*\\b(slotsom|beslis).*", Pattern.CASE_INSENSITIVE)),
            //new TextPattern("CONSIDERED", Pattern.compile(".*\\b(middel|bewijs|straf|maatregel|sanctie|hoofdzaak|overige|incident)\\b.*", Pattern.CASE_INSENSITIVE)),
            //new TextPattern("CONSIDERATION", Pattern.compile(".*\\b((bewijs|straf)?overweg|beoordel|(straf|bewijs)?motivering|beschouwing).*", Pattern.CASE_INSENSITIVE)),
            //new TextPattern("PROCEEDINGS", Pattern.compile("\\b(voor)?geschied|onsta|((proces)?(verlo|gang)|procedu|ontstaan|loop)")),
            //new TextPattern("PUNCT", Pattern.compile(".*(:|\\.).*", Pattern.CASE_INSENSITIVE)),

            ///**
            // * Any combination of uppercase characters and whitespace
            // */
            //new TextPattern("ALL_CAPS", Pattern.compile("^(?:\\s*\\p{Lu}\\s*)+$", Pattern.CASE_INSENSITIVE)),
    );
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
//        token.setFeatureValue(ALL_CAPS, dbl(Regex.ALL_CAPS.matcher(s).matches()));
//        String[] words = s.split("\\s+");// Simple split on whitespace
//        token.setFeatureValue(LESS_THAN_5_WORDS, dbl(words.length < 5));
//        token.setFeatureValue(LESS_THAN_10_WORDS, dbl(words.length < 10));
//        token.setFeatureValue(MORE_THAN_15_WORDS, dbl(words.length > 15));
//        token.setFeatureValue(HAS_DATE, dbl(s.matches(".*\\b(?:19|20)[0-9]{2}\\b.*")));
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
//        token.setFeatureValue(STARTS_WITH_WHITESPACE, dbl(Regex.START_W_WHITESPACE.matcher(textContent).matches()));
//    }
    ///TODO /|\

    /**
     * @param s    Input string
     * @param ptts Regex patterns
     * @return 1.0 if matches any pattern, 0.0 if it doesn't match anything
     */
    public static double dblMatches(String s, Pattern... ptts) {
        return dbl(matchesAny(s, ptts));
    }

    /**
     * @param s    Input string
     * @param ptts Regex patterns
     * @return 1.0 if matches any pattern, 0.0 if it doesn't match anything
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

    public static class Feature {
        public final double value;
        public final String name;

        public Feature(String name, double value) {
            this.value = value;
            this.name = name;
        }
    }

    public static class Space {
        public static final Pattern S_P_A_C_E_D = Pattern.compile("\\s*(?:[A-Z] ){2,}[A-Z]\\s*", Pattern.CASE_INSENSITIVE);
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

        public static class Filter extends org.crf.crf.filters.Filter<RechtspraakElement, Label> {
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

        /**
         * Starts with whitespace, then a decimal or roman numeral
         */
        public static final Pattern START_WITH_NUM = Pattern.compile(
                "^\\s*((?:([0-9]+)|(i{1,3}\\b)|(i?vi{0,3}\\b)|(i?xi{0,3}\\b))(\\.[0-9]+)*)\\s{0,3}([:\\.-;#]?).*",
                Pattern.CASE_INSENSITIVE);
        public static final Pattern ALL_WHITESPACE =
                Pattern.compile("^\\s*$");
    }

    public static class TextPattern {
        public final Map<Label, TokenMatch.Filter> filters = new EnumMap<>(Label.class);
        public final Map<Label, TokenMatch.Feature> features = new EnumMap<>(Label.class);
        private final String name;
        private final Pattern pattern;

        public TextPattern(String name, Pattern regex) {
            this.name = name;
            this.pattern = regex;
            for (Label l : Label.values()) {
                filters.put(l, new TokenMatch.Filter(l, pattern));
                features.put(l, new TokenMatch.Feature(l, pattern));
            }
        }

        public boolean matches(String text) {
            return pattern.matcher(text).matches();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TextPattern that = (TextPattern) o;

            return name.equals(that.name) && pattern.equals(that.pattern) && filters.equals(that.filters) && features.equals(that.features);

        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + pattern.hashCode();
            result = 31 * result + filters.hashCode();
            result = 31 * result + features.hashCode();
            return result;
        }
    }
}
