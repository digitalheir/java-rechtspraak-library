package org.leibnizcenter.rechtspraak.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maarten on 11-3-16.
 */
public class TextPattern {
    public final String name;
    public final Pattern pattern;

    public TextPattern(String name, Pattern regex) {
        this.name = name;
        this.pattern = regex;
    }

    public TextPattern(Pattern compile) {
        this(compile.pattern(), compile);
    }

    public boolean matches(String text) {
        return pattern.matcher(text).matches();
    }

    public boolean find(String text) {
        return pattern.matcher(text).find();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextPattern that = (TextPattern) o;

        return name.equals(that.name) && pattern.equals(that.pattern)
;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + pattern.hashCode();
        return result;
    }

    public String name() {
        return name;
    }


    public static class Match {
        public final TextPattern pattern;
        public final Matcher matcher;

        public Match(TextPattern pattern, Matcher matcher) {
            this.pattern = pattern;
            this.matcher = matcher;
        }
    }
}
