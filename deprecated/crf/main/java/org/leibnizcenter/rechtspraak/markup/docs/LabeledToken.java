package org.leibnizcenter.rechtspraak.markup.docs;

import deprecated.org.crf.utilities.TaggedToken;
import org.leibnizcenter.rechtspraak.markup.docs.tokentree.TokenTreeVertex;
import org.w3c.dom.Element;


/**
 * Created by maarten on 28-2-16.
 */
public class LabeledToken extends TaggedToken<RechtspraakElement, Label> implements TokenTreeVertex {

    public LabeledToken(Element token, Label tag) {
        super(token instanceof RechtspraakElement ? (RechtspraakElement) token : new RechtspraakElement(token), tag);
        if (tag == null) throw new NullPointerException("Tag can't be null");
    }

    @Override
    public String toString() {
        return "[" + this.getTag() + "]: " + getToken().getTextContent().trim();
    }
}
