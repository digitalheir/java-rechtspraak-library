package org.crf.postagging.postaggers.crf.features;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.crf.crf.filters.CrfFilteredFeature;
import org.crf.crf.filters.TwoTagsFilter;
import org.crf.crf.run.CrfFeatureGenerator;
import org.crf.utilities.CrfException;
import org.crf.utilities.TaggedToken;

/**
 * Generates the standard set of CRF features for part-of-speech tagging.
 * This standard set is:
 * <OL>
 * <LI>For each token and tag - a feature that models that that token is assigned that tag.</LI>
 * <LI>For each tag that follows a preceding tag - a feature that models this tag transition.</LI>
 * </OL>
 *
 * @author Asher Stern
 *         Date: Nov 10, 2014
 */
public class StandardFeatureGenerator extends CrfFeatureGenerator<String, String> {

    public StandardFeatureGenerator(Iterable<? extends List<? extends TaggedToken<String, String>>> corpus, Set<String> tags) {
        super(corpus, tags);
    }

    @Override
    public void generateFeatures() {
        setFilteredFeatures = new LinkedHashSet<>();
        addTokenAndTagFeatures();
        addTagTransitionFeatures();
    }

    public Set<CrfFilteredFeature<String, String>> getFeatures() {
        if (null == setFilteredFeatures) {
            throw new CrfException("Features were not generated.");
        }
        return setFilteredFeatures;
    }


    private void addTokenAndTagFeatures() {
        for (List<? extends TaggedToken<String, String>> sentence : corpus) {
            setFilteredFeatures.addAll(
                    sentence.stream().map(taggedToken -> new CrfFilteredFeature<>(
                            new CaseInsensitiveTokenAndTagFeature(taggedToken.getToken(), taggedToken.getTag()),
                            new CaseInsensitiveTokenAndTagFilter(taggedToken.getToken(), taggedToken.getTag()),
                            true
                    )).collect(Collectors.toList()));
        }
    }

    private void addTagTransitionFeatures() {
        for (String tag : tags) {
            setFilteredFeatures.add(
                    new CrfFilteredFeature<>(
                            new TagTransitionFeature(null, tag),
                            new TwoTagsFilter<>(tag, null),
                            true)
            );

            for (String previousTag : tags) {
                setFilteredFeatures.add(
                        new CrfFilteredFeature<>(
                                new TagTransitionFeature(previousTag, tag),
                                new TwoTagsFilter<>(tag, previousTag),
                                true)
                );
            }
        }
    }


    protected Set<CrfFilteredFeature<String, String>> setFilteredFeatures = null;
}
