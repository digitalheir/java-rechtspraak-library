package org.leibnizcenter.rechtspraak.tagging;

import com.google.common.collect.ImmutableMap;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

import java.util.*;

/**
 * Labels
 * <p>
 * Created by maarten on 1-4-16.
 */
public enum Label {
    //    QUOTE,
    NEWLINE,
    NR,
    SECTION_TITLE,
    TEXT_BLOCK, label;

    public final static Map<String, Label> fromString;

    static {
        ImmutableMap.Builder<String, Label> b = ImmutableMap.builder();
        for (Label l : Label.values()) {
            b.put(l.name(), l);
        }
        fromString = b.build();
    }

    public static Label getNumberingType(List<TokenTreeLeaf> tokens, int i) {
        if (i + 1 > tokens.size()) return Label.TEXT_BLOCK;
        else return Label.NR;
    }

    public static boolean isNumbering(Label label) {
        switch (label) {
            case NR:
                return true;
            default:
                return false;
        }
    }

    private static boolean isAllowed(Label l1, Label l2) {
        return true;
    }

    public static Collection<Label[]> getAllowedTransitions() {
        Collection<Label[]> s = new HashSet<>();
        for (Label l1 : values()) {
            for (Label l2 : values()) {
                //noinspection ConstantConditions
                if (isAllowed(l1, l2)) {
                    s.add(new Label[]{l1, l2});
                }
            }
        }
        return s;
    }
}
