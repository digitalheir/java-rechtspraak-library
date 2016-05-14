package org.leibnizcenter.rechtspraak.tokens;

import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTreeVertex;


/**
 * Created by maarten on 28-2-16.
 */
public class LabeledToken extends TaggedToken<TokenTreeLeaf, Label> implements TokenTreeVertex {

    public LabeledToken(TokenTreeLeaf token, Label tag) {
        super(token, tag);
        if (tag == null) throw new NullPointerException("Tag can't be null");
    }

    @Override
    public String toString() {
        return "[" + this.getTag() + "]: "
                + getToken().getTextContent().trim();
    }
}
