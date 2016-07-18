package org.leibnizcenter.rechtspraak.markup.docs.features;

import com.google.common.collect.Maps;
import deprecated.org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.util.Doubles;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by maarten on 4-3-16.
 */
public class HasNumberingTerminal {
    public static final Map<Label, Filter> filters;
    public static final Map<Label, Feature> features;

    static {
        EnumMap<Label, Feature> featurez = new EnumMap<>(Label.class);
        EnumMap<Label, Filter> filterz = new EnumMap<>(Label.class);
        for (Label l : Label.values()) {
            filterz.put(l, new Filter(l));
            featurez.put(l, new Feature(l));
        }
        filters = Maps.immutableEnumMap(filterz);
        features = Maps.immutableEnumMap(featurez);
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final Label label;

        public Feature(Label l) {
            this.label = l;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(sequence[indexInSequence].numbering.getTerminal() != null);
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

    public static class Filter extends deprecated.org.crf.crf.filters.Filter<RechtspraakElement, Label> {
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
