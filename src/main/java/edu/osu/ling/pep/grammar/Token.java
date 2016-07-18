package edu.osu.ling.pep.grammar;

/**
 * Created by Maarten on 2016-06-06.
 */
public class Token {
    public final Object obj;

    public Token(Object source) {
        this.obj = source;
    }

    @Override
    public String toString() {
        return obj.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return obj != null ? obj.equals(token.obj) : token.obj == null;

    }

    @Override
    public int hashCode() {
        return obj != null ? obj.hashCode() : 0;
    }
}
