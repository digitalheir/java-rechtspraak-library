package org.leibnizcenter.rechtspraak.markup.features;

import com.google.common.collect.Maps;
import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.Label;
import org.leibnizcenter.rechtspraak.markup.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.numbering.FullSectionNumber;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by maarten on 4-3-16.
 */
public class HasFullSectionNumbering {
    public static final Map<Label, Filter> filters;
    public static final Map<Label, Feature> features;

    static {
        EnumMap<Label, Filter> filterMap = new EnumMap<>(Label.class);
        EnumMap<Label, Feature> featureMap = new EnumMap<>(Label.class);
        for (Label l : Label.values()) {
            filterMap.put(l, new Filter(l));
            featureMap.put(l, new Feature(l));
        }
        filters = Maps.immutableEnumMap(filterMap);
        features = Maps.immutableEnumMap(featureMap);
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final Label label;

        public Feature(Label l) {
            this.label = l;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(sequence[indexInSequence].numbering instanceof FullSectionNumber);
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

    public static class Filter extends org.crf.crf.filters.Filter<RechtspraakElement, Label> {
        private final Label label;

        public Filter(Label l) {
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
