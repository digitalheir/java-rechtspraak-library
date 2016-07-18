//package org.leibnizcenter.rechtspraak.markup.docs;
//
//import Filter;
//import org.leibnizcenter.rechtspraak.markup.docs.mostlikelytreefromlist.*;
//import org.leibnizcenter.util.numbering.FullSectionNumber;
//
//import java.util.HashSet;
//
///**
// * Created by maarten on 29-2-16.
// */
//public class RechtspraakFilters extends HashSet<Filter<RechtspraakElement, Label>> {
//
//    public RechtspraakFilters(RechtspraakElement[] sequence, int index,
//                              Label currentTag, Label previousTag) {
//        RechtspraakElement thisToken = sequence[index];
//
////        add(HasLabel.filters.get(currentTag));
//
//        // Transition filters
//        //System.out.println(previousTag + " => " + currentTag);
////        this.add(Label.getTransitionFilters(previousTag == null ? Label.NULL : previousTag, currentTag));
//
//        ////
//        // Token value filters
//        ////
//        String normalizedText = thisToken.getNormalizedText();
//
//        // Add titles we've seen
////        TitlesEncountered.encountered.stream()
////                .filter(pattern -> pattern.find(normalizedText))
////                .forEach(pattern -> this.add(pattern.filters.get(currentTag)));
//
//        // Add patterns we've made
//        //TODO finetune params (use NamePatterns class)
////        TextBlockInfo.patterns.stream()
////                .filter(pattern -> pattern.find(normalizedText))
////                .forEach(pattern -> this.add(pattern.filters.get(currentTag)));
////
////        if (Label.SECTION_TITLE.equals(currentTag)) {
////            for (NamePatterns p : NamePatterns.values()) {
////                if (p.find(thisToken.normalizedText)) {
////                    if(Label.INFO.equals(previousTag))System.out.println("Likely title: "+previousTag+" -> "+currentTag+" (" + thisToken.normalizedText+")");
////                    add(NamePatterns.filters.get(p).get(Label.SECTION_TITLE));
////                }
////            }
////        }
//
//        if (FirstSectionTitleAfterInfo.isVeryLikelyFirstTitle(thisToken, currentTag, previousTag)) {
//            add(FirstSectionTitleAfterInfo.filter);
//        }
////        if (thisToken.isSpaced) add(TextBlockInfo.Space.filters.get(currentTag));
////        if (thisToken.isAllCaps) add(IsAllCaps.filters.get(currentTag));
////
////
////        for (WordCount wc : WordCount.values()) {
////            if (WordCount.isTrue(wc, thisToken)) {
////                add(WordCount.filters.get(wc).get(currentTag));
////            }
////        }
//
//        // Numbering filters
//        if (thisToken.numbering != null) {
//            this.add(HasNumbering.filters.get(currentTag));
//            boolean isPartOfList = IsPartOfList.isPartOfList(sequence, index);
//            if (isPartOfList) {
////                if (Label.SECTION_TITLE.equals(currentTag))
////                    System.out.println("[List:] " + sequence[index].getTextContent());
//                this.add(IsPartOfList.filters.get(currentTag));
//            }
//
//
//            if (!isPartOfList && thisToken.numbering instanceof FullSectionNumber) {
////                if (Label.SECTION_TITLE.equals(currentTag))
////                    System.out.println("[Section nr:] " + sequence[index].getTextContent());
//                if (currentTag.equals(Label.SECTION_TITLE)) this.add(HasFullSectionNumbering.filters.get(currentTag));
//            }
////            if (thisToken.numbering.getTerminal() != null) {
////                if (Label.SECTION_TITLE.equals(currentTag))
////                    System.out.println("[Terminal:] " + sequence[index].getTextContent());
////                this.add(HasNumberingTerminal.filters.get(currentTag));
////            }
//
//            // TODO Incrementing, isLikelyPartOfList
//        }
//
//        //
//        // Token position
//        //
//
//        // nth quartile currentTag
//        if (sequence.length > 0)
//            this.add(Quartile.isInQuartile(((double) (index + 1)) / ((double) sequence.length)).getFilter(currentTag));
//
//        // *.info in position X
//        if (currentTag == Label.INFO) {
//            for (InfoAsFirstX i : InfoAsFirstX.values()) {
//                if (index < i.getX()) add(i.filter);
//            }
//        }
//    }
//}
