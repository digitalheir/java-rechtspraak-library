package org.leibnizcenter.rechtspraak.tokens.features.textpatterns.interfaces;

import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.LabeledToken;

/**
 * Created by Maarten on 3/7/2016.
 */
public interface Patterns {
    static boolean match(UnnormalizedTextContains containsBracketedText, LabeledToken labeledToken) {
        return matches(containsBracketedText, labeledToken.getToken());
    }

    static boolean match(NormalizedTextMatches pattern, TokenTreeLeaf element) {
        return pattern.matches(element.getNormalizedText());
    }

    static boolean match(NormalizedTextContains pattern, TokenTreeLeaf element) {
        return element != null && pattern.matches(element.getNormalizedText());
    }

    static boolean matches(UnnormalizedTextContains pattern, TokenTreeLeaf element) {
        return pattern.matches(element.getTextContent());
    }

    boolean matches(String textContent);

    interface UnnormalizedTextContains extends Patterns {
    }

    interface UnnormalizedTextMatches extends Patterns {
    }

    interface NormalizedTextContains extends Patterns {
    }

    interface NormalizedTextMatches extends Patterns {
    }
}
