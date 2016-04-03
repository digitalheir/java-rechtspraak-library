package org.leibnizcenter.rechtspraak.features.textpatterns;

import org.leibnizcenter.rechtspraak.features.elementpatterns.interfaces.NamedElementFeatureFunction;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.numbering.ListMarking;
import org.leibnizcenter.rechtspraak.tokens.quote.Quote;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.util.Regex;
import org.leibnizcenter.rechtspraak.util.StringFeature;
import org.leibnizcenter.rechtspraak.util.Strings2;

import java.util.List;
import java.util.function.Function;

/**
 * Created by maarten on 31-3-16.
 */
public enum GeneralTextPattern implements StringFeature, NamedElementFeatureFunction {
    ENDS_WITH_NON_LETTER(s -> Regex.ENDS_WITH_NON_LETTER.matcher(s).find()),
    ENDS_WITH_LETTER(s -> Regex.ENDS_WITH_LETTER.matcher(s).find()),
    /**
     * Apply on trimmed text
     */
    IS_SPACED(s -> Regex.S_P_A_C_E_D.matcher(s).matches()),
    START_W_LOWER_CASE_LETTER(s -> Regex.START_W_LOWERCASE_LETTER.matcher(s.trim()).matches()),
    START_W_UPPER_CASE_LETTER(s -> Regex.START_W_UPPERCASE_LETTER.matcher(s.trim()).matches()),
    IS_ALL_CAPS(GeneralTextPattern::hasOnlyUppercaseLetters),
    END_W_SEMICOLON(s -> Strings2.lastCharIs(s.trim(), ';')),
    END_W_COLON(s -> Strings2.lastCharIs(s.trim(), ':')),
    START_W_LIST_MARKING(ListMarking::startsWithListMarking),
    START_W_CIRCLE_LIST_MARKING(s -> ListMarking.CLASSIC_ROUND_LIST_MARKINGS.contains(s.trim().charAt(0))),
    START_W_QUESTIONABLE_LIST_MARKING(s -> ListMarking.QUESTIONABLE_LIST_MARKINGS.contains(s.trim().charAt(0))),
    START_W_HORIZONTAL_LIST_MARKING(s -> ListMarking.HORIZONTAL_LIST_MARKING.contains(s.trim().charAt(0))),
    START_W_VERTICAL_LIST_MARKING(s -> ListMarking.VERTICAL_LIST_MARKINGS.contains(s.trim().charAt(0))),
    ALL_NUMERIC(s -> Regex.ALL_DIGITS.matcher(s.trim()).matches()),
    END_W_QUOTE(Quote::endsWithQuote),
    END_W_QUOTE_STRICT(Quote::endsWithQuoteStrict),
    END_W_SINGLE_QUOTE(Quote::endsWithSingleQuote),
    END_W_DOUBLE_QUOTE(Quote::endsWithDoubleQuote),
    END_W_DOUBLE_QUOTE_STRICT(Quote::endsWithDoubleQuoteStrict),
    MULTIPLE_QUOTES(Quote::containsMultipleQuotes);

    public static Boolean hasOnlyUppercaseLetters(String s) {
        boolean hasAtLeastOneLetter = false;
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c) && !Character.isUpperCase(c)) return false;
            if (!hasAtLeastOneLetter && Character.isLetter(c)) hasAtLeastOneLetter = true;
        }
        return hasAtLeastOneLetter;
    }

    private final Function<String, Boolean> f;

    public boolean apply(String s) {
        Boolean bool = f.apply(s);
        if (bool == null) throw new NullPointerException();
        return bool;
    }

    public boolean apply(List<TokenTreeLeaf> element, int ix) {
        Boolean bool = f.apply(element.get(ix).getTextContent());
        if (bool == null) throw new NullPointerException();
        return bool;
    }

    GeneralTextPattern(Function<String, Boolean> f) {
        this.f = f;
    }

    public static boolean followsLineBreak(List<TokenTreeLeaf> elements, int ix) {
        return ix > 0 && elements.get(ix - 1) instanceof Newline;
    }

    public static boolean containsRefToWoonplaats(List<TokenTreeLeaf> tokens, int i) {
        return Regex.CONTAINS_REF_TO_WOONPLAATS.matcher(tokens.get(i).getTextContent()).find();
    }

    public static boolean containsDate(List<TokenTreeLeaf> tokens, int i) {
        return Regex.CONTAINS_DATE.matcher(tokens.get(i).getTextContent()).find();
    }

    public static boolean containsBracketedText(List<TokenTreeLeaf> tokens, int i) {
        return Regex.CONTAINS_BRACKETED_TEST.matcher(tokens.get(i).getTextContent()).find();
    }

    public static boolean containsLikelyZaaknr(List<TokenTreeLeaf> elements, int i) {
        return Regex.CONTAINS_LIKELY_ZAAKNR.matcher(elements.get(i).getTextContent()).find();
    }

    public static boolean containsRefToPersonage(List<TokenTreeLeaf> elements, int i) {
        return Regex.CONTAINS_REF_TO_PERSONAGE.matcher(elements.get(i).getTextContent()).find();
    }
}
