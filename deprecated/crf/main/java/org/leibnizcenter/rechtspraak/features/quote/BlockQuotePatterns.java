package org.leibnizcenter.rechtspraak.tokens.features.quote;

import cc.mallet.types.Token;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.features.Patterns;
import org.leibnizcenter.util.TextPattern;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by maarten on 23-3-16.
 */
public enum BlockQuotePatterns implements Patterns.UnnormalizedTextContains {
    LID(Pattern.compile("^lid", Pattern.CASE_INSENSITIVE)),
    ARTIKEL(Pattern.compile("^artikel", Pattern.CASE_INSENSITIVE)),

    DOTDOTDOT(Pattern.compile("\\(\\.\\.\\.\\)", Pattern.CASE_INSENSITIVE)),
    IK(Pattern.compile("\\bik\\b", Pattern.CASE_INSENSITIVE)),
    U(Pattern.compile("\\bu\\b", Pattern.CASE_INSENSITIVE)),
    JIJ(Pattern.compile("\\bj(?:e|ij)\\b", Pattern.CASE_INSENSITIVE)),
    MIJN(Pattern.compile("\\bmijn\\b", Pattern.CASE_INSENSITIVE)),
    //ZIJN(Pattern.compile("\\bzijn\\b", Pattern.CASE_INSENSITIVE)),
    UW(Pattern.compile("\\buw\\b", Pattern.CASE_INSENSITIVE)),

    MULTIPLE_QUOTES(Pattern.compile(Constants.CHAR_ANY_QUOTE + ".+" + Constants.CHAR_ANY_QUOTE, Pattern.CASE_INSENSITIVE)),

    START_W_QUOTE(Pattern.compile("^" + Constants.CHAR_ANY_QUOTE, Pattern.CASE_INSENSITIVE)),
    END_W_QUOTE(Pattern.compile(Constants.CHAR_ANY_QUOTE + "[^\\w]{0,3}$", Pattern.CASE_INSENSITIVE)),
    END_W_QUOTE_STRICT(Pattern.compile(Constants.CHAR_ANY_QUOTE + "$", Pattern.CASE_INSENSITIVE)),

    START_W_DOUBLE_QUOTE(Pattern.compile("^" + Constants.CHAR_DOUBLE_QUOTE, Pattern.CASE_INSENSITIVE)),
    END_W_DOUBLE_QUOTE(Pattern.compile(Constants.CHAR_DOUBLE_QUOTE + "[^\\w]{0,3}$", Pattern.CASE_INSENSITIVE)),
    SINGLE_FINAL_DOUBLE_QUOTE(Pattern.compile("^" + Constants.CHAR_NOT_DOUBLE_QUOTE + "*"
            + Constants.CHAR_DOUBLE_QUOTE + "[^\\w]{0,3}$", Pattern.CASE_INSENSITIVE)),
    END_W_DOUBLE_QUOTE_STRICT(Pattern.compile(Constants.CHAR_DOUBLE_QUOTE + "$", Pattern.CASE_INSENSITIVE));


    public static Set<BlockQuotePatterns> set = EnumSet.allOf(BlockQuotePatterns.class);
    private final TextPattern pattern;


    BlockQuotePatterns(Pattern compile) {
        this.pattern = new TextPattern(name(), compile);
    }

    public static void setFeatureValues(Token t, RechtspraakElement token) {
        set.forEach((p) -> p.setFeatureValue(t, token));
    }

    @Override
    public boolean matches(String textContent) {
        return pattern.find(textContent);
    }

    public void setFeatureValue(Token t, RechtspraakElement token) {
        if (Patterns.matches(this, token)) t.setFeatureValue(name(), 1.0);
    }

}
