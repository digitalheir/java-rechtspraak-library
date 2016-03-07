package org.leibnizcenter.rechtspraak.markup;

import com.google.common.collect.Lists;
import org.crf.crf.CrfFeature;
import org.crf.crf.CrfModel;
import org.crf.crf.filters.CrfFilteredFeature;
import org.crf.crf.filters.Filter;
import org.crf.crf.filters.FilterFactory;
import org.crf.crf.run.*;
import org.crf.utilities.TaggedToken;
import org.leibnizcenter.rechtspraak.util.Doubles;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * :p
 * Created by maarten on 28-2-16.
 */
public final class SampleCrf extends ExampleMain {

    private static final FilterFactory<String, String> FILTER_FACTORY = (FilterFactory<String, String>)
            (sequence, tokenIndex, currentTag, previousTag) -> {
                Set<Filter<String, String>> s = new HashSet<>();
                s.add(new SampleCrfFeatureGenerator.OneIf(sequence[tokenIndex]));
                return s;
            };

    public static void main(String[] a) {
        SampleCrf app = new SampleCrf();
        app.run();
    }

    public List<List<? extends TaggedToken<String, String>>> loadCorpusWhereTokensAndTagsAreStrings() {
        TaggedToken<String, String> a = new TaggedToken<>("A", "A_AFTER_X");
        TaggedToken<String, String> b = new TaggedToken<>("B", "B_AFTER_A");
        TaggedToken<String, String> c = new TaggedToken<>("C", "C_AFTER_B");

        List<TaggedToken<String, String>> l1 = Lists.newArrayList();
        l1.add(a);
        l1.add(b);
        l1.add(c);

        List<TaggedToken<String, String>> l2 = Lists.newArrayList(a, a, b, b, c, a, b);
        List<TaggedToken<String, String>> l8 = Lists.newArrayList(a, b, b, c, a);
        List<TaggedToken<String, String>> l7 = Lists.newArrayList(a, a, b, b, c, a, a, a, b, c);
        List<TaggedToken<String, String>> l3 = Lists.newArrayList(a, a, b, b, c);
        List<TaggedToken<String, String>> l4 = Lists.newArrayList(a, a, a, b, c, a, a);
        List<TaggedToken<String, String>> l5 = Lists.newArrayList(a, a, b, c, c, c, c, a, b, c, a, b, c, a, a, a, a, b, c, c, c, a, b, c, a, b, c);


        List<List<? extends TaggedToken<String, String>>> l = Lists.newArrayList();
        l.add(l1);
        l.add(l2);
        l.add(l3);
        l.add(l4);
        l.add(l5);
        l.add(l7);
        l.add(l8);

        return l;
    }

    public CrfFeatureGenerator<String, String> createFeatureGeneratorForGivenCorpus(Iterable<? extends List<? extends TaggedToken<String, String>>> corpus, Set<String> tags) {
        return new SampleCrfFeatureGenerator(corpus, tags);
    }

    public FilterFactory<String, String> createFilterFactory() {
        return FILTER_FACTORY;
    }

    @Override
    public void run() {
        // Load a corpus into the memory
        List<List<? extends TaggedToken<String, String>>> corpus = loadCorpusWhereTokensAndTagsAreStrings();

        // Create trainer factory
        CrfTrainerFactory<String, String> trainerFactory = new CrfTrainerFactory<>();

        // Create trainer
        CrfTrainer<String, String> trainer = trainerFactory.createTrainer(
                corpus,
                this::createFeatureGeneratorForGivenCorpus,
                createFilterFactory()
        );

        // Run training with the loaded corpus.
        trainer.train(corpus);

        // Get the model
        CrfModel<String, String> crfModel = trainer.getLearnedModel();

        // Save the model into the disk.
        File file = new File("example.ser");
        save(crfModel, file);

        ////////

        // Later... Load the model from the disk
        //noinspection unchecked
        crfModel = (CrfModel<String, String>) load(file);

        // Create a CrfInferencePerformer, to find tags for test data
        CrfInferencePerformer<String, String> inferencePerformer = new CrfInferencePerformer<>(crfModel);

        // Test:
        List<String> test = Arrays.asList("A B C A B A".split("\\s+"));
        List<TaggedToken<String, String>> result = inferencePerformer.tagSequence(test);

        // Print the result:
        for (TaggedToken<String, String> taggedToken : result) {
            System.out.println("Tag for: " + taggedToken.getToken() + " is " + taggedToken.getTag());
        }
    }

