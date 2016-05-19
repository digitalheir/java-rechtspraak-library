package org.leibnizcenter.rechtspraak.tagging.crf.features.elementpatterns;

import cc.mallet.types.Token;
import com.google.common.base.Strings;
import org.leibnizcenter.rechtspraak.tagging.DeterministicTagger;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tagging.crf.features.Features;
import org.leibnizcenter.rechtspraak.tagging.crf.features.elementpatterns.interfaces.ElementFeatureFunction;
import org.leibnizcenter.rechtspraak.tagging.crf.features.elementpatterns.interfaces.NamedElementFeatureFunction;
import org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns.GeneralTextFeature;
import org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns.TitlePatterns;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.numbering.ListMarking;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.AlphabeticNumbering;
import org.leibnizcenter.rechtspraak.tokens.text.IgnoreElement;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TextElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;
import org.leibnizcenter.rechtspraak.tokens.quote.Quote;
import org.leibnizcenter.util.Regex;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by maarten on 31-3-16.
 */
public enum ElementFeature implements NamedElementFeatureFunction {
    HAS_1_WORD((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).words.length == 1),
    HAS_2_WORDS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).words.length == 2),
    HAS_3_WORDS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).words.length == 3),
    HAS_4_WORDS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).words.length == 4),
    HAS_5_WORDS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).words.length == 5),
    HAS_BETWEEN_5_AND_10_WORDS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).words.length > 5 && tokens.get(ix).words.length <= 10),
    HAS_MORE_THAN_10_WORDS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).words.length > 10),

    PRECEDED_BY_D_NUMBERING_gronden_van_het_hoger_beroep((tokens, ix) -> ix > 0
            && tokens.get(ix).getNormalizedText().endsWith("gronden van het hoger beroep")
            && Numbering.isAlphabetic(tokens.get(ix - 1))
            && ((AlphabeticNumbering) ((Numbering) tokens.get(ix - 1)).getNumbering()).getCharacter() == 'd'),

    TEXT_PRECEDED_BY_NUMBERING((tokens, ix) -> ix > 0
            && tokens.get(ix) instanceof TextElement && tokens.get(ix - 1) instanceof Numbering),

    HAS_1_WORD_BETWEEN_BRACKETS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).wordsBeforeOpeningBracket.length == 1),
    HAS_2_WORDS_BETWEEN_BRACKETS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).wordsBeforeOpeningBracket.length == 2),
    HAS_3_WORDS_BETWEEN_BRACKETS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).wordsBeforeOpeningBracket.length == 3),
    HAS_4_WORDS_BETWEEN_BRACKETS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).wordsBeforeOpeningBracket.length == 4),
    HAS_5_WORDS_BETWEEN_BRACKETS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).wordsBeforeOpeningBracket.length == 5),
    HAS_BETWEEN_5_AND_10_WORDS_BETWEEN_BRACKETS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).wordsBeforeOpeningBracket.length > 5 && tokens.get(ix).wordsBeforeOpeningBracket.length <= 10),
    HAS_MORE_THAN_10_WORDS_BETWEEN_BRACKETS((tokens, ix) -> tokens.get(ix) instanceof TextElement && tokens.get(ix).wordsBeforeOpeningBracket.length > 10),

    FOLLOWED_BY_TEXT_W_MORE_THAN_5_LETTERS((tokens, ix) -> tokens.size() > ix + 1 && GeneralTextFeature.MORE_THAN_5_LETTERS.apply(tokens, ix + 1)),
    IMMEDIATELY_FOLLOWED_BY_INLINE_TEXT((tokens, ix) -> tokens.size() > ix + 1 && GeneralTextFeature.START_W_LOWER_CASE_LETTER.apply(tokens, ix + 1)),

    FOLLOWED_OR_PRECEDED_BY_INZAKE((tokens, ix) -> followedBy(tokens, ix, Regex.INZAKE) || precededBy(tokens, ix, Regex.INZAKE)),
    FOLLOWED_OR_PRECEDED_BY_ENTEGENCONTRA((tokens, ix) -> followedBy(tokens, ix, Regex.EN_VS_CONTRA) || precededBy(tokens, ix, Regex.EN_VS_CONTRA)),

    NOT_SURROUNDED_BY_TEXT_BLOCKS((tokens, ix) ->
            (0 == ix || !TextElement.is(tokens, ix - 1))
                    && (tokens.size() == ix + 1 || !TextElement.is(tokens, ix + 1))
    ),

    //
    // Often seen in header text (*.info)
    //
    /**
     * starts with high/central council, e.g. 'hoge raad van beroep'
     */
    STARTS_WITH_RAAD(ElementFeatureFunction.find(Regex.START_W_RAAD)),

    /**
     * starts with rechtbank/gerechthof with location
     */
    START_WITH_COURT(ElementFeatureFunction.find(Regex.START_W_COURT)),

    /**
     * inzake / in de zaak
     */
    START_W_INZAKE(ElementFeatureFunction.find(Regex.INZAKE)),
    START_W_INDEZAAK(ElementFeatureFunction.find(Regex.INDEZAAK)),
    START_W_AFDELING(ElementFeatureFunction.find(Regex.DEAFDELING)),
    START_W_SECTOR(ElementFeatureFunction.find(Regex.START_W_SECTOR)),
    START_W_HIERNA(ElementFeatureFunction.find(Regex.START_W_HIERNA)),
    START_W_LOCATIE(ElementFeatureFunction.find(Regex.START_W_LOCATIE)),
    START_W_ZAAK_ROL_NR(ElementFeatureFunction.find(Regex.START_W_ZAAK_ROL_NR)),
    START_W_ZAAKNR(ElementFeatureFunction.find(Regex.START_W_ZAAKNR)),
    START_W_WONEND_GEVESTIGD(ElementFeatureFunction.find(Regex.START_W_GEVESTIGD)),
    START_W_DATUM(ElementFeatureFunction.find(Regex.START_W_DATUM)),
    END_W_KAMER(ElementFeatureFunction.find(Regex.END_W_KAMER)),
    END_W_ROLE(ElementFeatureFunction.find(Regex.END_W_ROLE)),
    CONTAINS_REF_TO_PERSON(GeneralTextFeature::containsRefToPersonage),
    CONTAINS_REF_TO_WOONPLAATS(GeneralTextFeature::containsRefToWoonplaats),
    CONTAINS_DATE(GeneralTextFeature::containsDate),
    CONTAINS_BRACKETED_TEXT(GeneralTextFeature::containsBracketedText),
    CONTAINS_LIKELY_ZAAKNR(GeneralTextFeature::containsLikelyZaaknr),


    /**
     * council of the state
     */
    MATCHES_RAAD_VAN_STATE(ElementFeatureFunction.matches(Pattern.compile(
            "^(de|het)?\\s{0,3}raad\\s{0,3}van\\s{0,3}state",
            Pattern.CASE_INSENSITIVE)
    )
    ),

    TENUITVOERLEGGING(((tokens, ix) -> tokens.get(ix).getNormalizedText().startsWith("tenuitvoerlegging")
            && tokens.get(ix).words.length < 15
            && tokens.get(ix).wordsBeforeOpeningBracket.length < 5
    )),

    MATCHES_DE_RECHTBANK(ElementFeatureFunction.matchesNormalized(Regex.DE_RECHTBANK)),
    MATCHES_EN_VS_CONTRA(ElementFeatureFunction.matchesNormalized(Regex.EN_VS_CONTRA)),
    MATCHES_VENNOOTSCHAP(ElementFeatureFunction.matchesNormalized(Regex.VENNOOTSCHAP)),
    MATCHES_INZAKE(ElementFeatureFunction.matchesNormalized(Regex.INZAKE)),

    //
    // We see these often in quotes
    //
    START_W_LID(ElementFeatureFunction.find(Constants.START_W_LID)),
    CONTAINS_DOTDOTDOT(ElementFeatureFunction.find(Constants.DOTDOTDOT)),
    CONTAINS_IK(ElementFeatureFunction.find(Constants.IK)),
    CONTAINS_U(ElementFeatureFunction.find(Constants.UW)),
    CONTAINS_JIJ(ElementFeatureFunction.find(Constants.JIJ)),
    CONTAINS_MIJN(ElementFeatureFunction.find(Constants.MIJN)),

    //
    // Often occur before quotes
    //
    HET_VOLGENDE_BERICHT(ElementFeatureFunction.find(Regex.HET_VOLGENDE_BERICHT)),
    DE_HET_VOLGENDE(ElementFeatureFunction.find(Regex.DE_HET_VOLGENDE)),
    INHOUDT(ElementFeatureFunction.find(Regex.INHOUDT)),
    GEGEVENS(ElementFeatureFunction.find(Regex.GEGEVENS)),
    VERZONDEN(ElementFeatureFunction.find(Regex.VERZONDEN)),
    VOLGENDE(ElementFeatureFunction.find(Regex.VOLGENDE)),
    ZAKELIJKWEERGEGEVEN(ElementFeatureFunction.find(Regex.ZAKELIJKWEERGEGEVEN)),
    LUIDT_ALS_VOLGT(ElementFeatureFunction.find(Regex.LUIDT_ALS_VOLGT)),
    LUIDT(ElementFeatureFunction.find(Regex.LUIDT)),
    IS_BEPAALD(ElementFeatureFunction.find(Regex.IS_BEPAALD)),
    HET_VOLGENDE_BEPAALD(ElementFeatureFunction.find(Regex.HET_VOLGENDE_BEPAALD)),
    MEEGEDEELD(ElementFeatureFunction.find(Regex.MEEGEDEELD)),
    HET_VOLGENDE_MEEGEDEELD(ElementFeatureFunction.find(Regex.HET_VOLGENDE_MEEGEDEELD)),
    HET_VOLGENDE_GESCHREVEN(ElementFeatureFunction.find(Regex.HET_VOLGENDE_GESCHREVEN)),
    STAAT_VERMELD(ElementFeatureFunction.find(Regex.STAAT_VERMELD)),
    ALS_VOLGT_BEANTWOORD(ElementFeatureFunction.find(Regex.ALS_VOLGT_BEANTWOORD)),
    TE_LEZEN(ElementFeatureFunction.find(Regex.TE_LEZEN)),
    BEANTWOORD_ALS_VOLGT(ElementFeatureFunction.find(Regex.BEANTWOORD_ALS_VOLGT)),
    VERMELD(ElementFeatureFunction.find(Regex.VERMELD)),
    STAAT(ElementFeatureFunction.find(Regex.STAAT)),
    DAARIN_STAAT(ElementFeatureFunction.find(Regex.DAARIN_STAAT)),
    OVERWOGEN(ElementFeatureFunction.find(Regex.OVERWOGEN)),
    OVERWOOG(ElementFeatureFunction.find(Regex.OVERWOOG)),
    GEWIJZIGD(ElementFeatureFunction.find(Regex.GEWIJZIGD)),
    HOUDT_IN(ElementFeatureFunction.find(Regex.HOUDT_IN)),
    CONTRACT(ElementFeatureFunction.find(Regex.CONTRACT)),
    VOORTS(ElementFeatureFunction.find(Regex.VOORTS)),
    OVEREENKOMST(ElementFeatureFunction.find(Regex.OVEREENKOMST)),
    IS_BEOORDEELD(ElementFeatureFunction.find(Regex.IS_BEOORDEELD)),

    IGNORE_NODE((ElementFeature::ignoreNode)),

    TEXT_WITHIN_FIRST_3_BLOCKS((tokens, ix) -> tokens.get(ix) instanceof TextElement && ix < 3),
    TEXT_WITHIN_FIRST_5_BLOCKS((tokens, ix) -> tokens.get(ix) instanceof TextElement && ix < 5),
    TEXT_WITHIN_FIRST_10_BLOCKS((tokens, ix) -> tokens.get(ix) instanceof TextElement && ix < 10),
    TEXT_WITHIN_FIRST_15_BLOCKS((tokens, ix) -> tokens.get(ix) instanceof TextElement && ix < 15),
    TEXT_WITHIN_FIRST_20_BLOCKS((tokens, ix) -> tokens.get(ix) instanceof TextElement && ix < 20),

