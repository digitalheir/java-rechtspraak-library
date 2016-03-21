package org.leibnizcenter.rechtspraak.markup.docs.features;

import org.crf.crf.CrfFeature;
import org.crf.crf.filters.Filter;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.Doubles;

import java.util.EnumMap;

/**
 * Created by maarten on 2-3-16.
 */
public final class HasLabel {
    public static final EnumMap<Label, Feature> features = new EnumMap<>(Label.class);
    public static final EnumMap<Label, HasLabelFilter> filters = new EnumMap<>(Label.class);

    static {
        for (Label l : Label.values()) {
            features.put(l, new Feature(l));
            filters.put(l, new HasLabelFilter(l));
        }
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final Label label;

        private Feature(Label label) {
            assert label != null;
            this.label = label;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(currentTag.equals(label));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Feature that = (Feature) o;
            return label.equals(that.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }

    public static class HasLabelFilter extends Filter<RechtspraakElement, Label> {
        private final Label checkFor;

        private HasLabelFilter(Label label) {
            this.checkFor = label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HasLabelFilter that = (HasLabelFilter) o;

            return checkFor != null ? checkFor.equals(that.checkFor) : that.checkFor == null;

        }

        @Override
        public int hashCode() {
            return checkFor != null ? checkFor.hashCode() : 0;
        }
    }
}
