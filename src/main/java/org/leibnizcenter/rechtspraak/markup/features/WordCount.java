package org.leibnizcenter.rechtspraak.markup.features;

import com.google.common.collect.Maps;
import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.Label;
import org.leibnizcenter.rechtspraak.markup.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.Labels;

import java.util.Map;

/**
 * Created by maarten on 5-3-16.
 */
public enum WordCount {
    LESS_THAN_5,
    LESS_THAN_10,
    LESS_THAN_15,
    AT_LEAST_15;

    public static final Map<WordCount, Map<Label, Feature>> features;
    public static final Map<WordCount, Map<Label, Filter>> filters;

    static {
        Map<WordCount, Map<Label, Feature>> featuresMap = Maps.newEnumMap(WordCount.class);
        Map<WordCount, Map<Label, Filter>> filtersMap = Maps.newEnumMap(WordCount.class);
        for (WordCount wc : WordCount.values()) {
            Map<Label, Feature> featureMap = Maps.newEnumMap(Label.class);
            Labels.set.forEach(label -> featureMap.put(label, new Feature(label, wc)));
            featuresMap.put(wc, featureMap);

            Map<Label, Filter> filterMap = Maps.newEnumMap(Label.class);
            Labels.set.forEach(label -> filterMap.put(label, new Filter(label, wc)));
            filtersMap.put(wc, filterMap);
        }
        features = Maps.immutableEnumMap(featuresMap);
        filters = Maps.immutableEnumMap(filtersMap);
    }

    public static boolean isTrue(WordCount wordCount, RechtspraakElement e) {
        boolean isTrue = false;
        switch (wordCount) {
            case LESS_THAN_5:
                isTrue = e.wordCount < 5;
                break;
            case LESS_THAN_10:
                isTrue = e.wordCount < 10;
                break;
            case LESS_THAN_15:
                isTrue = e.wordCount < 15;
                break;
            case AT_LEAST_15:
                isTrue = e.wordCount >= 15;
                break;
        }
        return isTrue;
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final WordCount wordCount;
        private final Label label;

        public Feature(Label l, WordCount wordCount) {
            this.label = l;
            this.wordCount = wordCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Feature feature = (Feature) o;

            return wordCount.equals(feature.wordCount) && label == feature.label;
        }

        @Override
        public int hashCode() {
            int result = wordCount.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            boolean isTrue = isTrue(wordCount, sequence[indexInSequence]);
            return Doubles.asDouble(isTrue);
        }

    }

    public static class Filter extends org.crf.crf.filters.Filter<RechtspraakElement, Label> {
        private final WordCount wordCount;
        private final Label label;

        public Filter(Label l, WordCount wordCount) {
            this.label = l;
            this.wordCount = wordCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filter feature = (Filter) o;
            return wordCount.equals(feature.wordCount) && label == feature.label;
        }

        @Override
        public int hashCode() {
            int result = wordCount.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }
    }
}
