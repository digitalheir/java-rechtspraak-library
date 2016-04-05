package org.leibnizcenter.rechtspraak.tokens.quote;

import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.features.textpatterns.GeneralTextFeature;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.util.Strings2;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by maarten on 31-3-16.
 */
public class Quote extends RechtspraakElement {
    public final static Set<Character> CHARS_DOUBLE_QUOTE = Sets.newHashSet(
            '‟', '”', '“', '"', '«', '»'
    );
    public final static Set<Character> CHARS_SINGLE_QUOTE = Sets.newHashSet(
            '\'', '⸃', '`', '⸊', '⸅', '⸍', '´', '›', '‛', '’', '‘'
    );
    public final static Set<Character> CHARS_QUOTE = Sets.union(CHARS_SINGLE_QUOTE, CHARS_DOUBLE_QUOTE);

    private static final String ANY_QUOTE = "["
            + Strings2.appendChars(CHARS_DOUBLE_QUOTE)
            + Strings2.appendChars(CHARS_SINGLE_QUOTE)
            + "]";
    private static final String DOUBLE_QUOTE = "["
            + Strings2.appendChars(CHARS_SINGLE_QUOTE)
            + "]";
    private static final String SINGLE_QUOTE = "["
            + Strings2.appendChars(CHARS_DOUBLE_QUOTE)
            + "]";
    private static final Pattern END_W_DOUBLE_QUOTE_STRICT = Pattern.compile(DOUBLE_QUOTE + "$");
    private static final Pattern END_W_DOUBLE_QUOTE = Pattern.compile(DOUBLE_QUOTE + "[^\\p{L}]{0,5}$");
    private static final Pattern END_W_SINGLE_QUOTE = Pattern.compile(SINGLE_QUOTE + "[^\\p{L}]{0,5}$");
    private static final Pattern END_W_ANY_QUOTE_STRICT = Pattern.compile(ANY_QUOTE + "$");
    private static final Pattern END_W_ANY_QUOTE = Pattern.compile(ANY_QUOTE + "[^\\p{L}]{0,5}$");
    public static final Pattern START_WITH_QUOTE = Pattern.compile("^\\s+" + ANY_QUOTE);


    public Quote(Element e) {
        super(e);
    }

    public static boolean endsWithDoubleQuote(String s) {
        return END_W_DOUBLE_QUOTE.matcher(s.trim()).find();
    }

    public static boolean endsWithDoubleQuoteStrict(String s) {
        return END_W_DOUBLE_QUOTE_STRICT.matcher(s.trim()).find();
    }

    public static boolean startsWithQuote(String s) {
        return Strings2.firstNonWhitespaceCharIsAny(s.trim(), CHARS_QUOTE) > -1;
    }

    public static boolean endsWithSingleQuote(String s) {
        return END_W_SINGLE_QUOTE.matcher(s.trim()).find();
    }

    public static boolean endsWithQuoteStrict(String s) {
        return END_W_ANY_QUOTE_STRICT.matcher(s.trim()).find();
    }

    public static boolean endsWithQuote(String s) {
        return END_W_ANY_QUOTE.matcher(s.trim()).find();
    }

    public static boolean quoteIntroduced(List<TokenTreeLeaf> tokens, int ix) {
        return ix > 0
                && tokens.get(ix) instanceof Quote
                && GeneralTextFeature.END_W_COLON.apply(tokens.get(ix).getTextContent());
    }

    public static int startsWithQuoteAtChar(String s) {
        return Strings2.firstNonWhitespaceCharIsAny(s, CHARS_QUOTE);
    }

    public static boolean containsMultipleQuotes(String s) {
        return Strings2.findChars(s, CHARS_QUOTE, 2).size() >= 2;
    }
}
