package org.leibnizcenter.rechtspraak.features.info;

import cc.mallet.types.Token;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.features.Patterns;
import org.leibnizcenter.rechtspraak.util.TextPattern;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by maarten on 17-3-16.
 */
public class InfoPatterns {
    public enum InfoPatternsUnormalizedContains implements Patterns.UnnormalizedTextContains {
        CONTAINS_DATE(Pattern.compile("(?:" + Constants.YEAR + "-" + Constants.DAY_MONTH + "-" + Constants.DAY_MONTH +
                        "|" + Constants.DAY_MONTH + "-" + Constants.DAY_MONTH + "-" + Constants.YEAR +
                        "|" + Constants.DAY_MONTH + " {0,3}" +
                        "(" +
                        "jan(\\.|uari)?|feb(\\.|ruari)?" +
                        "|m(aa)?rt\\.?|apr(\\.|il)?|mei|ju[nl](\\.|i)?" +
                        "|aug(\\.|ustus)?|okt(\\.|ober)?" +
                        "|nov(\\.|ember)?|sept?(\\.|t?ember)?|dec(\\.|ember)?" +
                        ")" +
                        " {0,3}" + Constants.YEAR +
                        ")",
                Pattern.CASE_INSENSITIVE)),

        CONTAINS_BRACKETED_TEXT(Pattern.compile("\\[[\\p{L}0-9 ]{0,25}\\]", Pattern.CASE_INSENSITIVE)),

        CONTAINS_LIKELY_ZAAKNR(Pattern.compile("[0-9]{0,15}(/[A-Z]{0,2}[0-9]{1,5}){1,3}",
                Pattern.CASE_INSENSITIVE)
        );

        public static Set<InfoPatternsUnormalizedContains> set = EnumSet.allOf(InfoPatternsUnormalizedContains.class);
        private final TextPattern pattern;

        InfoPatternsUnormalizedContains(TextPattern pattern) {
            this.pattern = pattern;
        }

        InfoPatternsUnormalizedContains(Pattern compile) {
            pattern = new TextPattern(this.name(), compile);
        }

        public static void setFeatureValues(Token t, RechtspraakElement token) {
            set.forEach((p) -> {
                if (Patterns.matches(p, token)) t.setFeatureValue(p.toString(), 1.0);
            });
        }

        @Override
        public boolean matches(String textContent) {
            return this.pattern.find(textContent);
        }

        private static class Constants {
            private static final String YEAR = "(?:[12][90])?[0-9]{2}";
            private static final String DAY_MONTH = "[0-9]{1,2}";
        }
    }

    public enum InfoPatternsNormalizedContains implements Patterns.NormalizedTextContains {
        //
        // INFO
        //
        START_W_INZAKE(new TextPattern("START_W_INZAKE",
                Pattern.compile("^inzake\\b", Pattern.CASE_INSENSITIVE)

        )),

        START_W_AFDELING(new TextPattern("START_W_AFDELING",
                Pattern.compile("^afdeling", Pattern.CASE_INSENSITIVE)

        )),

        START_W_SECTOR(new TextPattern("START_W_SECTOR",
                Pattern.compile("^sector", Pattern.CASE_INSENSITIVE)

        )),

        START_W_HIERNA(new TextPattern("START_W_HIERNA",
                Pattern.compile("^hierna", Pattern.CASE_INSENSITIVE)

        )),

        START_W_LOCATIE(new TextPattern("START_W_LOCATIE",
                Pattern.compile("^locatie", Pattern.CASE_INSENSITIVE)

        )),

        START_W_ZAAK_ROL_NR(new TextPattern("START_W_ZAAK_ROL_NR",
                Pattern.compile("(?:kenmerk|(?:(?:zaak|rol).{0,15})?n(?:umme)?[ro]\\.?)",
                        Pattern.CASE_INSENSITIVE)

        )),

        START_W_ZAAKNR(new TextPattern("START_W_ZAAKNR",
                Pattern.compile("(?:(?:zaak|rol|parket|ro).{0,15})?n(?:umme)?[ro]\\.?",
                        Pattern.CASE_INSENSITIVE)

        )),


