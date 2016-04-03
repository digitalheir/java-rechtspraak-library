package org.leibnizcenter.rechtspraak.features;

import cc.mallet.types.Token;
import org.leibnizcenter.rechtspraak.features.info.InfoPatterns;
import org.leibnizcenter.rechtspraak.features.quote.BlockQuotePatterns;
import org.leibnizcenter.rechtspraak.features.quote.PreBlockQuotePattrns;
import org.leibnizcenter.rechtspraak.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.manualannotation.DeterministicTagger;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledToken;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.features.Patterns;
import org.leibnizcenter.rechtspraak.util.numbering.ArabicNumbering;
import org.leibnizcenter.rechtspraak.util.numbering.FullNumber;
import org.leibnizcenter.rechtspraak.util.numbering.Numbering;
import org.w3c.dom.Element;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.leibnizcenter.rechtspraak.features.quote.BlockQuotePatterns.END_W_QUOTE;

/**
 * Created by Maarten on 2016-03-21.
 */
public class Features {
    public static final String ENDS_WITH_NON_LETTER = "ENDS_WITH_NON_LETTER";
    public static final String IS_SPACED = "IS_SPACED";
    public static final String AT_LEAST_10_WORDS = "AT_LEAST_10_WORDS";
    public static final String IS_ALL_CAPS = "IS_ALL_CAPS";
    public static final String WITHIN_FIRST_5_BLOCKS = "WITHIN_FIRST_5_BLOCKS";
    public static final String WITHIN_FIRST_10_BLOCKS = "WITHIN_FIRST_10_BLOCKS";
    public static final String OVER_HALF = "OVER_HALF";
    public static final String VERY_PROBABLE_SECTION = "VERY_PROBABLE_SECTION";
    public static final String CLOSE_TO_ADJACENT_NUMBERING = "CLOSE_TO_ADJACENT_NUMBERING";
    public static final String HAS_SUBSECTION_NUMBERING = "HAS_SUBSECTION_NUMBERING";
    public static final String HAS_SECTION_NUMBERING = "HAS_SECTION_NUMBERING";
    public static final String IS_FOOTNOTE = "IS_FOOTNOTE";
    public static final String IS_LIST = "IS_LIST";
    public static final String QUOTE_INTRODUCED_ = "QUOTE_INTRODUCED_";
    public static final String EMPHASIS = "EMPHASIS_";
    public static final String PREV_END_W_QUOTE = "PREV_END_W_QUOTE";
    public static final String NUMBERING_TERMINAL = "NUMBERING_TERMINAL_";
    public static final String PREV_START_W_QUOTE = "PREV_START_W_QUOTE";
    public static final String PREV_END_W_DBL_QUOTE = "PREV_END_W_DBL_QUOTE";
    public static final String PREV_START_W_DBL_QUOTE = "PREV_START_W_DBL_QUOTE";
    public static final String PREV_END_W_COLON = "PREV_END_W_COLON";
    public static final String PREV_END_W_SEMICOLON = "PREV_END_W_SEMICOLON";
    public static final String END_W_SEMICOLON = "END_W_SEMICOLON";
    public static final String END_W_COLON = "END_W_COLON";
    private static final Pattern ALL_NUM = Pattern.compile("^[0-9 ]*[0-9][0-9 ]*$");

