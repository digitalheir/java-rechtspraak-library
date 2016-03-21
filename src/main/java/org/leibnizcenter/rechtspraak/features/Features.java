package org.leibnizcenter.rechtspraak.features;

import cc.mallet.types.Token;
import org.leibnizcenter.rechtspraak.features.info.InfoPatterns;
import org.leibnizcenter.rechtspraak.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.numbering.FullSectionNumber;
import org.leibnizcenter.rechtspraak.util.numbering.SubSectionNumber;

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
    public static final String LIKELY_PART_OF_LIST = "LIKELY_PART_OF_LIST";
    public static final String HAS_SUBSECTION_NUMBERING = "HAS_SUBSECTION_NUMBERING";
    public static final String HAS_SECTION_NUMBERING = "HAS_SECTION_NUMBERING";

    public static void setFeatureValues(LabeledTokenList sequence, int indexInSequence, Token t) {
        RechtspraakElement token = sequence.get(indexInSequence).getToken();

//        System.out.println("1info");
        // Info patterns
        InfoPatterns.InfoPatternsNormalizedContains.setFeatureValues(t, token);
        InfoPatterns.InfoPatternsNormalizedMatches.setFeatureValues(t, token);
        InfoPatterns.InfoPatternsUnormalizedContains.setFeatureValues(t, token);
//        System.out.println("2info");

        // Title patterns
//        System.out.println("1title");
        TitlePatterns.TitlesNormalizedMatchesHighConf.setFeatureValues(t, token);
        TitlePatterns.TitlesNormalizedMatchesLowConf.setFeatureValues(t, token);
        TitlePatterns.TitlesUnnormalizedContains.setFeatureValues(t, token);
//        System.out.println("2title");

//        System.out.println("1names");
        // Whether the text contains a name, or something that looks like a name
        //Names.NamePatterns.setFeatureValues(t, token);
        // Whether the text contains a place name, or something that looks like a place name
        //PlaceNamesInNL.setFeatureValues(t, token);

//        System.out.println("2names");

//        System.out.println("1etc");
        if (token.numbering != null) {
            if (token.numbering instanceof SubSectionNumber) {
                t.setFeatureValue(HAS_SUBSECTION_NUMBERING, 1.0);
            } else if (token.numbering instanceof FullSectionNumber) {
                t.setFeatureValue(HAS_SECTION_NUMBERING, 1.0);
            }
        }
        if (token.highConfidenceNumberedTitleFoundAndIsNumbered()) t.setFeatureValue(VERY_PROBABLE_SECTION, 1.0);
        if (token.isLikelyPartOfList()) t.setFeatureValue(LIKELY_PART_OF_LIST, 1.0);


        if (token.isSpaced) t.setFeatureValue(IS_SPACED, 1.0);
        if (token.endsWithNonLetter) t.setFeatureValue(ENDS_WITH_NON_LETTER, 1.0);
        if (token.isAllCaps) t.setFeatureValue(IS_ALL_CAPS, 1.0);
        //if (token.wordCount < 5) t.setFeatureValue("LESS_THAN_5_WORDS", 1.0);
        //if (token.wordCount >= 5 && token.wordCount < 10) t.setFeatureValue("LESS_THAN_10_WORDS", 1.0);
        if (token.wordCount >= 5 && token.wordCount >= 10) t.setFeatureValue(AT_LEAST_10_WORDS, 1.0);

        if (indexInSequence < 5) t.setFeatureValue(WITHIN_FIRST_5_BLOCKS, 1.0);
        if (indexInSequence >= 5 && indexInSequence < 10) t.setFeatureValue(WITHIN_FIRST_10_BLOCKS, 1.0);
        //if (indexInSequence < sequence.size() / 4) t.setFeatureValue("FIRST_QUARTILE", 1.0);
        if (indexInSequence >= sequence.size() / 2) t.setFeatureValue(OVER_HALF, 1.0);
//        System.out.println("2etc");
    }
}