        START_W_WONEND_GEVESTIGD(new TextPattern("START_W_WONEND_GEVESTIGD",
                Pattern.compile("^.{0,15}(wonend|gevestigd)", Pattern.CASE_INSENSITIVE)

        )),

        START_W_DATUM(new TextPattern("START_W_DATUM",
                Pattern.compile("^datum", Pattern.CASE_INSENSITIVE)

        )),

        START_W_PATT(new TextPattern("START_W_IN_DE_ZAAK_VAN",
                Pattern.compile("^(?:in|op) (?:de|het) (?:zaak|beroep)", Pattern.CASE_INSENSITIVE)

        )),

        END_W_KAMER(new TextPattern("END_W_KAMER",
                Pattern.compile("kamer$", Pattern.CASE_INSENSITIVE)

        )),

        END_W_ROLE(new TextPattern("END_W_ROLE",
                Pattern.compile("(((ge)(daagd|machtigd)e?)|appelante?|verweerders?|advocaa?t(en)?)" +
                        "( te \\p{L}+\\b)?\\s{0,3}[^\\p{L}]{0,20}$", Pattern.CASE_INSENSITIVE)
        ));

        public static Set<InfoPatternsNormalizedContains> set = EnumSet.allOf(InfoPatternsNormalizedContains.class);
        private final TextPattern pattern;

        InfoPatternsNormalizedContains(TextPattern pattern) {
            this.pattern = pattern;
        }

        public static void setFeatureValues(Token t, RechtspraakElement token) {
            set.forEach((p) -> {
                if (Patterns.matches(p, token)) t.setFeatureValue(p.toString(), 1.0);
            });
        }

        public boolean matches(String textContent) {
            return pattern.find(textContent);
        }
    }


    public enum InfoPatternsNormalizedMatches implements Patterns.NormalizedTextMatches {
        /**
         * starts with rechtbank/gerechthof with location
         */
        START_WITH_COURT(new TextPattern("START_WITH_COURT", Pattern.compile(
                "^*(de|het)?\\s{0,3}(?:ge)?recht(?:bank|shof) ?(?:te )?",
                Pattern.CASE_INSENSITIVE)

        )),

        /**
         * council of the state
         */
        RAAD_VAN_STATE(new TextPattern("RAAD_VAN_STATE", Pattern.compile(
                "^*(de|het)?\\s{0,3}raad\\s{0,3}van\\s{0,3}state",
                Pattern.CASE_INSENSITIVE)

        )),

        /**
         * starts with high/central council, e.g. 'hoge raad van beroep'
         */
        STARTS_WITH_RAAD(new TextPattern("COUNCIL", Pattern.compile(
                "^(?:de|het)?\\s{0,3}(?:hoge|centrale)?\\s{0,3}(?:raad|college)" +
                        "(?:\\s{0,3}(van|voor))?" +
                        "(?:\\s{0,3}(de|het))?" +
                        "(?:\\s{1,3}\\p{L}+){0,3}",
                Pattern.CASE_INSENSITIVE)

        )),


        EN_VS_CONTRA(new TextPattern("AND_OR_AGAINST",
                Pattern.compile("^(en|vs|tegen|contra)", Pattern.CASE_INSENSITIVE)

        )),

        INZAKE(new TextPattern("INZAKE", Pattern.compile("^inzake$", Pattern.CASE_INSENSITIVE)));


        public static Set<InfoPatternsNormalizedMatches> set = EnumSet.allOf(InfoPatternsNormalizedMatches.class);
        private final TextPattern pattern;

        InfoPatternsNormalizedMatches(TextPattern pattern) {
            this.pattern = pattern;
        }

        public static void setFeatureValues(Token t, RechtspraakElement token) {
            set.forEach((p) -> {
                if (Patterns.matches(p, token)) t.setFeatureValue(p.toString(), 1.0);
            });
        }

        public boolean matches(String s) {
            return this.pattern.matches(s);
        }
    }

}
