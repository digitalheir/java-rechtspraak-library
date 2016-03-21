package org.leibnizcenter.rechtspraak.util.numbering;

import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maarten on 2016-03-21.
 */
public class Numbering extends ArrayList<List<Numbering>> {
    private final List<List<Numbering>> subSequences;
    private NumberingNumber numbering;
    private RechtspraakElement element;

    public Numbering(NumberingNumber num, List<List<Numbering>> subSequences, RechtspraakElement element) {
        this.subSequences = subSequences;
        this.numbering = num;
        this.element = element;
    }

    public Numbering(NumberingNumber num, RechtspraakElement element) {
        this.subSequences = new ArrayList<>();
        this.numbering = num;
        this.element = element;
    }

    public NumberingNumber getNumbering() {
        return numbering;
    }

    public List<List<Numbering>> getSubSequences() {
        return subSequences;
    }


    public void addSubSequence(List<Numbering> subSequence) {
        this.subSequences.add(subSequence);
    }

    public void addSubSequences(List<List<Numbering>> subSequences) {
        this.subSequences.addAll(subSequences);
    }

//    /**
//     * Non-deterministically collect numbering sequences.  All incrementing sequences of numbering are seen as a list.
//     * Subsection numberings and plain lists are both seen as subsequences.
//     * <pre>
//     * 1. Section one
//     *   1.1. Subsection one point one
//     * 2. Section two
//     *   This section contains a list, which is a sub-sequence of Section two:
//     *   1. Item 1
//     *   2. Item 2
//     * 3. Section three will also be in the sub-sequence item list, because we can't know to which 2 this is subsequent
//     *   The following list might semantically fall outside of section 3, but there is no way for us to know
//     *   1. Item 1
//     *   2. Item 2
//     * </pre>
//     *
//     * @param tokenList
//     * @return
//     */
//    private static List<List<Numbering>> parseNumberingSequences(List<LabeledToken> tokenList) {
//        Stack<List<Numbering>> sequences = new Stack<>();
//
//        for (LabeledToken token : tokenList) {
//            RechtspraakElement element = token.getToken();
//            NumberingNumber numb = element.numbering;
//            if (numb != null) {
//                if (sequences.size() == 0) {
//                    // Start new sequence
//                    List<Numbering> currentSequence;
//                    currentSequence = new ArrayList<>();
//                    currentSequence.add(new Numbering(numb, element));
//                    sequences.push(currentSequence);
//                } else {
//                    List<Numbering> currentSequence = sequences.peek();
//                    Numbering previousNumber = currentSequence.get(currentSequence.size() - 1);
//                    if (numb.isSuccedentOf(previousNumber.getNumbering())) {
//                        // Continue sequence
//                        currentSequence.add(previousNumber);
//                    } else {
//                        // Close sequence and go on
//                        TODO
//                    }
//                }
//
//            }
//        }
//    }
}
