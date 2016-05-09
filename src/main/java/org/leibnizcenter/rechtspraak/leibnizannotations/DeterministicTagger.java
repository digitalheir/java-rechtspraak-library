package org.leibnizcenter.rechtspraak.leibnizannotations;

import org.leibnizcenter.rechtspraak.enricher.crf.features.elementpatterns.ElementFeature;
import org.leibnizcenter.rechtspraak.enricher.crf.features.elementpatterns.NumberingFeature;
import org.leibnizcenter.rechtspraak.enricher.crf.features.textpatterns.TitlePatterns;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maarten on 29-3-16.
 */
public class DeterministicTagger {
    // TODO make feature functions

    public static List<Label> tag(List<TokenTreeLeaf> untagged) {
        List<Label> tagged = new ArrayList<>(untagged.size());
        int firstTitle = -1;
        for (int i = 0; i < untagged.size(); i++) {
            Label tag = firstPass(untagged, i);

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

    public static Label firstPass(List<TokenTreeLeaf> untagged, int ix) {
        TokenTreeLeaf token = untagged.get(ix);
        if (ElementFeature.dontTryToLabel.apply(untagged,ix)) {
            return Label.TEXT_BLOCK;
        } else if (ElementFeature.probablySectionTitle.apply(untagged, ix)) {
            //if (probablyInfoTag(untagged, ix, tagged)) return Label.TEXT_BLOCK;
            //else
            return Label.SECTION_TITLE;
        } else if (token instanceof Numbering) {
            if (ElementFeature.isProbablyNumbering.apply(untagged, ix)) return Label.getNumberingType(untagged, ix);
            else return Label.TEXT_BLOCK;
        } else {
            return Label.TEXT_BLOCK;
        }
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

    public static boolean looksLikeNumberingButProbablyIsnt(List<org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf> tokens, int ix) {
        return NumberingFeature.any(
                tokens, ix,
                NumberingFeature.PART_OF_WG, // [w.][g.]
                NumberingFeature.FOLLOWS_WG, // w.g. [A.] van der Hoeven
                NumberingFeature.NUM_LARGER_THAN_100, // We assume full numbers are <= 100
                NumberingFeature.probableAbbrFlorijnen, // florijnen
                NumberingFeature.probableAbbrEuro, // euro
                NumberingFeature.probablyJustU, // u heeft
                NumberingFeature._S, // 's
                NumberingFeature.MORE_THAN_2_NUMBERS_IN_SEQUENCE,
                NumberingFeature.HAS_NO_PLAUSIBLE_PREDECESSORS_OR_SUCCESSORS,
                NumberingFeature.SUSPECT_NUMBERING,
                NumberingFeature.IS_SPELLED_OUT, // 2 (twee)
                NumberingFeature.isPartOfSpacedLetters, // u i t s p r a a k
                NumberingFeature.IS_PROBABLY_NAME //
        );
        //|| (isProbablyJustStartOfSentence,) // 7 patronen //TODO
    }
}
