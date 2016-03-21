package org.leibnizcenter.rechtspraak.markup.docs.features;

import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakToken;

/**
 * Created by Maarten on 3/7/2016.
 */
public interface Patterns {

    static boolean matches(UnnormalizedTextContains containsBracketedText, RechtspraakToken rechtspraakToken) {
        return matches(containsBracketedText, rechtspraakToken.getToken());
    }

    static boolean matches(NormalizedTextMatches pattern, RechtspraakElement element) {
        return pattern.matches(element.normalizedText);
    }

    static boolean matches(NormalizedTextContains pattern, RechtspraakElement element) {
        return pattern.matches(element.normalizedText);
    }

    static boolean matches(UnnormalizedTextContains pattern, RechtspraakElement element) {
        return pattern.matches(element.getTextContent());
    }

    boolean matches(String textContent);

    interface UnnormalizedTextContains extends Patterns {
    }

    interface NormalizedTextContains extends Patterns {
    }

    interface NormalizedTextMatches extends Patterns {
    }
}
