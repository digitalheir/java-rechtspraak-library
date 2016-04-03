package org.leibnizcenter.rechtspraak.tokens.text;

import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.w3c.dom.*;

/**
 * XML element from Rechtspraak.nl that is some text block
 * Created by maarten on 29-2-16.
 */
public class TextElement extends RechtspraakElement {
    public TextElement(Element e) {
        super(e);
    }
}
