package org.leibnizcenter.rechtspraak.markup.docs;

import com.google.common.collect.Maps;
import deprecated.org.crf.crf.filters.Filter;
import org.leibnizcenter.rechtspraak.util.Labels;
import org.leibnizcenter.rechtspraak.util.numbering.FullNumber;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by maarten on 29-2-16.
 */
public enum Label {
//    INFO,
//    QUOTE_START,
//    QUOTE_IN,

//    LIST_ITEM_START,
//    LIST_ITEM_IN,

    SECTION_TITLE,

    // [T.] S. Eliot, expect this always to be inline
    PART_OF_TEXT_NR,

    // [1.] Verloop
    NR_FULL_INLINE,
    // [1.]
    // Verloop
    NR_FULL_BLOCK,

    // [1.1.] Bla bla bla
    NR_SUBSECTION_INLINE,
    // [1.1.]
    // Bla bla bla
    NR_SUBSECTION_BLOCK,

    // Expect always to be inline
    NR_NON_NUMERIC,


    TEXT_BLOCK;

    private static final Map<Label, Map<Label, Filter<RechtspraakElement, Label>>> transitionFilters =
            new EnumMap<>(Label.class);
    public static Map<String, Label> map = Maps.newHashMap();

    static {
        for (Label l : Label.values()) map.put(l.name(), l);
    }

    static {
        for (Label from : Labels.set) {
            Map<Label, Filter<RechtspraakElement, Label>> filterMap = new EnumMap<>(Label.class);
            for (Label to : Labels.set) {
                filterMap.put(to, new TransitionFilter(from, to));
            }
            transitionFilters.put(from, filterMap);
        }
    }

    public static Filter<RechtspraakElement, Label> getTransitionFilters(Label from, Label to) {
        Filter<RechtspraakElement, Label> filter = transitionFilters.get(from).get(to);
        if (filter == null) throw new NullPointerException();
        return filter;
    }

    public static Label get(String str) {
        return map.get(str);
    }


    public static Collection<Label[]> getAllowedTransitions() {
        Collection<Label[]> s = new HashSet<>();
        for (Label l1 : values()) {
            for (Label l2 : values()) {
                if (isAllowed(l1, l2)) {
                    s.add(new Label[]{l1, l2});
                }
            }
        }
        return s;
    }

    private static boolean isAllowed(Label l1, Label l2) {
        return true;
    }

    public static List<Label> getAllowedTransitions(Label l) {
        List<Label> allowds = new ArrayList<>(values().length);
        for (Label l2 : values()) if (isAllowed(l, l2)) allowds.add(l2);
        return allowds;
    }

    public static boolean isSectionNumber(Label l) {
        switch (l) {
            case NR_FULL_INLINE:
            case NR_FULL_BLOCK:
            case NR_SUBSECTION_INLINE:
            case NR_SUBSECTION_BLOCK:
            case NR_NON_NUMERIC:
                return true;
            case SECTION_TITLE:
            case PART_OF_TEXT_NR: // Initials, etc
            case TEXT_BLOCK:
                return false;
            default:
                throw new InvalidParameterException();
        }
    }

    public static Label getNumberingType(RechtspraakElement token) {
        if (token.precedesNonEmptyText) {
            if (token.numbering instanceof FullNumber) {

            } else {
                return NR_NON_NUMERIC;
            }
        }
        return NR_FULL_BLOCK;//todo
    }

    public static void setSectionNumber(LabeledToken prev) {
        prev.tag=NR_FULL_BLOCK;
        // TODO
    }

    public static class TransitionFilter extends Filter<RechtspraakElement, Label> {
        private final Label to;
        private final Label from;

        public TransitionFilter(Label from, Label to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TransitionFilter that = (TransitionFilter) o;

            return to == that.to && from == that.from;
        }

        @Override
        public int hashCode() {
            int result = to.hashCode();
            result = 31 * result + from.hashCode();
            return result;
        }
    }
}
