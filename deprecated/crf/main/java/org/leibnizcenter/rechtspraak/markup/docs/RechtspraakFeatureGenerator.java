//package org.leibnizcenter.rechtspraak.markup.docs;
//
//import CrfFeature;
//import CrfFilteredFeature;
//import CrfFeatureGenerator;
//import TaggedToken;
//import org.leibnizcenter.rechtspraak.markup.docs.mostlikelytreefromlist.*;
//import org.leibnizcenter.util.Doubles;
//import org.leibnizcenter.util.TextBlockInfo;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * Created by maarten on 29-2-16.
// */
//public class RechtspraakFeatureGenerator extends CrfFeatureGenerator<RechtspraakElement, Label> {
//    private Set<CrfFilteredFeature<RechtspraakElement, Label>> mostlikelytreefromlist;
//
//    public RechtspraakFeatureGenerator(Iterable<? extends List<? extends TaggedToken<RechtspraakElement, Label>>> corpus,
//                                       Set<Label> tags) {
//        super(corpus, tags);
//    }
//
//    @Override
//    public void generateFeatures() {
//        this.mostlikelytreefromlist = new HashSet<>();
//
//        // transition probabilities
//        for (Label from : Label.values()) {
//            for (Label to : Label.values()) {
//                if (to != Label.NULL) {
//                    // NULL is only ever used as a start tag
//                    CrfFeature<RechtspraakElement, Label> feature = new TransitionFeature(from, to);
//                    mostlikelytreefromlist.add(
//                            new CrfFilteredFeature<>(feature,
//                                    Label.getTransitionFilters(from, to), true)
//                    );
//                }
//            }
//        }
//
//
//        // Whether this is an info tag as one of the first X elements
//        for (InfoAsFirstX i : InfoAsFirstX.values()) {
//            mostlikelytreefromlist.add(
//                    new CrfFilteredFeature<>(i.feature, i.filter, true)
//            );
//        }
//
//        // Whether this is a very likely title tag following info
//        mostlikelytreefromlist.add(
//                new CrfFilteredFeature<>(
//                        FirstSectionTitleAfterInfo.feature,
//                        FirstSectionTitleAfterInfo.filter,
//                        true)
//        );
//
////        // Whether this is a section title that looks like something we've seen a lot before
////        for (NamePatterns p : NamePatterns.values()) {
////            mostlikelytreefromlist.add(
////                    new CrfFilteredFeature<>(
////                            NamePatterns.mostlikelytreefromlist.get(p).get(Label.SECTION_TITLE),
////                            NamePatterns.filters.get(p).get(Label.SECTION_TITLE),
////                            true
////                    ));
////        }
//
//        for (Label label : Label.values()) {
//            // Label probabilities
//            //  mostlikelytreefromlist.add(
//            //          new CrfFilteredFeature<>(
//            //                  HasLabel.mostlikelytreefromlist.get(label),
//            //                  HasLabel.filters.get(label),
//            //                  true)
//            //  );
//
//            // Relative position in text
//            for (Quartile q : Quartile.values()) {
//                CrfFeature<RechtspraakElement, Label> feature = q.getFeature(label);
//                mostlikelytreefromlist.add(
//                        new CrfFilteredFeature<>(feature, q.getFilter(label), true)
//                );
//            }
//
//
//            for (WordCount wc : WordCount.values()) {
//                mostlikelytreefromlist.add(
//                        new CrfFilteredFeature<>(
//                                WordCount.mostlikelytreefromlist.get(wc).get(label),
//                                WordCount.filters.get(wc).get(label),
//                                true
//                        )
//                );
//            }
//
//            // Token values
////            mostlikelytreefromlist.addAll(
////                    TitlesEncountered.encountered.stream()
////                            .map(p -> new CrfFilteredFeature<>(p.mostlikelytreefromlist.get(label), p.filters.get(label), true))
////                            .collect(Collectors.toSet())
////            );
//
//            mostlikelytreefromlist.add(
//                    new CrfFilteredFeature<>(
//                            TextBlockInfo.Space.mostlikelytreefromlist.get(label),
//                            TextBlockInfo.Space.filters.get(label),
//                            true
//                    )
//            );
//            mostlikelytreefromlist.add(
//                    new CrfFilteredFeature<>(
//                            IsAllCaps.mostlikelytreefromlist.get(label),
//                            IsAllCaps.filters.get(label),
//                            true
//                    )
//            );
//        }
//
//        ////
//        // Numbering mostlikelytreefromlist
//        ////
//        for (Label label : Label.values()) {
//            mostlikelytreefromlist.add(
//                    new CrfFilteredFeature<>(
//                            HasNumbering.mostlikelytreefromlist.get(label),
//                            HasNumbering.filters.get(label),
//                            true)
//            );
//            mostlikelytreefromlist.add(new CrfFilteredFeature<>(
//                    HasNumberingTerminal.mostlikelytreefromlist.get(label),
//                    HasNumberingTerminal.filters.get(label),
//                    true
//            ));
//        }
//    }
//
//    @Override
//    public Set<CrfFilteredFeature<RechtspraakElement, Label>> getFeatures() {
//        return mostlikelytreefromlist;
//    }
//
//
//    private static class IsIncremental extends CrfFeature<String, String> {
//        public IsIncremental() {
//        }
//
//        public static boolean isIncremental(String currentTag, String previousTag) {
//            return "A".equals(currentTag) && "C".equals(previousTag)
//                    || "B".equals(currentTag) && "A".equals(previousTag)
//                    || "C".equals(currentTag) && "B".equals(previousTag);
//        }
//
//        @Override
//        public double value(String[] sequence, int indexInSequence, String currentTag, String previousTag) {
//            return Doubles.asDouble(
//                    isIncremental(currentTag, previousTag)
//            );
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            return true;
//        }
//
//        @Override
//        public int hashCode() {
//            return -12;
//        }
//    }
//
//}
