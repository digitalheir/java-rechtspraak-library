package org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns;

import org.leibnizcenter.rechtspraak.tagging.crf.features.elementpatterns.interfaces.NamedElementFeatureFunction;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.numbering.ListMarking;
import org.leibnizcenter.rechtspraak.tokens.quote.Quote;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.util.Regex;
import org.leibnizcenter.util.StringFeature;
import org.leibnizcenter.util.Strings2;

import java.util.List;
import java.util.function.Function;

/**
 * Features for general text patterns (capitalization, string length etc)
 * Created by maarten on 31-3-16.
 */
public enum GeneralTextFeature implements StringFeature, NamedElementFeatureFunction {
    ENDS_WITH_NON_LETTER_OR_NUMBER_OR_BRACKET(s -> Regex.ENDS_WITH_NON_ALPHANUMERIC_OR_BRACKET.matcher(s).find()),
    ENDS_WITH_LETTER_OR_NUMBER_OR_BRACKET(s -> Regex.ENDS_WITH_ALPHANUMERIC_OR_BRACKET.matcher(s).find()),

    LESS_THAN_5_LETTERS(lessThanNLetters(5)),
    LESS_THAN_10_LETTERS(lessThanNLetters(10)),
    LESS_THAN_20_LETTERS(lessThanNLetters(20)),
    LESS_THAN_30_LETTERS(lessThanNLetters(30)),
    LESS_THAN_50_LETTERS(lessThanNLetters(50)),
    LESS_THAN_75_LETTERS(lessThanNLetters(75)),

    MORE_THAN_5_LETTERS(moreThanNLetters(5)),
    MORE_THAN_10_LETTERS(moreThanNLetters(10)),
    MORE_THAN_20_LETTERS(moreThanNLetters(20)),
    MORE_THAN_30_LETTERS(moreThanNLetters(30)),
    MORE_THAN_50_LETTERS(moreThanNLetters(50)),
    MORE_THAN_75_LETTERS(moreThanNLetters(75)),

    // TODO is noun phrase without verb -> likely title

    /**
     * Apply on trimmed text
     * NOTE: won't work because all spaced letter are tokenised as potential nrs
     */
    IS_SPACED(s -> Regex.S_P_A_C_E_D.matcher(s).matches()),
    ARTIKEL(s -> Regex.ARTIKEL.matcher(s.trim()).matches()),
    FEIT(s -> Regex.FEIT.matcher(s.trim()).matches()),
    START_W_FEIT(s -> Regex.START_W_FEIT.matcher(s.trim()).find()),
    START_W_ARTIKEL(s -> Regex.START_W_ARTIKEL.matcher(s.trim()).find()),
    START_W_LOWER_CASE_LETTER(s -> Regex.START_W_LOWERCASE_LETTER.matcher(s.trim()).find()),
    START_W_UPPER_CASE_LETTER(s -> Regex.START_W_UPPERCASE_LETTER.matcher(s.trim()).find()),

    IS_90_PERCENT_CAPS(GeneralTextFeature::hasMostlyUppercaseLetters),

    CONTAINS_SQUARE_BRACKETS((s) -> s.contains("[") || s.contains("]")),
    CONTAINS_NON_LETTER((s) -> s.chars()
            .filter(ch -> !(Character.isWhitespace(ch) || Character.isLetter(ch)))
            .limit(1)
            .toString()
            .length() > 0
    ),
    START_W_NON_LETTER((s) -> Regex.START_W_NON_LETTER.matcher(s).find()),

    END_W_SEMICOLON(s -> Strings2.lastCharIs(s.trim(), ';')),
    END_W_COLON(s -> Strings2.lastCharIs(s.trim(), ':')),
    END_W_BV(s -> Regex.END_W_BV.matcher(s).find()),

    START_W_LIST_MARKING(ListMarking::startsWithListMarking),
    START_W_CIRCLE_LIST_MARKING(s -> Strings2.firstNonWhitespaceCharIsAny(s, ListMarking.CLASSIC_ROUND_LIST_MARKINGS) > -1),
    START_W_QUESTIONABLE_LIST_MARKING(s -> Strings2.firstNonWhitespaceCharIsAny(s, ListMarking.QUESTIONABLE_LIST_MARKINGS) > -1),
    START_W_HORIZONTAL_LIST_MARKING(s -> Strings2.firstNonWhitespaceCharIsAny(s, ListMarking.HORIZONTAL_LIST_MARKING) > -1),
    START_W_VERTICAL_LIST_MARKING(s -> Strings2.firstNonWhitespaceCharIsAny(s, ListMarking.VERTICAL_LIST_MARKINGS) > -1),

