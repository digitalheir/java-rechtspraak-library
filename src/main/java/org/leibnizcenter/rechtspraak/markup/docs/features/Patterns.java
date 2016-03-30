package org.leibnizcenter.rechtspraak.markup.docs.features;

import org.leibnizcenter.rechtspraak.features.quote.BlockQuotePatterns;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledToken;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;

/**
 * Created by Maarten on 3/7/2016.
 */
public interface Patterns {

    static boolean matches(UnnormalizedTextContains containsBracketedText, LabeledToken labeledToken) {
        return matches(containsBracketedText, labeledToken.getToken());
    }

    static boolean matches(NormalizedTextMatches pattern, RechtspraakElement element) {
        return pattern.matches(element.normalizedText);
    }

    static boolean matches(NormalizedTextContains pattern, RechtspraakElement element) {
        return element != null && pattern.matches(element.normalizedText);
    }

    static boolean matches(UnnormalizedTextContains pattern, RechtspraakElement element) {
        return pattern.matches(element.getTextContent());
    }

    static boolean matches(BlockQuotePatterns luidtAlsVolgt, LabeledToken labeledToken) {
        return matches(luidtAlsVolgt, labeledToken.getToken());
    }

    boolean matches(String textContent);

    interface UnnormalizedTextContains extends Patterns {
    }

    interface NormalizedTextContains extends Patterns {
    }

    interface NormalizedTextMatches extends Patterns {
    }
}
