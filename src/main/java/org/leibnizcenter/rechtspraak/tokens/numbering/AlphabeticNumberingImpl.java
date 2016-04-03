package org.leibnizcenter.rechtspraak.tokens.numbering;

import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.AlphabeticNumbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;

import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * Created by maarten on 16-2-16.
 */
public class AlphabeticNumberingImpl implements AlphabeticNumbering {
    private int num;
    private char character;
    private String terminal;

    public AlphabeticNumberingImpl(String num, String terminal) {
        this(num.charAt(0), terminal);
        if (num.length() != 1) throw new InvalidParameterException();
    }

    public AlphabeticNumberingImpl(char cg, String terminal) {
        character = cg;
        this.num = (Character.toLowerCase(character) - 'a') + 1;
        if (this.num < 1 || this.num > 26) throw new IllegalStateException();

        if (terminal != null) {
            terminal = terminal.trim();
            if (terminal.length() <= 0) terminal = null;
        }
        this.terminal = terminal;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    @Override
    public String toString() {
        return character + (terminal == null ? "" : terminal);
    }

    @Override
    public boolean isSuccedentOf(NumberingNumber precedent) {
        // We can't mix Arabics/Alphabetics/Romans
        return precedent instanceof SingleCharNumbering
                && (char) (((SingleCharNumbering) precedent).getCharacter() + 1) == getCharacter();
    }

    @Override
    public boolean isFirstNumbering() {
        return character == 'a' || character == 'A';
    }

    @Override
    public String getTerminal() {
        return terminal;
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

//    @Override
//    public int intValue() {
//        return num;
//    }
//
//    @Override
//    public long longValue() {
//        return num;
//    }
//
//    @Override
//    public float floatValue() {
//        return num;
//    }
//
//    @Override
//    public double doubleValue() {
//        return num;
//    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SingleCharNumbering && equalsSansTerminal(obj)
                && Objects.equals(terminal, ((SingleCharNumbering) obj).getTerminal());
    }

    @Override
    public boolean equalsSansTerminal(Object obj) {
        return obj instanceof SingleCharNumbering
                && ((SingleCharNumbering) obj).getCharacter() == this.character;
    }
}
