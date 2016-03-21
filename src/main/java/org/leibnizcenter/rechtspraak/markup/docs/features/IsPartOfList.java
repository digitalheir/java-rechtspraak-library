package org.leibnizcenter.rechtspraak.markup.docs.features;

import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.numbering.FullSectionNumber;
import org.leibnizcenter.rechtspraak.util.numbering.NumberingNumber;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by maarten on 2-3-16.
 */
public class IsPartOfList {
    public static final Map<Label, IsPartOfListFeature> features = new EnumMap<>(Label.class);
    public static final Map<Label, IsPartOfListFilter> filters = new EnumMap<>(Label.class);
    private static final int LIST_LOOKUP_LIMIT = 2;

    static {
        for (Label l : Label.values()) {
            features.put(l, new IsPartOfListFeature(l));
            filters.put(l, new IsPartOfListFilter(l));
        }
    }

    public static boolean isPartOfList(LabeledTokenList sequence, int indexInSequence) {
        RechtspraakElement token = sequence.get(indexInSequence).getToken();
        return token.numbering != null && token.numbering instanceof FullSectionNumber
                && (succedentFound(sequence, indexInSequence) || precedentFound(sequence, indexInSequence));
    }

    public static boolean isPartOfList(RechtspraakElement[] sequence, int indexInSequence) {
        RechtspraakElement token = sequence[indexInSequence];
        return token.numbering != null && token.numbering instanceof FullSectionNumber
                && (succedentFound(sequence, indexInSequence) || precedentFound(sequence, indexInSequence));
    }

    private static boolean precedentFound(RechtspraakElement[] sequence, int indexInSequence) {
        for (int i = 1; i <= LIST_LOOKUP_LIMIT; i++) {
            if (indexInSequence - i >= 0) {
                RechtspraakElement adjacentEl = sequence[indexInSequence - i];
                if (adjacentEl.numbering != null
                        && adjacentEl.numbering instanceof FullSectionNumber
                        && NumberingNumber.isSuccedentOf(sequence[indexInSequence].numbering,
                        adjacentEl.numbering)
                        && (!TitlePatterns.TitlesNormalizedMatchesHighConf.matchesAny(adjacentEl))) {
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
                if (adjacentEl.numbering != null
                        && adjacentEl.numbering instanceof FullSectionNumber
                        && NumberingNumber.isSuccedentOf(token.numbering,
                        adjacentEl.numbering)
                        && (!TitlePatterns.TitlesNormalizedMatchesHighConf.matchesAny(adjacentEl))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean succedentFound(RechtspraakElement[] sequence, int indexInSequence) {
        for (int i = 1; i <= LIST_LOOKUP_LIMIT; i++) {
            if (sequence.length > indexInSequence + i) {
                RechtspraakElement adjacentEl = sequence[indexInSequence + i];
                if (adjacentEl.numbering != null
                        && adjacentEl.numbering instanceof FullSectionNumber
                        && NumberingNumber.isSuccedentOf(adjacentEl.numbering,
                        sequence[indexInSequence].numbering)
                        && (!TitlePatterns.TitlesNormalizedMatchesHighConf.matchesAny(adjacentEl))) {
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
                if (adjacentEl.numbering != null
                        && adjacentEl.numbering instanceof FullSectionNumber
                        && NumberingNumber.isSuccedentOf(adjacentEl.numbering,
                        token.numbering)
                        && (!TitlePatterns.TitlesNormalizedMatchesHighConf.matchesAny(adjacentEl))) {
                    return true;
                }
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