//    OVER_HALF((tokens, ix) -> (ix > (tokens.size() / 2))),

    // VERY_PROBABLE_SECTION(ElementFeature::), //TODO
    /**
     * 'Close' has a window of 2 either way
     */
    CLOSE_TO_ADJACENT_NUMBERING(ElementFeature::hasAdjacentNumbering),

    IS_FOOTNOTE((tokens, ix) -> "footnote".equals(tokens.get(ix).getNodeName())),
    IS_LIST((tokens, ix) -> "list".equals(tokens.get(ix).getNodeName())),
    QUOTE_INTRODUCED_(Quote::quoteIntroduced),
    EMPHASIS(ElementFeature::hasEmphasis),

    /**
     * Whether the element starts with 1 or Procesverloop
     *
     * @return Whether this element is very likely to be the first title in the sequence
     */
    isVeryLikelyFirstTitle((tokens, ix) -> {
        TokenTreeLeaf prevToken = ix > 0 ? tokens.get(ix - 1) : null;
        return (Numbering.isArabic(prevToken)
                && ((Numbering) prevToken).getNumbering().isFirstNumbering())
                || TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS.apply(tokens, ix);
    }),
    UITSPRAAK_WITHIN_10_BLOCKS((tokens, ix) -> ix < 10
            && tokens.get(ix).getNormalizedText().equals("uitspraak")),
    PREV_END_W_QUOTE((ElementFeatureFunction)
            (tokens, ix) -> ix > 0 && Quote.endsWithQuote(tokens.get(ix - 1).getTextContent())),
    PREV_END_W_DBL_QUOTE((ElementFeatureFunction)
            (tokens, ix) -> ix > 0 && Quote.endsWithDoubleQuote(tokens.get(ix - 1).getTextContent())),
    PREV_END_W_COLON((ElementFeatureFunction)
            (tokens, ix) -> ix > 0 && GeneralTextFeature.END_W_COLON.apply(tokens.get(ix - 1).getTextContent())),
    PREV_END_W_SEMICOLON((ElementFeatureFunction)
            (tokens, ix) -> ix > 0 && GeneralTextFeature.END_W_SEMICOLON.apply(tokens.get(ix - 1).getTextContent())),
    matchesHighConfTitle((tokens, ix) -> Features.matchesAny(tokens, ix,
            (ElementFeatureFunction[]) TitlePatterns.TitlesNormalizedMatchesHighConf.values())
    ),

    matchesLowConfTitle((tokens, ix) -> Features.matchesAny(tokens, ix,
            (ElementFeatureFunction[]) TitlePatterns.TitlesNormalizedMatchesLowConf.values())),

    likelyNotTitle((tokens, ix) -> ElementFeature.TEXT_WITHIN_FIRST_15_BLOCKS.apply(tokens, ix)
            || GeneralTextFeature.CONTAINS_NON_LETTER.apply(tokens, ix)
            || GeneralTextFeature.CONTAINS_SQUARE_BRACKETS.apply(tokens, ix)
            || GeneralTextFeature.START_W_NON_LETTER.apply(tokens, ix)
            || ElementFeature.UITSPRAAK_WITHIN_10_BLOCKS.apply(tokens, ix)
            || ElementFeature.IMMEDIATELY_FOLLOWED_BY_INLINE_TEXT.apply(tokens, ix)),
