package org.leibnizcenter.rechtspraak.tokens.numbering.interfaces;

import org.leibnizcenter.rechtspraak.tokens.numbering.SingleCharNumbering;

/**
 * Created by maarten on 2-4-16.
 */
public interface AlphabeticNumbering extends SingleCharNumbering {


    static char nextChar(char character) {
        switch (character) {
            case 'z':
            case 'Z':
            case '\0':
                return '\0';
            default:
                return ((char) (character + 1));
        }
    }

    static char prevChar(char character) {
        switch (character) {
            case 'a':
            case 'A':
            case '\0':
                return '\0';
            default:
                return ((char) (character + 1));
        }
    }
}
