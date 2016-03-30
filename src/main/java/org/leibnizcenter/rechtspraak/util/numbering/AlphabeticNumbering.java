package org.leibnizcenter.rechtspraak.util.numbering;

import java.security.InvalidParameterException;

/**
 * Created by maarten on 16-2-16.
 */
public class AlphabeticNumbering extends Number implements FullNumber {
    private int num;
    private char character;
    private String terminal;

    public AlphabeticNumbering(String num, String terminal) {
        this(num.charAt(0), terminal);
        if (num.length() != 1) throw new InvalidParameterException();
    }

    public AlphabeticNumbering(char cg, String terminal) {
        character = cg;
        this.num = (Character.toLowerCase(character) - 'a') + 1;

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
        return num + "";
    }

    @Override
    public boolean isSuccedentOf(NumberingNumber precedent) {
        if (precedent.mainNum() + 1 == num) {
            //noinspection Duplicates
            if (precedent instanceof AlphabeticNumbering) {
                return true;
            } else {
                return false; // We can't mix Arabics/Alphabetics/Romans
            }
        } else {
            return false;
        }
    }

    @Override
    public int mainNum() {
        return num;
    }

    @Override
    public boolean isFirstNumbering() {
        return num == 1;
    }

    @Override
    public String getTerminal() {
        return terminal;
    }

    @Override
    public String canonicalRepresentation() {
        return String.valueOf(character);
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public int intValue() {
        return num;
    }

    @Override
    public long longValue() {
        return num;
    }

    @Override
    public float floatValue() {
        return num;
    }

    @Override
    public double doubleValue() {
        return num;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO check if terminal is the same?
        return obj instanceof AlphabeticNumbering && ((AlphabeticNumbering) obj).mainNum() == this.mainNum();
    }

    @Override
    public FullNumber succ() {
        return new AlphabeticNumbering((char) (this.character + 1), terminal);
    }
}
