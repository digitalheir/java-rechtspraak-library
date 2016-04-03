package org.leibnizcenter.rechtspraak.tokens.numbering;

import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.AlphabeticNumbering;

/**
 * Created by maarten on 2-4-16.
 */
public class AmbiguousAlphabeticOrRomanNumeral extends RomanNumeral implements AlphabeticNumbering {
    private final char character;


    public AmbiguousAlphabeticOrRomanNumeral(String num, String terminal) {
        super(num, terminal);
        if (num.length() != 1) throw new IllegalStateException();
        this.character = num.charAt(0);
        if (!isAmbiguousCharacter(character)) throw new IllegalStateException();
    }

    public static boolean isAmbiguousCharacter(char character) {
        switch (character) {
            case 'i':
            case 'I':
            case 'v':
            case 'V':
            case 'x':
            case 'X':
            case 'c':
            case 'C':
            case 'm':
            case 'M':
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isFirstNumbering() {
        return super.isFirstNumbering();
    }



    @Override
    public char getCharacter() {
        return character;
    }

    @Override
    public char nextChar() {
        return AlphabeticNumbering.nextChar(character);
    }

    @Override
    public char prevChar() {
        return AlphabeticNumbering.prevChar(character);
    }
}
