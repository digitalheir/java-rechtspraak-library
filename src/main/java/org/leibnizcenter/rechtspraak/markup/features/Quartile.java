package org.leibnizcenter.rechtspraak.markup.features;

import org.crf.crf.CrfFeature;
import org.crf.crf.filters.Filter;
import org.leibnizcenter.rechtspraak.markup.Label;
import org.leibnizcenter.rechtspraak.markup.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.Doubles;

import java.security.InvalidParameterException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by maarten on 2-3-16.
 */
public enum Quartile {
    FIRST(-Double.MAX_VALUE, 0.25), SECOND(0.25, 0.50), THIRD(0.50, 0.75), FOURTH(0.75, Double.MAX_VALUE);

    private final Map<Label, QuartileFilter> filters = new EnumMap<>(Label.class);
    private final Map<Label, QuartileFeature> features = new EnumMap<>(Label.class);

    private double rangeMin;
    private double rangeMax;

    Quartile(double rangeMin, double rangeMax) {
        for (Label l : Label.values()) {
            filters.put(l, new QuartileFilter(this, l));
            features.put(l, new QuartileFeature(this, l));
        }
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    /**
     * @param num number between 0 and 1
     */
    public static Quartile isInQuartile(double num) {
        for (Quartile q : Quartile.values()) {
            if (q.isExactlyInQuartile(num)) {
                return q;
            }
        }
        throw new Error("Could not find quartile to fit " + num + " in. This is a bug.");
    }

    public QuartileFilter getFilter(Label l) {
        QuartileFilter filter = filters.get(l);
        if (filter == null) throw new NullPointerException();
        return filter;
    }

    public QuartileFeature getFeature(Label l) {
        QuartileFeature feature = features.get(l);
        if (feature == null) throw new NullPointerException();
        return feature;
    }

    /**
     * @param num number between 0 and 1
     */
    public boolean isExactlyInQuartile(double num) {
        if (num > 1 || num < 0) {
            throw new InvalidParameterException(num + " should be between 0.0 and " + 1.0);
        }
        return num > rangeMin && num <= rangeMax;
    }

    public static class QuartileFeature extends CrfFeature<RechtspraakElement, Label> {
        private final Quartile q;
        private final Label l;

        private QuartileFeature(Quartile q, Label l) {
            this.q = q;
            this.l = l;

        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            if (sequence.length > 0) {
                return Doubles.asDouble(q.isExactlyInQuartile(((double) (indexInSequence + 1)) / ((double) sequence.length)));
            } else {
                return 0.0;
            }
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            QuartileFilter that = (QuartileFilter) o;

            if (q != that.q) return false;
            return l == that.l;

        }

        @Override
        public int hashCode() {
            int result = q.hashCode();
            result = 31 * result + l.hashCode();
            return result;
        }
    }

    public static class QuartileFilter extends Filter<RechtspraakElement, Label> {
        private final Quartile q;
        private final Label l;

        private QuartileFilter(Quartile q, Label l) {
            this.q = q;
            this.l = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            QuartileFilter that = (QuartileFilter) o;

            if (q != that.q) return false;
            return l == that.l;

        }

        @Override
        public int hashCode() {
            int result = q.hashCode();
            result = 31 * result + l.hashCode();
            return result;
        }
    }
}
