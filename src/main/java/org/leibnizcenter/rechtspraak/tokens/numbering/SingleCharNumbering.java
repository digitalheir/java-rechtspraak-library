package org.leibnizcenter.rechtspraak.tokens.numbering;

import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.SingleTokenNumbering;

/**
 * Created by maarten on 2-4-16.
 */
public interface SingleCharNumbering extends SingleTokenNumbering {
    char getCharacter();
    char nextChar();
    char prevChar();

    static char getChar(TokenTreeLeaf token) {
        if(token instanceof SingleCharNumbering) return ((SingleCharNumbering) token).getCharacter();
        return '\0';
    }
}
