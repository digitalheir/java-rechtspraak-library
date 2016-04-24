package org.leibnizcenter.util.numbering;

import java.util.Objects;

/**
 * Created by maarten on 28-3-16.
 */
public class NonNumericNumbering implements SingleTokenNumbering {
    private final String terminal;
    private final String symbol;

    public NonNumericNumbering(String symbol, String terminal) {
        this.symbol = symbol.trim();
        if (terminal != null) terminal = terminal.trim();
        this.terminal = terminal;
    }

    @Override
    public boolean isSuccedentOf(NumberingNumber n2) {
        return this.equals(n2);
    }


    @Override
    public int mainNum() {
        return 1;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NonNumericNumbering that = (NonNumericNumbering) o;

        return Objects.equals(terminal, that.terminal) && symbol.equals(that.symbol);

    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public int hashCode() {
        int result = terminal.hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }
}
