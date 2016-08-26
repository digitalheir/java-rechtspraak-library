package org.leibnizcenter.rechtspraak.enricher;

import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.token.Token;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.LabeledToken;

/**
 * Created by Maarten on 23-8-2016.
 */
public class LabelRecognizer implements Terminal<LabeledToken> {
    private final Label label;

    public LabelRecognizer(Label label) {
        this.label = label;
    }

    @Override
    public boolean hasCategory(Token<LabeledToken> token) {
        return label.equals(token.obj.getTag());
    }

    @Override
    public String toString() {
        return label.toString();
    }
}
