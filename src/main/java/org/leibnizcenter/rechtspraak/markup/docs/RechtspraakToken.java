package org.leibnizcenter.rechtspraak.markup.docs;

import org.crf.utilities.TaggedToken;
import org.w3c.dom.Element;


/**
 * Created by maarten on 28-2-16.
 */
public class RechtspraakToken extends TaggedToken<RechtspraakElement, Label> {

    public RechtspraakToken(Element token, Label tag) {
        super(new RechtspraakElement(token), tag);
    }

    @Override
    public String toString() {
        return "[" + this.getTag() + "]: " + getToken().getTextContent().trim();
    }

}
