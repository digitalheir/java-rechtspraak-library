package org.leibnizcenter.rechtspraak.markup.docs;

import org.crf.crf.CrfFeature;
import org.crf.crf.filters.CrfFilteredFeature;
import org.crf.crf.run.CrfFeatureGenerator;
import org.crf.utilities.TaggedToken;
import org.leibnizcenter.rechtspraak.markup.docs.features.*;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.TextBlockInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by maarten on 29-2-16.
 */
public class RechtspraakFeatureGenerator extends CrfFeatureGenerator<RechtspraakElement, Label> {
    private Set<CrfFilteredFeature<RechtspraakElement, Label>> features;

    public RechtspraakFeatureGenerator(Iterable<? extends List<? extends TaggedToken<RechtspraakElement, Label>>> corpus,
                                       Set<Label> tags) {
        super(corpus, tags);
    }

    @Override
    public void generateFeatures() {
        this.features = new HashSet<>();

        // transition probabilities
        for (Label from : Label.values()) {
            for (Label to : Label.values()) {
                if (to != Label.NULL) {
                    // NULL is only ever used as a start tag
                    CrfFeature<RechtspraakElement, Label> feature = new TransitionFeature(from, to);
                    features.add(
                            new CrfFilteredFeature<>(feature,
                                    Label.getTransitionFilters(from, to), true)
                    );
                }
            }
        }


        // Whether this is an info tag as one of the first X elements
        for (InfoAsFirstX i : InfoAsFirstX.values()) {
            features.add(
                    new CrfFilteredFeature<>(i.feature, i.filter, true)
            );
        }

        // Whether this is a very likely title tag following info
        features.add(
                new CrfFilteredFeature<>(
                        FirstSectionTitleAfterInfo.feature,
                        FirstSectionTitleAfterInfo.filter,
                        true)
        );

//        // Whether this is a section title that looks like something we've seen a lot before
//        for (NamePatterns p : NamePatterns.values()) {
//            features.add(
//                    new CrfFilteredFeature<>(
//                            NamePatterns.features.get(p).get(Label.SECTION_TITLE),
//                            NamePatterns.filters.get(p).get(Label.SECTION_TITLE),
//                            true
//                    ));
//        }

        for (Label label : Label.values()) {
            // Label probabilities
            //  features.add(
            //          new CrfFilteredFeature<>(
            //                  HasLabel.features.get(label),
            //                  HasLabel.filters.get(label),
            //                  true)
            //  );

            // Relative position in text
            for (Quartile q : Quartile.values()) {
                CrfFeature<RechtspraakElement, Label> feature = q.getFeature(label);
                features.add(
                        new CrfFilteredFeature<>(feature, q.getFilter(label), true)
                );
            }


            for (WordCount wc : WordCount.values()) {
                features.add(
                        new CrfFilteredFeature<>(
                                WordCount.features.get(wc).get(label),
                                WordCount.filters.get(wc).get(label),
                                true
                        )
                );
            }

            // Token values
//            features.addAll(
//                    TitlesEncountered.encountered.stream()
//                            .map(p -> new CrfFilteredFeature<>(p.features.get(label), p.filters.get(label), true))
//                            .collect(Collectors.toSet())
//            );

            features.add(
                    new CrfFilteredFeature<>(
                            TextBlockInfo.Space.features.get(label),
                            TextBlockInfo.Space.filters.get(label),
                            true
                    )
            );
            features.add(
                    new CrfFilteredFeature<>(
                            IsAllCaps.features.get(label),
                            IsAllCaps.filters.get(label),
                            true
                    )
            );
        }

        ////
        // Numbering features
        ////
        for (Label label : Label.values()) {
            features.add(
                    new CrfFilteredFeature<>(
                            HasNumbering.features.get(label),
                            HasNumbering.filters.get(label),
                            true)
            );
            features.add(new CrfFilteredFeature<>(
                    HasNumberingTerminal.features.get(label),
                    HasNumberingTerminal.filters.get(label),
                    true
            ));
        }
    }

    @Override
    public Set<CrfFilteredFeature<RechtspraakElement, Label>> getFeatures() {
        return features;
    }


    private static class IsIncremental extends CrfFeature<String, String> {
        public IsIncremental() {
        }

        public static boolean isIncremental(String currentTag, String previousTag) {
            return "A".equals(currentTag) && "C".equals(previousTag)
                    || "B".equals(currentTag) && "A".equals(previousTag)
                    || "C".equals(currentTag) && "B".equals(previousTag);
        }

        @Override
        public double value(String[] sequence, int indexInSequence, String currentTag, String previousTag) {
            return Doubles.asDouble(
                    isIncremental(currentTag, previousTag)
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return -12;
        }
    }

}
