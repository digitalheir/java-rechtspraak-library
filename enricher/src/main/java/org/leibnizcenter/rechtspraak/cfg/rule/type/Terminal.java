package org.leibnizcenter.rechtspraak.cfg.rule.type;

import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.cfg.rule.TypeContainer;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

/**
 * Created by maarten on 18-4-16.
 */
public class Terminal implements Type, TypeContainer {
    private final Object name;
    private final Object data;

    /**
     * @param name Object to represent this terminal.
     */
    public Terminal(@NotNull Object name) {
        this.name = name;
        this.data=null;
    }

    /**
     *
     * @param name Object to represent this terminal.
     * @param data Some extra data, does NOT count for equals()!!!
     */
    public Terminal(@NotNull Object name, Object data) {
        this.name=name;
        this.data=data;
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
        if (o == null || !(o instanceof Terminal)) return false;

        Terminal terminal = (Terminal) o;

        return name.equals(terminal.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Object getData() {
        return data;
    }

    @Override
    public Type getType() {
        return this;
    }
}
