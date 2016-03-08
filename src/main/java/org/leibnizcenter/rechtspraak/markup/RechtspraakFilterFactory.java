package org.leibnizcenter.rechtspraak.markup;

import org.crf.crf.filters.Filter;
import org.leibnizcenter.rechtspraak.markup.features.*;
import org.leibnizcenter.rechtspraak.util.TextBlockInfo;
import org.leibnizcenter.rechtspraak.util.numbering.FullSectionNumber;

import java.util.HashSet;

/**
 * Created by maarten on 29-2-16.
 */
public class RechtspraakFilterFactory extends HashSet<Filter<RechtspraakElement, Label>> {

    public RechtspraakFilterFactory(RechtspraakElement[] sequence, int index,
                                    Label currentTag, Label previousTag) {
        RechtspraakElement thisToken = sequence[index];

        add(HasLabel.filters.get(currentTag));

        // Transition filters
        //System.out.println(previousTag + " => " + currentTag);
        this.add(Label.getTransitionFilters(previousTag == null ? Label.NULL : previousTag, currentTag));

        ////
        // Token value filters
        ////
        String normalizedText = thisToken.getNormalizedText();

        // Add titles we've seen
//        TitlesEncountered.encountered.stream()
//                .filter(pattern -> pattern.matches(normalizedText))
//                .forEach(pattern -> this.add(pattern.filters.get(currentTag)));

        // Add patterns we've made
        TextBlockInfo.patterns.stream()
                .filter(pattern -> pattern.matches(normalizedText))
                .forEach(pattern -> this.add(pattern.filters.get(currentTag)));

        if (FirstSectionTitleAfterInfo.isVeryLikelyFirstTitle(thisToken, currentTag, previousTag)) {
            add(FirstSectionTitleAfterInfo.filter);
        }
        if (thisToken.isSpaced) add(TextBlockInfo.Space.filters.get(currentTag));
        if (thisToken.isAllCaps) add(IsAllCaps.filters.get(currentTag));


        for (WordCount wc : WordCount.values()) {
            if (WordCount.isTrue(wc, thisToken)) {
                add(WordCount.filters.get(wc).get(currentTag));
            }
        }

        // Numbering filters
        if (thisToken.numbering != null) {
            this.add(HasNumbering.filters.get(currentTag));
            if (IsPartOfList.IsPartOfListFeature.isPartOfList(sequence, index)) {
                this.add(IsPartOfList.filters.get(currentTag));
            }

            if (thisToken.numbering instanceof FullSectionNumber) {
                this.add(HasFullSectionNumbering.filters.get(currentTag));
            }
            if (thisToken.numbering.getTerminal() != null) {
                this.add(HasNumberingTerminal.filters.get(currentTag));
            }

            // TODO Incrementing, isPartOfList
        }

        //
        // Token position
        //

        // nth quartile currentTag
        if (sequence.length > 0)
            this.add(Quartile.isInQuartile(((double) (index + 1)) / ((double) sequence.length)).getFilter(currentTag));

        // *.info in position X
        if (currentTag == Label.INFO) {
            for (InfoAsFirstX i : InfoAsFirstX.values()) {
                if (index < i.getX()) add(i.filter);
            }
        }
    }
}
