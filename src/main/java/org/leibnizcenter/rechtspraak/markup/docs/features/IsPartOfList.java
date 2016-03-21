package org.leibnizcenter.rechtspraak.markup.docs.features;

import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakTokenList;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.numbering.NumberingNumber;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by maarten on 2-3-16.
 */
public class IsPartOfList {
    public static final Map<Label, IsPartOfListFeature> features = new EnumMap<>(Label.class);
    public static final Map<Label, IsPartOfListFilter> filters = new EnumMap<>(Label.class);

    static {
        for (Label l : Label.values()) {
            features.put(l, new IsPartOfListFeature(l));
            filters.put(l, new IsPartOfListFilter(l));
        }
    }

    public static boolean isPartOfList(RechtspraakTokenList sequence, int indexInSequence) {
        RechtspraakElement token = sequence.get(indexInSequence).getToken();
        if ((token).numbering == null) return false;

        if ((token).numbering.isFirstNumbering()) {
            // Check next
            if (sequence.size() > indexInSequence + 1) {
                return (
                        NumberingNumber.isSuccedentOf(sequence.get(indexInSequence + 1).getToken().numbering,
                                (token).numbering)
                );
            }
        } else {
            // Check previous
            if (indexInSequence > 0) {
                return (
                        NumberingNumber.isSuccedentOf(token.numbering,
                                sequence.get(indexInSequence - 1).getToken().numbering)
                );
            }
        }
        return false;
    }
    public static boolean isPartOfList(RechtspraakElement[] sequence, int indexInSequence) {
        if (sequence[indexInSequence].numbering == null) return false;

        if (sequence[indexInSequence].numbering.isFirstNumbering()) {
            // Check next
            if (sequence.length > indexInSequence + 1) {
                return (
                        NumberingNumber.isSuccedentOf(sequence[indexInSequence + 1].numbering,
                                sequence[indexInSequence].numbering)
                );
            }
        } else {
            // Check previous
            if (indexInSequence > 0) {
                return (
                        NumberingNumber.isSuccedentOf(sequence[indexInSequence].numbering,
                                sequence[indexInSequence - 1].numbering)
                );
            }
        }
        return false;
    }

    public static class IsPartOfListFilter extends org.crf.crf.filters.Filter<RechtspraakElement, Label> {
        public final Label label;

        private IsPartOfListFilter(Label l) {
            label = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IsPartOfListFilter that = (IsPartOfListFilter) o;

            return label.equals(that.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }

    public static class IsPartOfListFeature extends CrfFeature<RechtspraakElement, Label> {
        private final Label label;

        private IsPartOfListFeature(Label label) {
            this.label = label;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(isPartOfList(sequence, indexInSequence));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IsPartOfListFeature that = (IsPartOfListFeature) o;

            return label.equals(that.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }
}