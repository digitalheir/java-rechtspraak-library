package org.leibnizcenter.rechtspraak.cfg.rule.type;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

/**
 * Created by maarten on 18-4-16.
 */
public class NonTerminal implements Type {
    private final String name;
    private static final Pattern REGEX = Pattern.compile("\\p{Lu}[\\p{L}0-9'\"]*");

    /**
     * @param name String to represent this terminal. Must be capitalized.
     */
    public NonTerminal(@NotNull String name) {
        if (!REGEX.matcher(name).matches())
            throw new InvalidParameterException("Non-terminal must start with uppercase letter");
        this.name = name;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(@NotNull Type o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NonTerminal that = (NonTerminal) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
