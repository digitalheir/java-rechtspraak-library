package org.leibnizcenter.rechtspraak.tokens.text;

import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.w3c.dom.*;

import java.util.List;

/**
 * XML element from Rechtspraak.nl that is some text block
 * Created by maarten on 29-2-16.
 */
public class TextElement extends RechtspraakElement {
    public TextElement(Element e) {
        super(e);
    }

    public static boolean is(List<TokenTreeLeaf> tokens, int i) {
        return tokens.get(i) instanceof TextElement;
    }
}
