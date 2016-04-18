package org.leibnizcenter.rechtspraak.cfg.rule.type;

import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.cfg.TypeScore;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

/**
 * Created by maarten on 18-4-16.
 */
public class Terminal implements Type,TypeScore {
    private final Object name;

    /**
     * @param name Object to represent this terminal.
     */
    public Terminal(@NotNull Object name) {
        this.name = name;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public int compareTo(@NotNull Type o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Terminal terminal = (Terminal) o;

        return name.equals(terminal.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public Type getType() {
        return this;
    }
}