//                || KnownSurnamesNl.containsAnyName.apply(tokens, ix)

    matchesLowConfTitleButLooksLikeATitleSomewhat((tokens, ix) ->
            !likelyNotTitle.apply(tokens, ix) &&
                    (
                            GeneralTextFeature.LOOKS_LIKE_UPPERCASE_TITLE.apply(tokens, ix)
                                    || ElementFeature.hasEmphasis(tokens, ix)
                                    || GeneralTextFeature.ENDS_WITH_LETTER_OR_NUMBER_OR_BRACKET.apply(tokens, ix)
                    )
                    && (
                    matchesLowConfTitle.apply(tokens, ix)
                            || (
                            ElementFeature.NOT_SURROUNDED_BY_TEXT_BLOCKS.apply(tokens, ix)
                                    && GeneralTextFeature.MORE_THAN_5_LETTERS.apply(tokens, ix)
                                    && tokens.get(ix).wordsBeforeOpeningBracket.length <= 5
                                    && tokens.get(ix).words.length <= 15
                    )

            )),
    isProbablyNumbering((tokens, ix) -> Numbering.is(tokens.get(ix))
            && ((Numbering) tokens.get(ix)).isPlausibleNumbering
            && !NumberingFeature.IS_TAINTED_BY_IMPLAUSIBLE_SIBLING_NR.apply(tokens, ix)),
    dontTryToLabel((tokens, ix) ->
            IgnoreElement.dontTryToLabel(tokens.get(ix).getNodeName())),
    probablySectionTitle((tokens, ix) ->
            !(
                    ElementFeature.MATCHES_EN_VS_CONTRA.apply(tokens, ix)
                            || ElementFeature.MATCHES_INZAKE.apply(tokens, ix)
                            || ElementFeature.FOLLOWED_OR_PRECEDED_BY_ENTEGENCONTRA.apply(tokens, ix)
                            || ElementFeature.FOLLOWED_OR_PRECEDED_BY_INZAKE.apply(tokens, ix)
                            || ElementFeature.PRECEDED_BY_D_NUMBERING_gronden_van_het_hoger_beroep.apply(tokens, ix)
                            || GeneralTextFeature.END_W_BV.apply(tokens, ix)
            )
                    && (matchesHighConfTitle.apply(tokens, ix)
                    || matchesLowConfTitleButLooksLikeATitleSomewhat.apply(tokens, ix)
            )),
    probablyTextBlock((tokens, ix) -> (!Strings.isNullOrEmpty(tokens.get(ix).getTextContent())) && DeterministicTagger.firstPass(tokens, ix).equals(Label.TEXT_BLOCK)),
    probablyNotTextBlock((tokens, ix) -> !DeterministicTagger.firstPass(tokens, ix).equals(Label.TEXT_BLOCK)),

    probablyNotNewline((tokens, ix) -> !(tokens.get(ix) instanceof Newline)),
    probablyNewline((tokens, ix) -> (tokens.get(ix) instanceof Newline)),

    emptyOrNoTextContent((tokens, ix) -> Strings.isNullOrEmpty(tokens.get(ix).getTextContent())),

    possibleNr((tokens, ix) -> (tokens.get(ix) instanceof Numbering)),
    probablyNotNr((tokens, ix) -> !(tokens.get(ix) instanceof Numbering));


    private static boolean followedBy(List<TokenTreeLeaf> tokens, int ix, Pattern p) {
        for (int i = ix + 1; i < tokens.size(); i++) {
            String txt = tokens.get(i).getNormalizedText();
            if (txt.length() > 0 && !Regex.CONSECUTIVE_WHITESPACE.matcher(txt).matches()) {
                return p.matcher(txt).matches();
            }
        }
        return false;
    }

    private static boolean precededBy(List<TokenTreeLeaf> tokens, int ix, Pattern p) {
        for (int i = (ix - 1); i >= 0; i--) {
            String txt = tokens.get(i).getNormalizedText();
            if (txt.length() > 0 && !Regex.CONSECUTIVE_WHITESPACE.matcher(txt).matches()) {
                return p.matcher(txt).matches();
            }
        }
        return false;
    }

    /**
     * Whether this element's only substantial text is wrapped in an <code>emphasis</code> element
     *
     * @param tokens
     * @param i
     * @return
     */
    public static boolean hasEmphasis(List<TokenTreeLeaf> tokens, int i) {
        return tokens.get(i).getEmphasis() != null;
    }

    private static boolean ignoreNode(List<TokenTreeLeaf> tokens, int ix) {
        String nodeName = tokens.get(ix).getNodeName();
        return (nodeName.equals("listitem")
                || nodeName.endsWith("list")
                || nodeName.equals("link")
                || nodeName.startsWith("footnote")
                || nodeName.endsWith("table"));
    }

    private static boolean hasAdjacentNumbering(List<TokenTreeLeaf> tokens, int ix) {
        if (tokens.get(ix) instanceof Numbering) {
            if (hasAdjacentNumberingBefore(tokens, ix)) return true;
            if (hasAdjacentNumberingAfter(tokens, ix)) return true;
        }
        return false;
    }

    private static boolean hasAdjacentNumberingBefore(List<TokenTreeLeaf> tokens, int ix) {
        Numbering current = (Numbering) tokens.get(ix);
        for (int i = ix - 1; i >= 0; i--) {
            TokenTreeLeaf earlier = tokens.get(i);
            if (earlier instanceof Numbering
                    && NumberingNumber.isSuccedentOf(current.getNumbering(), ((Numbering) earlier).getNumbering()))
                return true;
        }
        return false;
    }

    private static boolean hasAdjacentNumberingAfter(List<TokenTreeLeaf> tokens, int ix) {
        Numbering current = (Numbering) tokens.get(ix);
        for (int i = ix + 1; i < tokens.size(); i++) {
            TokenTreeLeaf later = tokens.get(i);
            if (later instanceof Numbering
                    && NumberingNumber.isSuccedentOf(((Numbering) later).getNumbering(), current.getNumbering()))
                return true;
        }
        return false;
    }


    public final ElementFeatureFunction function;

    ElementFeature(ElementFeatureFunction function) {
        this.function = function;
    }

    @Override
    public boolean apply(List<TokenTreeLeaf> tokens, int ix) {
        return function.apply(tokens, ix);
    }

    public static final Pattern NON_A_Z_a_z_0_9 = Pattern.compile("[^0-9A-Za-z]");

    public static void setFeatures(Token t, List<TokenTreeLeaf> tokens, int ix) {
        TokenTreeLeaf token = tokens.get(ix);

        if (token instanceof Newline) {
            t.setFeatureValue("TOKEN_TYPE_NEWLINE", 1.0);
        } else if (token instanceof Quote) {
            t.setFeatureValue("TOKEN_TYPE_QUOTE", 1.0);
        } else if (token instanceof Numbering) {
            t.setFeatureValue("TOKEN_TYPE_NUMBERING", 1.0);
        } else if (token instanceof ListMarking) {
            t.setFeatureValue("TOKEN_TYPE_LISTMARKING", 1.0);
        } else if (token instanceof RechtspraakElement) {
            t.setFeatureValue("TOKEN_TYPE_RECHTSPRAAKELEMENT", 1.0);
        }


        Set<String> emphasis = tokens.get(ix).getEmphasis();
        if (emphasis != null) {
            if (emphasis.size() > 0) t.setFeatureValue("EMPHASIS", 1.0);

            for (String em : emphasis) {
                t.setFeatureValue(NON_A_Z_a_z_0_9.matcher("EMPH_" + em).replaceFirst("_"), 1.0);
            }
        }
//        if (sameEmphasis(previousToken, emph)) {
//            t.setFeatureValue("CONTINUES_EMPHASIS", 1.0);
//        } else if (sameEmphasisAsTwoBack) {
//            t.setFeatureValue("CONTINUES_EMPHASIS_SKIP", 1.0);
//        }
//        if (sameEmphasisAsTwoBack) {
//            t.setFeatureValue("CONTINUES_EMPHASIS_2", 1.0);
//        }

        // Run through enum
        Features.setFeatures(t, tokens, ix,
                (NamedElementFeatureFunction[]) ElementFeature.values());
    }

    private static class Constants {
        public static final Pattern START_W_LID = Pattern.compile("^lid", Pattern.CASE_INSENSITIVE);
        public static final Pattern START_W_ARTIKEL = Pattern.compile("^artikel", Pattern.CASE_INSENSITIVE);
        public static final Pattern DOTDOTDOT = Pattern.compile("\\((?:\\.\\.\\.|…)\\)", Pattern.CASE_INSENSITIVE);
        public static final Pattern IK = Pattern.compile("\\bik\\b", Pattern.CASE_INSENSITIVE);
        public static final Pattern UW = Pattern.compile("\\buw?\\b", Pattern.CASE_INSENSITIVE);
        public static final Pattern JIJ = Pattern.compile("\\bj(?:e|ij|ouw?)\\b", Pattern.CASE_INSENSITIVE);
        public static final Pattern MIJN = Pattern.compile("\\bmijn\\b", Pattern.CASE_INSENSITIVE);
    }
}
