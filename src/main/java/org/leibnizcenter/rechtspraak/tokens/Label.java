package org.leibnizcenter.rechtspraak.tokens;

import org.leibnizcenter.rechtspraak.tokens.numbering.ListMarking;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

import java.util.List;

/**
 * Labels
 * <p>
 * Created by maarten on 1-4-16.
 */
public enum Label {
    QUOTE,
    NEWLINE,
    NR_BLOCK,
    NR_INLINE,
    SECTION_TITLE,
    LIST_HEADING,
    TEXT_BLOCK;

    public static Label get(String manualAnnotation) {
        throw new NullPointerException();
    }

    public static Label getNumberingType(List<TokenTreeLeaf> tokens, int i) {
        if (((Numbering) tokens.get(i)).getNumbering() instanceof ListMarking) return Label.LIST_HEADING;
        else {
            if (i + 1 > tokens.size()) return Label.TEXT_BLOCK;
            else {
                if (tokens.get(i) instanceof Newline) return Label.NR_BLOCK;
                else return Label.NR_INLINE;
            }
        }
    }

    public static boolean isNumbering(Label label) {
        switch (label) {
            case NR_BLOCK:
            case NR_INLINE:
                return true;
            default:
                return false;
        }
    }
}
