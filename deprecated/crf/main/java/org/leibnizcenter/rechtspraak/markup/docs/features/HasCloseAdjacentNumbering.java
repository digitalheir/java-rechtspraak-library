package org.leibnizcenter.rechtspraak.markup.docs.features;

import deprecated.org.crf.crf.CrfFeature;
import deprecated.org.crf.crf.filters.Filter;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.util.Doubles;
import org.leibnizcenter.util.numbering.NumberingNumber;

/**
 * Created by maarten on 2-3-16.
 */
public class HasCloseAdjacentNumbering {
    private static final int LIST_LOOKUP_LIMIT = 2;

    public static boolean value(LabeledTokenList sequence, int indexInSequence) {
        RechtspraakElement token = sequence.get(indexInSequence).getToken();
        return token.numbering != null
                && (succedentFound(sequence, indexInSequence) || precedentFound(sequence, indexInSequence));
    }

    public static boolean value(RechtspraakElement[] sequence, int indexInSequence) {
        RechtspraakElement token = sequence[indexInSequence];
        return token.numbering != null
                && (succedentFound(sequence, indexInSequence) || precedentFound(sequence, indexInSequence));
    }

    private static boolean precedentFound(RechtspraakElement[] sequence, int indexInSequence) {
        RechtspraakElement el = sequence[indexInSequence];
        for (int i = 1; i <= LIST_LOOKUP_LIMIT; i++) {
            if (indexInSequence - i >= 0) {
                RechtspraakElement adjacentEl = sequence[indexInSequence - i];
                if (isAdjacentNumber(el, adjacentEl)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean precedentFound(LabeledTokenList sequence, int indexInSequence) {
        RechtspraakElement token = sequence.get(indexInSequence).getToken();
        for (int i = 1; i <= LIST_LOOKUP_LIMIT; i++) {
            if (indexInSequence - i >= 0) {
                RechtspraakElement adjacentEl = sequence.get(indexInSequence - i).getToken();
                if (isAdjacentNumber(token, adjacentEl)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean succedentFound(RechtspraakElement[] sequence, int indexInSequence) {
        RechtspraakElement token = sequence[indexInSequence];
        for (int i = 1; i <= LIST_LOOKUP_LIMIT; i++) {
            if (sequence.length > indexInSequence + i) {
                RechtspraakElement adjacentEl = sequence[indexInSequence + i];
                if (isAdjacentNumber(adjacentEl, token)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean succedentFound(LabeledTokenList sequence, int indexInSequence) {
        RechtspraakElement token = sequence.get(indexInSequence).getToken();
        for (int i = 1; i <= LIST_LOOKUP_LIMIT; i++) {
            if (sequence.size() > indexInSequence + i) {
                RechtspraakElement adjacentEl = sequence.get(indexInSequence + i).getToken();
                if (isAdjacentNumber(adjacentEl, token)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isAdjacentNumber(RechtspraakElement later, RechtspraakElement earlier) {
        return earlier.numbering != null
                && !earlier.getTagName().endsWith("list")
                && !earlier.getTagName().equals("li")
                && !earlier.getTagName().equals("item")
                && !earlier.getTagName().startsWith("footnote")
                && NumberingNumber.isSuccedentOf(later.numbering, earlier.numbering);
    }

    public static class IsPartOfListFilter extends Filter<RechtspraakElement, Label> {
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
            return Doubles.asDouble(HasCloseAdjacentNumbering.value(sequence, indexInSequence));
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