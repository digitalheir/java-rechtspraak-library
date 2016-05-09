package org.leibnizcenter.rechtspraak.enricher.crf.features.quote;

import cc.mallet.types.Token;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.features.Patterns;
import org.leibnizcenter.util.TextPattern;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by maarten on 24-3-16.
 */
public class PreBlockQuotePattrns {
    private static final String VERGEMELD = "(?:(?:ge|ver)meld)";
    private static final String VERGEZONDEN = "(?:(?:ge|ver)zonden)";
    private static final String HET_VOLGENDE = "(?: (?:als|het) volg(t|ende))";
    private static final String IN_XXX = "(?: (?:in)(?: (?:de|het))?(?: \\p{L}{3,20}))";

    /**
     * Created by maarten on 23-3-16.
     */
    public static enum Unnormalized implements Patterns.UnnormalizedTextContains {
        END_W_COLON(Pattern.compile(":$", Pattern.CASE_INSENSITIVE)),
        END_W_SEMICOLON(Pattern.compile(";$", Pattern.CASE_INSENSITIVE));


        public static Set<Normalized> set = EnumSet.allOf(Normalized.class);
        private final TextPattern pattern;


        Unnormalized(Pattern compile) {
            this.pattern = new TextPattern(name(), compile);
        }

        public static void setFeatureValues(Token t, RechtspraakElement token) {
            set.forEach((p) -> p.setFeatureValue(t, token));
        }

        public static boolean matchesAny(RechtspraakElement token) {
            for (Normalized p : set) {
                if (Patterns.matches(p, token)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean matchesAny(String s) {
            for (Normalized p : set) {
                if (p.matches(s)) return true;
            }
            return false;
        }

        @Override
        public boolean matches(String textContent) {
            return pattern.find(textContent);
        }

        public void setFeatureValue(Token t, RechtspraakElement token) {
            if (Patterns.matches(this, token)) t.setFeatureValue(name(), 1.0);
        }
    }

    public static enum Normalized implements Patterns.NormalizedTextContains {
        HET_VOLGENDE_BERICHT(Pattern.compile("het volgende bericht" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        DE_HET_VOLGENDE(Pattern.compile("((het|de) )volgende", Pattern.CASE_INSENSITIVE)),
        INHOUDT(Pattern.compile("inhoudt" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        GEGEVENS(Pattern.compile("de volgende gegevens" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        VERZONDEN(Pattern.compile(VERGEZONDEN + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        VOLGENDE(Pattern.compile(HET_VOLGENDE + "" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        ZAKELIJKWEERGEGEVEN(Pattern.compile("zakelijk weergegeven" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        LUIDT_ALS_VOLGT(Pattern.compile("luid(dde|t)? als volgt$", Pattern.CASE_INSENSITIVE)),
        LUIDT(Pattern.compile("luid(t|en)( \\((?: \\p{L}{0,20}){0,10}\\))?$", Pattern.CASE_INSENSITIVE)),
        IS_BEPAALD(Pattern.compile("is (?:als|het) volg(t|ende)? bepaald" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        HET_VOLGENDE_BEPAALD(Pattern.compile(HET_VOLGENDE + " bepaald" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        MEEGEDEELD(Pattern.compile(" med?e(?:ge)?deelde?" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        HET_VOLGENDE_MEEGEDEELD(Pattern.compile(HET_VOLGENDE + " med?e(?:ge)?deelde?" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        HET_VOLGENDE_GESCHREVEN(Pattern.compile(HET_VOLGENDE + " geschreven" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        STAAT_VERMELD(Pattern.compile("(staa[nt]|volgende) " + VERGEMELD + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        ALS_VOLGT_BEANTWOORD(Pattern.compile("als volgt( werd(en)?) [gb]e(antwoord|schreven)" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE)),
        TE_LEZEN(Pattern.compile("(is|valt).*te lezen$", Pattern.CASE_INSENSITIVE)),
        BEANTWOORD_ALS_VOLGT(Pattern.compile("beantwoord(dde|t) als volgt$", Pattern.CASE_INSENSITIVE)),
        VERMELD(Pattern.compile("\\b" + VERGEMELD + "$", Pattern.CASE_INSENSITIVE)),
        STAAT(Pattern.compile("in.*\\bstaat\\b( onder meer)?$", Pattern.CASE_INSENSITIVE)),
        DAARIN_STAAT(Pattern.compile("(daar(in)? )?staa[nt]( " + VERGEMELD + ")?$", Pattern.CASE_INSENSITIVE)),
        OVERWOGEN(Pattern.compile("overwogen$", Pattern.CASE_INSENSITIVE)),
        OVERWOOG(Pattern.compile("overwoog( (de|het))?( [\\p{L}]{1,20})?$", Pattern.CASE_INSENSITIVE)),
        GEWIJZIGD(Pattern.compile("als volgt.*(ge)?wijzig(en|d)$", Pattern.CASE_INSENSITIVE)),
        HOUDT_IN(Pattern.compile("houd(t|en)" +
                "( (v(oorts|erder)))?" +
                "( onder meer)?" +
                HET_VOLGENDE + "?" +
                " in( als volgt)?$", Pattern.CASE_INSENSITIVE)),
        VOORTS(Pattern.compile("^(en )?voorts$", Pattern.CASE_INSENSITIVE)),
        CONTRACT(Pattern.compile("contract", Pattern.CASE_INSENSITIVE)),
        OVEREENKOMST(Pattern.compile("overeenkomst", Pattern.CASE_INSENSITIVE)),
        IS_BEOORDEELD(Pattern.compile("^in.*artikel.*is (?:als|het) volg(t|ende)? (?:[gb]e)oordeelde?$", Pattern.CASE_INSENSITIVE));

        public static Set<Normalized> set = EnumSet.allOf(Normalized.class);
        private final TextPattern pattern;


        Normalized(Pattern compile) {
            this.pattern = new TextPattern(name(), compile);
        }

        public static void setFeatureValues(Token t, RechtspraakElement token) {
            set.forEach((p) -> p.setFeatureValue(t, token));
        }

        public static boolean matchesAny(RechtspraakElement token) {
            for (Normalized p : set) {
                if (Patterns.matches(p, token)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean matchesAny(String s) {
            for (Normalized p : set) {
                if (p.matches(s)) return true;
            }
            return false;
        }

        @Override
        public boolean matches(String textContent) {
            return pattern.find(textContent);
        }

        public void setFeatureValue(Token t, RechtspraakElement token) {
            if (Patterns.matches(this, token)) t.setFeatureValue(name(), 1.0);
        }
    }
}
