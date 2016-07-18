package org.leibnizcenter.rechtspraak.markup.docs.features;

import deprecated.org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.util.Doubles;

/**
 * Created by maarten on 2-3-16.
 */
public enum InfoAsFirstX {
    FIVE(5), TEN(10), FIFTEEN(15);
    public final Filter filter;
    public final Feature feature;
    private int x;

    InfoAsFirstX(int x) {
        this.x = x;
        this.filter = new Filter(x);
        this.feature = new Feature(x);
    }

    public int getX() {
        return x;
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final int x;

        public Feature(int i) {
            this.x = i;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(smallerThan(indexInSequence));
        }

        private boolean smallerThan(int indexInSequence) {
            return indexInSequence < x;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Feature that = (Feature) o;

            return x == that.x;

        }

        @Override
        public int hashCode() {
            return x;
        }
    }

    public static class Filter extends deprecated.org.crf.crf.filters.Filter<RechtspraakElement, Label> {
        private final int x;

        public Filter(int i) {
            this.x = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filter that = (Filter) o;

            return x == that.x;

        }

        @Override
        public int hashCode() {
            return x;
        }
    }
}
