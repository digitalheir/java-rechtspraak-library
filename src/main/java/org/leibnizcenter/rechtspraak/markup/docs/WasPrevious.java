package org.leibnizcenter.rechtspraak.markup.docs;

import com.google.common.collect.Maps;
import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.Labels;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by maarten on 29-2-16.
 */
public class WasPrevious {
    public static final Map<Label, Feature> features;
    public static final Map<Label, Filter> filters;

    static {
        Map<Label, Feature> featurez = new EnumMap<>(Label.class);
        Map<Label, Filter> filterz = new EnumMap<>(Label.class);

        for (Label l : Labels.set) {
            featurez.put(l, new Feature(l));
        }

        features = Maps.immutableEnumMap(featurez);
        filters = Maps.immutableEnumMap(filterz);
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final Label label;

        private Feature(Label l) {
            this.label = l;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(

                    label.equals(previousTag));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Feature feature = (Feature) o;

            return label == feature.label;

        }

        @Override
        public int hashCode() {
            return label != null ? label.hashCode() : 0;
        }
    }

    public static class Filter extends org.crf.crf.filters.Filter<RechtspraakElement, Label> {
        private final Label label;

        private Filter(Label l) {
            this.label = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filter filter = (Filter) o;

            return label == filter.label;

        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }
}
