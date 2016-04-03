package org.leibnizcenter.rechtspraak.features.elementpatterns;

import cc.mallet.types.Token;
import org.leibnizcenter.rechtspraak.features.Features;
import org.leibnizcenter.rechtspraak.features.elementpatterns.interfaces.ElementFeatureFunction;
import org.leibnizcenter.rechtspraak.features.elementpatterns.interfaces.NamedElementFeatureFunction;
import org.leibnizcenter.rechtspraak.features.textpatterns.GeneralTextPattern;
import org.leibnizcenter.rechtspraak.features.textpatterns.TitlePatterns;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;
import org.leibnizcenter.rechtspraak.tokens.quote.Quote;
import org.leibnizcenter.rechtspraak.util.Regex;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by maarten on 31-3-16.
 */
public enum ElementFeature implements NamedElementFeatureFunction {
    HAS_1_WORD((tokens, ix) -> tokens.get(ix).words.length == 1),
    HAS_2_WORDS((tokens, ix) -> tokens.get(ix).words.length == 2),
    HAS_3_WORDS((tokens, ix) -> tokens.get(ix).words.length == 3),
    HAS_4_WORDS((tokens, ix) -> tokens.get(ix).words.length == 4),
    HAS_5_WORDS((tokens, ix) -> tokens.get(ix).words.length == 5),
    HAS_BETWEEN_5_AND_10_WORDS((tokens, ix) -> tokens.get(ix).words.length > 5 && tokens.get(ix).words.length <= 10),
    HAS_MORE_THAN_10_WORDS((tokens, ix) -> tokens.get(ix).words.length > 10),

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
    CONTAINS_REF_TO_PERSON(GeneralTextPattern::containsRefToPersonage),
    CONTAINS_REF_TO_WOONPLAATS(GeneralTextPattern::containsRefToWoonplaats),
    CONTAINS_DATE(GeneralTextPattern::containsDate),
    CONTAINS_BRACKETED_TEXT(GeneralTextPattern::containsBracketedText),
    CONTAINS_LIKELY_ZAAKNR(GeneralTextPattern::containsLikelyZaaknr),


    /**
     * council of the state
     */
    MATCHES_RAAD_VAN_STATE(ElementFeatureFunction.matches(Pattern.compile(
            "^(de|het)?\\s{0,3}raad\\s{0,3}van\\s{0,3}state",
            Pattern.CASE_INSENSITIVE)
    )
    ),

    MATCHES_DE_RECHTBANK(ElementFeatureFunction.matches(Regex.DE_RECHTBANK)),
    MATCHES_EN_VS_CONTRA(ElementFeatureFunction.matches(Regex.EN_VS_CONTRA)),
    MATCHES_VENNOOTSCHAP(ElementFeatureFunction.matches(Regex.VENNOOTSCHAP)),
    MATCHES_INZAKE(ElementFeatureFunction.matches(Regex.INZAKE)),

    //
    // We see these often in quotes
    //
    START_W_LID(ElementFeatureFunction.find(Constants.START_W_LID)),
    START_W_ARTIKEL(ElementFeatureFunction.find(Constants.START_W_ARTIKEL)),
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

//    WITHIN_FIRST_5_BLOCKS((tokens, ix) -> ix < 5),
//    WITHIN_FIRST_10_BLOCKS((tokens, ix) -> ix < 10),
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
     * @param rechtspraakElement
     * @return Whether this element is very likely to be the first title in the sequence
     */
    isVeryLikelyFirstTitle((tokens, ix) -> {
        TokenTreeLeaf prevToken = ix > 0 ? tokens.get(ix - 1) : null;
        return (Numbering.isArabic(prevToken)
                && ((Numbering) prevToken).getNumbering().isFirstNumbering())
                || TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS.apply(tokens, ix);
    }),

    PREV_END_W_QUOTE((ElementFeatureFunction)
            (tokens, ix) -> ix > 0 && Quote.endsWithQuote(tokens.get(ix - 1).getTextContent())),
    PREV_END_W_DBL_QUOTE((ElementFeatureFunction)
            (tokens, ix) -> ix > 0 && Quote.endsWithDoubleQuote(tokens.get(ix - 1).getTextContent())),
    PREV_END_W_COLON((ElementFeatureFunction)
            (tokens, ix) -> ix > 0 && GeneralTextPattern.END_W_COLON.apply(tokens.get(ix - 1).getTextContent())),
    PREV_END_W_SEMICOLON((ElementFeatureFunction)
            (tokens, ix) -> ix > 0 && GeneralTextPattern.END_W_SEMICOLON.apply(tokens.get(ix - 1).getTextContent()));


    /**
     * Whether this element's only substantial text is wrapped in an <code>emphasis</code> element
     *
     * @param tokens
     * @param i
     * @return
     */
    public static boolean hasEmphasis(List<TokenTreeLeaf> tokens, int i) {
        return getEmphasis(tokens, i) != null;
    }

    public static Element getEmphasis(List<TokenTreeLeaf> tokens, int i) {
        NodeList children = tokens.get(i).getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            Node child = children.item(j);
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    if (!"emphasis".equals(((Element) child).getTagName())) return (Element) child;
                    break;
                case Node.TEXT_NODE:
                    if (Xml.hasSubstantialText((Text) child)) return null;
            }
        }
        return null;
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
        for (int i = ix - 1; ix >= 0; i--) {
            TokenTreeLeaf earlier = tokens.get(i);
            if (earlier instanceof Numbering
                    && NumberingNumber.isSuccedentOf(current.getNumbering(), ((Numbering) earlier).getNumbering()))
                return true;
        }
        return false;
    }

    private static boolean hasAdjacentNumberingAfter(List<TokenTreeLeaf> tokens, int ix) {
        Numbering current = (Numbering) tokens.get(ix);
        for (int i = ix + 1; ix < tokens.size(); i++) {
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

    public static void setFeatures(Token t, List<TokenTreeLeaf> tokens, int ix) {
        TokenTreeLeaf token = tokens.get(ix);
        t.setFeatureValue("CLASS_" + token.getClass(), 1.0);
        Element emphasis = getEmphasis(tokens, ix);
        if (emphasis != null) {
            String role = emphasis.getAttribute("role");
            t.setFeatureValue("EMPH_" + role, 1.0);
            if (role != null) {
                String[] roles = Regex.CONSECUTIVE_WHITESPACE.split(role);
                for (String r : roles) t.setFeatureValue("EMPH_" + r, 1.0);
            }
//        if (sameEmphasis(previousToken, emph)) {
//            t.setFeatureValue("CONTINUES_EMPHASIS", 1.0);
//        } else if (sameEmphasisAsTwoBack) {
//            t.setFeatureValue("CONTINUES_EMPHASIS_SKIP", 1.0);
//        }
//        if (sameEmphasisAsTwoBack) {
//            t.setFeatureValue("CONTINUES_EMPHASIS_2", 1.0);
//        }
        }

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
