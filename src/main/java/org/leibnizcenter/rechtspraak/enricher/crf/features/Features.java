package org.leibnizcenter.rechtspraak.enricher.crf.features;

import cc.mallet.types.Token;
import org.leibnizcenter.rechtspraak.enricher.crf.features.elementpatterns.ElementFeature;
import org.leibnizcenter.rechtspraak.enricher.crf.features.elementpatterns.NumberingFeature;
import org.leibnizcenter.rechtspraak.enricher.crf.features.elementpatterns.interfaces.ElementFeatureFunction;
import org.leibnizcenter.rechtspraak.enricher.crf.features.elementpatterns.interfaces.NamedElementFeatureFunction;
import org.leibnizcenter.rechtspraak.enricher.crf.features.textpatterns.GeneralTextFeature;
import org.leibnizcenter.rechtspraak.enricher.crf.features.textpatterns.TitlePatterns;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helpers functions for mostlikelytreefromlist
 * <p>
 * Created by Maarten on 2016-03-21.
 */
public class Features {
    public static void setAllFeatures(Token t, List<TokenTreeLeaf> sequence, int ix) {
        TokenTreeLeaf token = sequence.get(ix);
        TokenTreeLeaf previousToken = ix > 0 ? sequence.get(ix - 1) : null;

        /**
         * General element features (ie is this newline, text block, etc)
         */
        ElementFeature.setFeatures(t, sequence, ix);

        /**
         * Features specifically for numberings
         */
        NumberingFeature.setFeatures(t, sequence, ix);
        if(!(token instanceof Newline)) {
            /**
             * Features for string matches
             */
            TitlePatterns.setFeatureValues(t, sequence, ix);
            /**
             * Features for general text patterns (capitalization, string length etc)
             */
            Features.setFeatures(t, sequence, ix, (NamedElementFeatureFunction[]) GeneralTextFeature.values());
        }


//        Features.setFeatures(t, sequence, ix, (NamedElementFeatureFunction[]) KnownSurnamesNl.values());


        // General patterns
        //
        // Whether the text contains a name, or something that looks like a name
        //Names.NamePatterns.setFeatureValues(t, token);
        // Whether the text contains a place name, or something that looks like a place name
        //PlaceNamesInNL.setFeatureValues(t, token);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        //TODO something like SEEMS_TO_BE_PART_OF_BOLD_TITLES

        if (token.getTextContent().length() > 0) {
            char firstChar = token.getTextContent().charAt(0);
            if (!Character.isLetterOrDigit(firstChar))
                t.setFeatureValue("START_W_" + firstChar, 1.0);
        }
    }

    public static Set<NamedElementFeatureFunction> setFeatures(Token t, List<TokenTreeLeaf> tokens, int ix,
                                                               NamedElementFeatureFunction... values) {
        Set<NamedElementFeatureFunction> matches = new HashSet<>();
        for (NamedElementFeatureFunction f : values)
            if (f.apply(tokens, ix)) {
                t.setFeatureValue(f.name(), 1.0);
                matches.add(f);
            }
//            else t.setFeatureValue("NOT_"+f.name(), 1.0);
        return matches;
    }

    public static boolean matchesAny(List<TokenTreeLeaf> tokens, int ix, ElementFeatureFunction... patterns) {
        for (ElementFeatureFunction p : patterns)
            if (p.apply(tokens, ix))
                return true;
        return false;
    }
}
