package org.leibnizcenter.rechtspraak.markup.docs.features;

import deprecated.org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.util.Doubles;

import java.util.regex.Pattern;

/**
 * Created by maarten on 2-3-16.
 */
public class TokenMatch {
    public static class Filter extends deprecated.org.crf.crf.filters.Filter<RechtspraakElement, Label> {
        public final Pattern pattern;
        public final Label label;

        public Filter(Label l, Pattern s) {
            this.label = l;
            this.pattern = s;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Feature feature = (Feature) o;

            return pattern.equals(feature.pattern) && label == feature.label;

        }

        @Override
        public int hashCode() {
            int result = pattern.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        /**
         * Regex pattern
         */
        private final Pattern pattern;
        private final Label label;

        public Feature(Label l, Pattern pattern) {
            assert pattern != null;
            this.label = l;
            this.pattern = pattern;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(label.equals(currentTag) && pattern.matcher(sequence[indexInSequence].normalizedText).matches());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Feature feature = (Feature) o;

            return pattern.equals(feature.pattern) && label == feature.label;

        }

        @Override
        public int hashCode() {
            int result = pattern.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }
    }
}