    public static class SampleCrfFeatureGenerator extends CrfFeatureGenerator<String, String> {
        private static final CheckEquals FEATURE_IS_A = new CheckEquals("A");
        private static final CheckEquals FEATURE_IS_B = new CheckEquals("B");
        private static final CheckEquals FEATURE_IS_C = new CheckEquals("C");

        private HashSet<CrfFilteredFeature<String, String>> features;

        public SampleCrfFeatureGenerator(Iterable<? extends List<? extends TaggedToken<String, String>>> corpus, Set<String> tags) {
            super(corpus, tags);
        }

        @Override
        public void generateFeatures() {
            this.features = new HashSet<>();

            CrfFilteredFeature<String, String> filteredFeature;

            CrfFeature<String, String> featureA = FEATURE_IS_A;
            Filter<String, String> filterA = new OneIf("A");
            filteredFeature = new CrfFilteredFeature<>(featureA, filterA, false);
            features.add(filteredFeature);

            CrfFeature<String, String> featureB = FEATURE_IS_B;
            Filter<String, String> filterB = new OneIf("B");
            filteredFeature = new CrfFilteredFeature<>(featureB, filterB, false);
            features.add(filteredFeature);

            features.add(filteredFeature);

            CrfFeature<String, String> featureC = FEATURE_IS_C;
            Filter<String, String> filterC = new OneIf("C");
            filteredFeature = new CrfFilteredFeature<>(featureC, filterC, false);
            features.add(filteredFeature);

            CrfFeature<String, String> isIncr;
// = new IsIncremental();
//            filteredFeature = new CrfFilteredFeature<>(isIncr, EXACTLY_THIS, false);

            isIncr = new Follows("A");
            filteredFeature = new CrfFilteredFeature<>(isIncr, filterA, false);
            features.add(filteredFeature);

            isIncr = new Follows("B");
            filteredFeature = new CrfFilteredFeature<>(isIncr, filterB, false);
            features.add(filteredFeature);

            isIncr = new Follows("C");
            filteredFeature = new CrfFilteredFeature<>(isIncr, filterC, false);
            features.add(filteredFeature);
        }

        @Override
        public Set<CrfFilteredFeature<String, String>> getFeatures() {
            return features;
        }

        private static class IsIncremental extends CrfFeature<String, String> {
            public IsIncremental() {
            }

            @Override
            public double value(String[] sequence, int indexInSequence, String currentTag, String previousTag) {
                return Doubles.asDouble(
                        isIncremental(currentTag, previousTag)
                );
            }

            public static boolean isIncremental(String currentTag, String previousTag) {
                return "A".equals(currentTag) && "C".equals(previousTag)
                        || "B".equals(currentTag) && "A".equals(previousTag)
                        || "C".equals(currentTag) && "B".equals(previousTag);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                CheckEquals that = (CheckEquals) o;
                return true;
            }

            @Override
            public int hashCode() {
                return -12;
            }
        }

        private static class CheckEquals extends CrfFeature<String, String> {
            private final String checkFor;

            public CheckEquals(String checkFor) {
                if (checkFor == null) throw new NullPointerException();
                this.checkFor = checkFor;
            }

            @Override
            public double value(String[] sequence, int indexInSequence, String currentTag, String previousTag) {
                return sequence[indexInSequence].equals(checkFor) ? 1.0 : 0.0;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                CheckEquals that = (CheckEquals) o;
                return checkFor.equals(that.checkFor);

            }

            @Override
            public int hashCode() {
                return checkFor.hashCode();
            }
        }

        private static class OneIf extends Filter<String, String> {
            private final String checkFor;

            public OneIf(String checkFor) {
                this.checkFor = checkFor;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                OneIf that = (OneIf) o;
                return checkFor.equals(that.checkFor);
            }

            @Override
            public int hashCode() {
                return checkFor.hashCode();
            }
        }
    }

}
