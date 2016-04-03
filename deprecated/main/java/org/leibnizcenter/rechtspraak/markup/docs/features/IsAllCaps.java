package org.leibnizcenter.rechtspraak.markup.docs.features;

import deprecated.org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.Labels;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Maarten on 3/7/2016.
 */
public class IsAllCaps {
    public static final Map<Label, Feature> features = new EnumMap<>(Label.class);
    public static final Map<Label, Filter> filters = new EnumMap<>(Label.class);
    public static final Pattern ALL_CAPS = Pattern.compile("^[\\p{Lu}0-9 ]*\\p{Lu}+[\\p{Lu}0-9 ]*$");

    static {
        for (Label l : Labels.set) {
            features.put(l, new Feature(l));
            filters.put(l, new Filter(l));
        }
    }

    public static String unspace(String normalizedText) {
        return normalizedText.replaceAll("\\s", "");
    }

    public static boolean isSpaced(String normalizedText) {
        return ALL_CAPS.matcher(normalizedText).matches();
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final Label label;

        public Feature(Label l) {
            this.label = l;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(sequence[indexInSequence].isSpaced);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Feature feature = (Feature) o;

            return label.equals(feature.label);
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

            Filter feature = (Filter) o;

            return label.equals(feature.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }
}