    LOOKS_LIKE_UPPERCASE_TITLE(s -> IS_90_PERCENT_CAPS.apply(s) && MORE_THAN_5_LETTERS.apply(s)),

    ALL_NUMERIC(s -> Regex.ALL_DIGITS.matcher(s.trim()).matches()),

    END_W_QUOTE(Quote::endsWithQuote),
    END_W_QUOTE_STRICT(Quote::endsWithQuoteStrict),
    END_W_SINGLE_QUOTE(Quote::endsWithSingleQuote),
    END_W_DOUBLE_QUOTE(Quote::endsWithDoubleQuote),
    END_W_DOUBLE_QUOTE_STRICT(Quote::endsWithDoubleQuoteStrict),
    MULTIPLE_QUOTES(Quote::containsMultipleQuotes);

    private final Function<String, Boolean> f;

    GeneralTextFeature(Function<String, Boolean> f) {
        this.f = f;
    }

    private static Function<String, Boolean> lessThanNLetters(int i) {
        return (s) -> {
            int letterCount = 0;
            for (char c : s.toCharArray()) if (Character.isLetter(c)) letterCount++;
            return letterCount < i;
        };
    }

    private static Function<String, Boolean> moreThanNLetters(int i) {
        return (s) -> {
            int letterCount = 0;
            for (char c : s.toCharArray()) if (Character.isLetter(c)) letterCount++;
            return letterCount > i;
        };
    }

    public boolean apply(String s) {
        if (s == null) return false;
        Boolean bool = f.apply(s);
        if (bool == null) throw new NullPointerException();
        return bool;
    }

    public boolean apply(List<TokenTreeLeaf> element, int ix) {
        String textContent = element.get(ix).getTextContent();
        if (textContent == null) return false;
        Boolean bool = f.apply(textContent);
        if (bool == null) throw new NullPointerException();
        return bool;
    }

    /**
     * @param s Input string
     * @return If strings has more than 3 characters and most (+90%) are uppercase
     */
    public static boolean hasMostlyUppercaseLetters(String s) {
        if (s == null) return false;
        int allLetters = 0;
        int uppercase = 0;
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                allLetters++;
                if (Character.isUpperCase(c)) uppercase++;
            }
        }
        return
                (allLetters > 3)
                        && ((allLetters <= 10 && uppercase >= (allLetters - 1))
                        || ((10.0 * uppercase) / (10.0 * allLetters) >= 0.9));
    }

    public static boolean followsLineBreak(List<TokenTreeLeaf> elements, int i) {
        return elements.get(i).getTextContent() != null && i > 0 && elements.get(i - 1) instanceof Newline;
    }

    public static boolean containsRefToWoonplaats(List<TokenTreeLeaf> elements, int i) {
        return elements.get(i).getTextContent() != null && Regex.CONTAINS_REF_TO_WOONPLAATS.matcher(elements.get(i).getTextContent()).find();
    }

    public static boolean containsDate(List<TokenTreeLeaf> tokens, int i) {
        return tokens.get(i).getTextContent() != null && Regex.CONTAINS_DATE.matcher(tokens.get(i).getTextContent()).find();
    }

    public static boolean containsBracketedText(List<TokenTreeLeaf> elements, int i) {
        return elements.get(i).getTextContent() != null && Regex.CONTAINS_BRACKETED_TEST.matcher(elements.get(i).getTextContent()).find();
    }

    public static boolean containsLikelyZaaknr(List<TokenTreeLeaf> elements, int i) {
        return elements.get(i).getTextContent() != null && Regex.CONTAINS_LIKELY_ZAAKNR.matcher(elements.get(i).getTextContent()).find();
    }

    public static boolean containsRefToPersonage(List<TokenTreeLeaf> elements, int i) {
        String textContent = elements.get(i).getTextContent();
        return textContent != null && Regex.CONTAINS_REF_TO_PERSONAGE.matcher(textContent).find();
    }
}
