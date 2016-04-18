package org.leibnizcenter.rechtspraak.tokens.numbering;

import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;

import java.util.Objects;

/**
 * Created by maarten on 28-3-16.
 */
public class NonNumericNumbering implements SingleCharNumbering {
    private final String terminal;
    private final char symbol;

    public NonNumericNumbering(String symbol, String terminal) {
        symbol = symbol.trim();
        if (symbol.length() != 1) throw new IllegalStateException();
        this.symbol = symbol.charAt(0);
        if (terminal != null) terminal = terminal.trim();
        this.terminal = terminal;
    }

    @Override
    public boolean isSuccedentOf(NumberingNumber n2) {
        return this.equals(n2);
    }

    @Override
    public boolean isFirstNumbering() {
        return true;
    }

    @Override
    public String getTerminal() {
        return terminal;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NonNumericNumbering
                && equalsSansTerminal(obj)
                && Objects.equals(((NonNumericNumbering) obj).getTerminal(), terminal);
    }

    @Override
    public boolean equalsSansTerminal(Object obj) {
        return obj instanceof NonNumericNumbering && ((NonNumericNumbering) obj).getSymbol() == this.getSymbol();
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public int hashCode() {
        int result = terminal.hashCode();
        result = 31 * result + symbol;
        return result;
    }

    @Override
    public char getCharacter() {
        return symbol;
    }

    @Override
    public char nextChar() {
        return symbol;
    }

    @Override
    public char prevChar() {
        return symbol;
    }

    @Override
    public boolean couldBeFirstInSequence() {
        return true;
    }
}
