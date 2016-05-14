package org.leibnizcenter.rechtspraak.tagging.crf.features.elementpatterns.interfaces;


import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by maarten on 1-4-16.
 */
public interface ElementFeatureFunction {
    static ElementFeatureFunction matches(Pattern patt) {
        return (elements, i) -> elements.get(i).getTextContent()!=null && patt.matcher(elements.get(i).getTextContent()).matches();
    }

    static ElementFeatureFunction find(Pattern patt) {
        return (elements, i) -> elements.get(i).getTextContent()!=null && patt.matcher(elements.get(i).getTextContent()).find();
    }

    boolean apply(List<TokenTreeLeaf> tokens, int ix);

    static ElementFeatureFunction matchesNormalized(Pattern pattern) {
        return (elements, i) -> pattern.matcher(elements.get(i).getNormalizedText()).matches();
    }
}
