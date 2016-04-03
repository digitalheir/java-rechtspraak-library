package org.leibnizcenter.rechtspraak.tokens.numbering;

import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.AlphabeticNumbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.NumberingProfile;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Created by Maarten on 2016-03-21.
 */
public class Numbering extends RechtspraakElement {
    private NumberingNumber numbering;
    public boolean isPlausibleNumbering;
    private NumberingProfile alphabeticSequence;

    private static final Comparator<Map.Entry<Integer, Numbering>> sortByKey = (o1, o2) -> o1.getKey().compareTo(o2.getKey());

    private final SortedSet<Map.Entry<Integer, Numbering>> sameProfileSuccessors = new TreeSet<>(sortByKey);
    private final SortedSet<Map.Entry<Integer, Numbering>> sameProfilePredecessors = new TreeSet<>(sortByKey);

    private final SortedSet<Map.Entry<Integer, Numbering>> plausibleSuccessors = new TreeSet<>(sortByKey);
    private final SortedSet<Map.Entry<Integer, Numbering>> plausiblePredecessors = new TreeSet<>(sortByKey);

    public Numbering(Element element) {
        super(element);
        this.numbering = NumberingNumber.startsWithNumbering(this.getTextContent());
        if (numbering == null)
            throw new NullPointerException();
    }


    public NumberingNumber getNumbering() {
        return numbering;
    }

    public String getTerminal() {
        return numbering.getTerminal();
    }

    public void setAlphabeticSequence(NumberingProfile alphabeticSequence) {
        this.alphabeticSequence = alphabeticSequence;
    }

    public NumberingProfile getAlphabeticSequence() {
        return alphabeticSequence;
    }

    public boolean isSuccedentOf(Numbering earlier) {
        return this.getNumbering().isSuccedentOf(earlier.getNumbering());
    }

    public void addSameProfileSuccessor(Map.Entry<Integer, Numbering> succ) {
        sameProfileSuccessors.add(succ);
    }

    public SortedSet<Map.Entry<Integer, Numbering>> getSameProfilePredecessors() {
        return sameProfilePredecessors;
    }

    public void addSameProfilePredecessor(Map.Entry<Integer, Numbering> pred) {
        sameProfilePredecessors.add(pred);
    }

    public SortedSet<Map.Entry<Integer, Numbering>> getSameProfileSuccessors() {
        return sameProfileSuccessors;
    }

    public static boolean isAlphabetic(TokenTreeLeaf element) {
        return element instanceof Numbering && ((Numbering) element).getNumbering() instanceof AlphabeticNumbering;
    }

    public static boolean isRoman(TokenTreeLeaf element) {
        return element instanceof Numbering && ((Numbering) element).getNumbering() instanceof RomanNumeral;
    }

    public static boolean isArabic(TokenTreeLeaf element) {
        return element instanceof Numbering && ((Numbering) element).getNumbering() instanceof ArabicNumbering;
    }

    public static boolean isNonNumeric(TokenTreeLeaf element) {
        return element instanceof Numbering && ((Numbering) element).getNumbering() instanceof NonNumericNumbering;
    }

    public static boolean isCompositeNumbering(TokenTreeLeaf element) {
        return element instanceof Numbering && ((Numbering) element).getNumbering() instanceof SubSectionNumber;
    }

    public static boolean isCharNumbering(TokenTreeLeaf element) {
        return element instanceof Numbering && ((Numbering) element).getNumbering() instanceof SingleCharNumbering;
    }

    public static boolean isCharNumbering(char character, TokenTreeLeaf e) {
        return isCharNumbering(e) && ((SingleCharNumbering) ((Numbering) e).getNumbering()).getCharacter() == character;
    }

    public void addPlausibleSuccessor(Map.Entry<Integer, Numbering> numbering) {
        plausibleSuccessors.add(numbering);
    }

    public void addPlausiblePredecessor(Map.Entry<Integer, Numbering> numbering) {
        plausiblePredecessors.add(numbering);
    }

    public Collection<Map.Entry<Integer, Numbering>> getPlausiblePredecessors() {
        return plausiblePredecessors;
    }

    public Collection<Map.Entry<Integer, Numbering>> getPlausibleSuccessors() {
        return plausibleSuccessors;
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
//            AbstractRechtspraakElement element = token.getToken();
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
//                    }
//                }
//
//            }
//        }
//    }
}
