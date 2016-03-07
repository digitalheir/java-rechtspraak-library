package org.leibnizcenter.rechtspraak.markup;

import org.crf.crf.filters.Filter;
import org.leibnizcenter.rechtspraak.util.Labels;

import java.security.InvalidParameterException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by maarten on 29-2-16.
 */
public enum Label {
    NULL(null),
    INFO("*.info"),
    //SECTION_PARA("section"),
    SECTION_TITLE("section-title"),
    //LABEL_NR ("nr"),
    OUT("out");

    private static final Map<Label, Map<Label, Filter<RechtspraakElement, Label>>> transitionFilters =
            new EnumMap<>(Label.class);

    static {
        for (Label from : Labels.set) {
            Map<Label, Filter<RechtspraakElement, Label>> filterMap = new EnumMap<>(Label.class);
            for (Label to : Labels.set) {
                filterMap.put(to, new TransitionFilter(from, to));
            }
            transitionFilters.put(from, filterMap);
        }
    }

    public final String label;

    Label(String label) {
        this.label = label;
    }

    public static Label get(String label) {
        for (Label l : Label.values()) {
            if (label.equals(l.label)) {
                return l;
            }
        }

        throw new InvalidParameterException("Could not find enum value for " + label);
    }

    public static Filter<RechtspraakElement, Label> getTransitionFilters(Label from, Label to) {
        Filter<RechtspraakElement, Label> filter = transitionFilters.get(from).get(to);
        if (filter == null) throw new NullPointerException();
        return filter;
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
