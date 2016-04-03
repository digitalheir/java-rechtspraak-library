package org.leibnizcenter.rechtspraak.manualannotation;

import org.leibnizcenter.rechtspraak.features.Features;
import org.leibnizcenter.rechtspraak.features.elementpatterns.ElementFeature;
import org.leibnizcenter.rechtspraak.features.elementpatterns.NumberingFeature;
import org.leibnizcenter.rechtspraak.features.elementpatterns.interfaces.ElementFeatureFunction;
import org.leibnizcenter.rechtspraak.features.textpatterns.GeneralTextPattern;
import org.leibnizcenter.rechtspraak.features.textpatterns.TitlePatterns;
import org.leibnizcenter.rechtspraak.tokens.Label;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.text.IgnoreElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maarten on 29-3-16.
 */
public class DeterministicTagger {
    // TODO make feature functions

    public static List<Label> tag(String ecli, Document doc, List<TokenTreeLeaf> untagged) {
        List<Label> tagged = new ArrayList<>(untagged.size());
        int firstTitle = -1;
        for (int i = 0; i < untagged.size(); i++) {
            TokenTreeLeaf token = untagged.get(i);
            Label tag = firstPass(untagged, i, tagged);

            // Set first title if this is the first encountered title
            if (firstTitle < 0 && Label.SECTION_TITLE.equals(tag)) firstTitle = setToPrevIfPrecededByNumber(tagged, i);

            //////////////////////////////////////////////////////

            tagged.add(i, tag);
        }
        // Update first title based on full first pass
        //firstTitle = getLikelyFirstTitleIndex(tagged, firstTitle);

        // make everything up to the first title 'INFO'
        //for (int i = 0; i < firstTitle; i++) tagged.get(i).tag = Label.TEXT_BLOCK;//INFO;

//        for (int ix = 0; ix < tagged.size(); ix++) {
//            // This may yet be a title if it is not inline (or follows a NR), does not end with punctuation, and is marked up
//            if (Label.TEXT_BLOCK.equals(tagged.get(ix))
//                    && ElementFeature.hasEmphasis(untagged, ix)
//                    && token.endsWithNonLetter
//                    && ((!token.precedesNonEmptyText && !token.followsNonEmptyText)
//                    || (ix > 0 && Label.isSectionNumber(tagged.get(ix - 1).getTag())))
//                    ) tagged.get(ix).tag = Label.SECTION_TITLE;
//        }
        return tagged;
    }

    private static int getLikelyFirstTitleIndex(List<TokenTreeLeaf> untagged, List<Label> tagged, int firstTitle) {
        boolean proceedingsFound = false;
        for (int i = 0; i < (firstTitle > 0 ? firstTitle : tagged.size()); i++) {
            if (TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS.apply(untagged, i)) {
                proceedingsFound = true;
                firstTitle = setToPrevIfPrecededByNumber(tagged, i);
                break;
            }
        }
        if (!proceedingsFound) {
            for (int i = 0; i < (firstTitle > 0 ? firstTitle : tagged.size()); i++) {
                if (i < tagged.size() - 1
                        && tagged.get(i).equals(Label.SECTION_TITLE)) {
                    firstTitle = setToPrevIfPrecededByNumber(tagged, i);
                    break;
                }
            }
        }
        return firstTitle;
    }

    private static int setToPrevIfPrecededByNumber(List<Label> tagged, int i) {
        if (i > 0 && Label.isNumbering(tagged.get(i - 1))) i = i - 1;
        return i;
    }

    private static Label firstPass(List<TokenTreeLeaf> untagged, int ix, List<Label> tagged) {
        TokenTreeLeaf token = untagged.get(ix);
        if (IgnoreElement.dontTokenize(token.getNodeName())) {
            return Label.TEXT_BLOCK;
        } else if (matchesHighConfTitle(untagged, ix) || matchesLowConfTitleButHasMarkup(untagged, ix)) {
            if (probablyInfoTag(untagged, ix, tagged)) return Label.TEXT_BLOCK;
            else return Label.SECTION_TITLE;
        } else if (token instanceof Numbering) {
            if (!((Numbering) token).isPlausibleNumbering) return Label.TEXT_BLOCK;
            else return Label.getNumberingType(untagged, ix);
        } else {
            return Label.TEXT_BLOCK;
        }
    }

    public static boolean matchesHighConfTitle(List<TokenTreeLeaf> tokens, int ix) {
        return Features.matchesAny(tokens, ix, (ElementFeatureFunction[]) TitlePatterns.TitlesNormalizedMatchesHighConf.values());
    }

    public static boolean matchesLowConfTitle(List<TokenTreeLeaf> tokens, int ix) {
        return Features.matchesAny(tokens, ix, (ElementFeatureFunction[]) TitlePatterns.TitlesNormalizedMatchesLowConf.values());
    }

    public static boolean matchesLowConfTitleButHasMarkup(List<TokenTreeLeaf> tokens, int ix) {
        return (ElementFeature.hasEmphasis(tokens, ix) || GeneralTextPattern.ENDS_WITH_LETTER.apply(tokens, ix))
                && matchesLowConfTitle(tokens, ix);
    }


    private static boolean probablyInfoTag(List<TokenTreeLeaf> untagged, int ix, List<Label> tagged) {
        return false; //todo
//        ix < 20
//                && !(ix > 0 && tagged.get(ix - 1).getTag().equals(Label.SECTION_NR))
//                &&
//                ((untagged.size() > ix + 1 && untagged.get(ix + 1).startsWithLowerCaseLetter)
//                        || (untagged.get(ix).endsWithNonLetter)
//                );
    }

    public static boolean looksLikeNumberingButProbablyIsnt(List<org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf> elements, int ix) {
        return NumberingFeature.PART_OF_WG.apply(elements, ix) // [w.][g.]
                || NumberingFeature.FOLLOWS_WG.apply(elements, ix) // w.g. [A.] van der Hoeven
                || NumberingFeature.probableAbbrFlorijnen.apply(elements, ix) // florijnen
                || NumberingFeature.probableAbbrEuro.apply(elements, ix) // euro
                || NumberingFeature.probablyJustU.apply(elements, ix) // u heeft
                || (NumberingFeature._S.apply(elements, ix)) // 's
                || (NumberingFeature.IS_SPELLED_OUT.apply(elements, ix)) // 2 (twee)
                || (NumberingFeature.NUM_LARGER_THAN_15.apply(elements, ix)) // We assume full numbers are <= 15
                //|| (isProbablyJustStartOfSentence.apply(elements, ix)) // 7 patronen //TODO
                || (NumberingFeature.isPartOfSpacedLetters.apply(elements, ix)) // u i t s p r a a k
                || (NumberingFeature.IS_PROBABLY_NAME.apply(elements, ix)) //
                ;
    }
}
