package org.leibnizcenter.rechtspraak.cfg;

/**
 * Used when a grammar is not
 * correctly defined
 */

class MalformedGrammarException extends Exception {
    MalformedGrammarException() {
        super();
    }

    MalformedGrammarException(String s) {
        super(s);
    }
}