    public static void setFeatureValues(LabeledTokenList sequence, int ix, Token t) {
        RechtspraakElement token = sequence.get(ix).getToken();
        RechtspraakElement previousToken = ix > 0 ? sequence.get(ix - 1).getToken() : null;
        List<RechtspraakElement> elements = sequence.stream().map(LabeledToken::getToken).collect(Collectors.toList());

        //
        // Info patterns
        //
        InfoPatterns.InfoPatternsNormalizedContains.setFeatureValues(t, token);
        InfoPatterns.InfoPatternsNormalizedMatches.setFeatureValues(t, token);
        InfoPatterns.InfoPatternsUnormalizedContains.setFeatureValues(t, token);

        //
        // Title patterns
        //
        TitlePatterns.TitlesNormalizedMatchesHighConf.setFeatureValues(t, token);
        TitlePatterns.TitlesNormalizedMatchesLowConf.setFeatureValues(t, token);
        TitlePatterns.TitlesUnnormalizedContains.setFeatureValues(t, token);
        if (isVeryLikelyFirstTitle(token)) t.setFeatureValue("isVeryLikelyFirstTitle", 1.0);
        if (DeterministicTagger.hasEmphasis(token))
            t.setFeatureValue("hasEmphasis", 1.0);
        if (DeterministicTagger.matchesLowConfTitle(token))
            t.setFeatureValue("matchesLowConfTitle", 1.0);
        if (DeterministicTagger.matchesLowConfTitleButHasMarkup(token))
            t.setFeatureValue("matchesLowConfTitleButHasMarkup", 1.0);
        if (DeterministicTagger.matchesHighConfTitle(token))
            t.setFeatureValue("matchesHighConfTitle", 1.0);


        //
        // Block quote patterns
        //

        // Pre
        PreBlockQuotePattrns.Normalized.set.stream()
                .filter(pttrn -> Patterns.matches(pttrn, previousToken))
                .forEach(pttrn -> t.setFeatureValue(QUOTE_INTRODUCED_ + pttrn.name(), 1.0));
        // This token
        if (token.startsWithDoubleQuote) t.setFeatureValue("START_W_LOWERCASE_LETTER", 1.0);
        if (token.endsWithQuote) t.setFeatureValue(END_W_QUOTE.name(), 1.0);
        if (token.endsWithSemiColon) t.setFeatureValue(END_W_SEMICOLON, 1.0);
        if (token.endsWithColon) t.setFeatureValue(END_W_COLON, 1.0);

        // previous token
        if (previousToken != null) {
            if (previousToken.endsWithColon) t.setFeatureValue(PREV_END_W_COLON, 1.0);
            if (previousToken.endsWithSemiColon) t.setFeatureValue(PREV_END_W_SEMICOLON, 1.0);
            if (previousToken.endsWithQuote) t.setFeatureValue(PREV_END_W_QUOTE, 1.0);
            if (previousToken.startsWithQuote) t.setFeatureValue(PREV_START_W_QUOTE, 1.0);
            if (previousToken.endsWithDoubleQuote) t.setFeatureValue(PREV_END_W_DBL_QUOTE, 1.0);
            if (previousToken.startsWithDoubleQuote) t.setFeatureValue(PREV_START_W_DBL_QUOTE, 1.0);
        }

        BlockQuotePatterns.setFeatureValues(t, token);
        if (previousToken != null) for (BlockQuotePatterns p : BlockQuotePatterns.values()) {
            if (Patterns.matches(p, previousToken)) t.setFeatureValue("PREV_" + p.name(), 1.0);
        }

        // General patterns
        //
        // Whether the text contains a name, or something that looks like a name
        //Names.NamePatterns.setFeatureValues(t, token);
        // Whether the text contains a place name, or something that looks like a place name
        //PlaceNamesInNL.setFeatureValues(t, token);

        // Whether there is an emphasis node in this element

        String nodeName = token.getNodeName();
        if (nodeName.equals("listitem")
                || nodeName.endsWith("list")
                || nodeName.equals("link")
                || nodeName.startsWith("footnote")
                || nodeName.endsWith("table")) {
            t.setFeatureValue("TAG_IGNORE", 1.0);
        }

        Element emph = token.getEmphasisSingletonChild();
        if (emph != null) {
            t.setFeatureValue(EMPHASIS, 1.0);
            String role = emph.getAttribute("role");
            if (role != null && role.length() > 0) {
                t.setFeatureValue(EMPHASIS + role, 1.0);
            }

            RechtspraakElement secondPreviousToken = sequence.get(ix).getToken();
            boolean sameEmphasisAsTwoBack = sameEmphasis(secondPreviousToken, emph);
            if (sameEmphasis(previousToken, emph)) {
                t.setFeatureValue("CONTINUES_EMPHASIS", 1.0);
            } else if (sameEmphasisAsTwoBack) {
                t.setFeatureValue("CONTINUES_EMPHASIS_SKIP", 1.0);
            }
            if (sameEmphasisAsTwoBack) {
                t.setFeatureValue("CONTINUES_EMPHASIS_2", 1.0);
            }
        }

        //
        // Numbering:
        //
        if (token.numbering != null) {
            t.setFeatureValue("NUMBERING", 1.0);
            if (token.numbering instanceof FullNumber) {
                t.setFeatureValue("NUMBERING_" + ((FullNumber) token.numbering).canonicalRepresentation(), 1.0);
            }
            t.setFeatureValue("NUMBERING_TYPE_" + token.numbering.getClass().getSimpleName(), 1.0);
            t.setFeatureValue(NUMBERING_TERMINAL + token.numbering.getTerminal(), 1.0);
            if (token.numbering.isFirstNumbering()) t.setFeatureValue("FIRST_NUMBER", 1.0);
            if (token.isCloseToAdjacentNumbering()) t.setFeatureValue(CLOSE_TO_ADJACENT_NUMBERING, 1.0);
        }
        if (Numbering.isSpelledOut(elements, ix)) t.setFeatureValue("IS_SPELLED_OUT", 1.0);
        if (Numbering.partOfWasGetekend(elements, ix)) t.setFeatureValue("PART_OF_WG", 1.0);
        if (Numbering.afterWasGetekend(elements, ix)) t.setFeatureValue("AFTER_WG", 1.0);
        if (Numbering._s(elements, ix))
            t.setFeatureValue("'s", 1.0);
        if (Numbering.hasCharNumberingAndDoesntFollowsPreviousChar(elements, ix, 'f'))
            t.setFeatureValue("LIKELY_ABBR_FLORIJNEN", 1.0);
        if (Numbering.hasCharNumberingAndDoesntFollowsPreviousChar(elements, ix, 'e'))
            t.setFeatureValue("LIKELY_ABBR_EURO", 1.0);// euro
        if (Numbering.hasCharNumberingAndDoesntFollowsPreviousChar(elements, ix, 'u'))
            t.setFeatureValue("LIKELY_WORD_U", 1.0);// u heeft
        if (Numbering.isSpelledOut(elements, ix)) t.setFeatureValue("NUMBER_SPELLED_OUT", 1.0);// 2 (twee)
        if (Numbering.isJustStartOfSentence(elements, ix))
            t.setFeatureValue("LIKELY_JUST_START_OF_SENTENCE", 1.0);// 7 patronen
        if (Numbering.isProbablyName(elements, ix)) t.setFeatureValue("LIKELY_NAME_INITIAL", 1.0);//

        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        if (token.highConfidenceNumberedTitleFoundAndIsNumbered()) t.setFeatureValue(VERY_PROBABLE_SECTION, 1.0);


        if (token.isSpaced) t.setFeatureValue(IS_SPACED, 1.0);
        if (ALL_NUM.matcher(token.normalizedText).matches()) t.setFeatureValue("IS_NUMERIC", 1.0);
        if (token.endsWithNonLetter) t.setFeatureValue(ENDS_WITH_NON_LETTER, 1.0);
        if (token.isAllCaps) t.setFeatureValue(IS_ALL_CAPS, 1.0);
        if (nodeName.equals("footnote")) t.setFeatureValue(IS_FOOTNOTE, 1.0);
        if (nodeName.endsWith("list")) t.setFeatureValue(IS_LIST, 1.0);
        if (token.wordCount < 10) t.setFeatureValue("LESS_THAN_10_WORDS", 1.0);
        if (token.wordCount < 5) t.setFeatureValue("LESS_THAN_5_WORDS", 1.0);
        if (token.wordCount < 3) t.setFeatureValue("LESS_THAN_3_WORDS", 1.0);
        if (token.wordCount == 1) t.setFeatureValue("ONE_WORD", 1.0);
        if (token.followsLineBreak) t.setFeatureValue("FOLLOWS_LINEBREAK", 1.0);
        if (token.wordCount >= 10) t.setFeatureValue(AT_LEAST_10_WORDS, 1.0);
        //TODO something like SEEMS_TO_BE_PART_OF_BOLD_TITLES

        if (token.getTextContent().length() > 0) {
            char firstChar = token.getTextContent().charAt(0);
            if (!Character.isLetterOrDigit(firstChar))
                t.setFeatureValue("START_W_" + firstChar, 1.0);
        }
        if (ix < 5) t.setFeatureValue(WITHIN_FIRST_5_BLOCKS, 1.0);
        if (ix >= 5 && ix < 10) t.setFeatureValue(WITHIN_FIRST_10_BLOCKS, 1.0);
        //if (indexInSequence < sequence.size() / 4) t.setFeatureValue("FIRST_QUARTILE", 1.0);
        if (ix >= sequence.size() / 2) t.setFeatureValue(OVER_HALF, 1.0);
//        System.out.println("2etc");
    }

    private static boolean sameEmphasis(RechtspraakElement previousToken, Element emph) {
        return previousToken != null
                && previousToken.getEmphasisSingletonChild() != null
                && emph.getAttribute("role").equals(
                previousToken.getEmphasisSingletonChild().getAttribute("role")
        );
    }

    /**
     * Whether the element starts with 1 or Procesverloop
     *
     * @param rechtspraakElement
     * @return Whether this element is very likely to be the first title in the sequence
     */
    public static boolean isVeryLikelyFirstTitle(RechtspraakElement rechtspraakElement) {
        return (rechtspraakElement.numbering != null && rechtspraakElement.numbering instanceof ArabicNumbering
                && rechtspraakElement.numbering.mainNum() == 1)
                || Patterns.matches(TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS, rechtspraakElement);
    }
}
