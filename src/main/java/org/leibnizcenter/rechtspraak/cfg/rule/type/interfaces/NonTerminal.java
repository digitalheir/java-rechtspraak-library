package org.leibnizcenter.rechtspraak.cfg.rule.type.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * Created by maarten on 18-4-16.
 */
public interface NonTerminal extends Type {
    @Override
    default boolean isTerminal() {
        return false;
    }

    String getName();

    @Override
    default int compareTo(@NotNull Type o) {
        return toString().compareTo(o.toString());
    }

}
