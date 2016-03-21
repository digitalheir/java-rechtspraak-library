package org.leibnizcenter.rechtspraak.markup.docs.features;

import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.Doubles;

/**
 * Created by Maarten on 3/7/2016.
 */
public class FirstSectionTitleAfterInfo {
    public static final Filter filter = new Filter();
    public static final Feature feature = new Feature();

    /**
     * Whether the element starts with 1 or Procesverloop
     *
     * @param rechtspraakElement
     * @return Whether this element is very likely to be the first title in the sequence
     */
    public static boolean isVeryLikelyFirstTitle(RechtspraakElement rechtspraakElement, Label currentTag, Label previousTag) {
        return Label.INFO.equals(previousTag)
                && currentTag.equals(Label.SECTION_TITLE)
                && (rechtspraakElement.numbering != null && rechtspraakElement.numbering.mainNum() == 1
                || TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS.matches(rechtspraakElement.normalizedText));
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            return Doubles.asDouble(
                    isVeryLikelyFirstTitle(sequence[indexInSequence], currentTag, previousTag)
            );
        }

        @Override
        public boolean equals(Object o) {
            return this == o || !(o == null || getClass() != o.getClass());
        }

        @Override
        public int hashCode() {
            return 999;
        }
    }

    public static class Filter extends org.crf.crf.filters.Filter<RechtspraakElement, Label> {

        @Override
        public boolean equals(Object o) {
            return this == o || !(o == null || getClass() != o.getClass());
        }

        @Override
        public int hashCode() {
            return 999;
        }
    }
}
