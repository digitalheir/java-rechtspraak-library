package org.leibnizcenter.rechtspraak.util.numbering;

import com.google.common.base.Strings;
import org.leibnizcenter.rechtspraak.features.KnownSurnamesNl;
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

    public static boolean isCharNumbering(char ch, RechtspraakElement e) {
        return e.numbering != null
                && e.numbering instanceof AlphabeticNumbering
                && ((AlphabeticNumbering) e.numbering).getCharacter() == ch;
    }

    public static boolean wasGetekend(RechtspraakElement first, RechtspraakElement second) {
        return isCharNumbering('w', first)
                && isCharNumbering('g', second);
    }

    public static boolean hasArabicNumberWithNoTerminal(NumberingNumber numbering) {
        return numbering != null
                && Strings.isNullOrEmpty(numbering.getTerminal())
                && numbering instanceof ArabicNumbering
                ;
    }

    public static boolean hasAlphabeticNumberWithNoTerminal(NumberingNumber numbering) {
        return numbering != null
                && Strings.isNullOrEmpty(numbering.getTerminal())
                && numbering instanceof AlphabeticNumbering
                ;
    }

    public static boolean isFirstPlausibleAlphabeticNumWalkingBackFrom(char character, List<RechtspraakElement> untagged, int ix) {
        for (int i = ix - 1; i >= 0; i--) {
            if (untagged.get(i).isPlausibleNumbering
                    && untagged.get(i).numbering != null && untagged.get(i).numbering instanceof AlphabeticNumbering) {
                return ((AlphabeticNumbering) untagged.get(i).numbering).getCharacter() == character;
            }
        }
        return false;
    }

    public static boolean isFirstPlausibleNumberingWalkingBackFrom(int num, List<RechtspraakElement> untagged, int ix) {
        for (int i = ix - 1; i >= 0; i--) {
            if (untagged.get(i).isPlausibleNumbering && untagged.get(i).numbering instanceof ArabicNumbering) {
                return untagged.get(i).numbering.mainNum() == num;
            }
        }
        return false;
    }

    public static boolean isSpelledOut(List<RechtspraakElement> untagged, int ix) {
        if (untagged.size() > ix + 1) {
            NumberingNumber numbering = untagged.get(ix).numbering;
            if (hasArabicNumberWithNoTerminal(numbering)) {
                RechtspraakElement next = untagged.get(ix + 1);
                String text = next.normalizedText;
                switch (numbering.mainNum()) {
                    case 1:
                        return text.startsWith("een") || text.startsWith("één");
                    case 2:
                        return text.startsWith("twee");
                    case 3:
                        return text.startsWith("drie");
                    case 4:
                        return text.startsWith("vier");
                    case 5:
                        return text.startsWith("vijf");
                    case 6:
                        return text.startsWith("zes");
                    case 7:
                        return text.startsWith("zeven");
                    case 8:
                        return text.startsWith("acht");
                    case 9:
                        return text.startsWith("negen");
                    case 10:
                        return text.startsWith("tien");
                    case 11:
                        return text.startsWith("elf");
                    case 12:
                        return text.startsWith("twaalf");
                    case 13:
                        return text.startsWith("dertien");
                    case 14:
                        return text.startsWith("veertien");
                    case 15:
                        return text.startsWith("vijftien");
                    case 16:
                        return text.startsWith("zestien");
                    case 17:
                        return text.startsWith("zeventien");
                    case 18:
                        return text.startsWith("achttien");
                    case 19:
                        return text.startsWith("negentien");
                    case 20:
                        return text.startsWith("twintig");
                }
            }
        }
        return false;
    }

    public static boolean afterWasGetekend(List<RechtspraakElement> elements, int ix) {
        return ix > 1 && wasGetekend(elements.get(ix - 2), elements.get(ix - 1));
    }

    public static boolean partOfWasGetekend(List<RechtspraakElement> elements, int ix) {
        RechtspraakElement e = elements.get(ix);
        return ((ix + 1) < elements.size() && wasGetekend(e, elements.get(ix + 1)))
                || (ix > 0 && wasGetekend(elements.get(ix - 1), e));
    }

    public static boolean hasCharNumberingAndDoesntFollowsPreviousChar(List<RechtspraakElement> elements, int ix, char f) {
        RechtspraakElement e = elements.get(ix);
        return isCharNumbering(f, e) && !isFirstPlausibleAlphabeticNumWalkingBackFrom((char) (f - 1), elements, ix);
    }

    public static char getUppercaseCharNumbering(RechtspraakElement token) {
        if (token.numbering != null
                && token.numbering instanceof AlphabeticNumbering
                && Character.isUpperCase(((AlphabeticNumbering) token.numbering).getCharacter())) {
            return ((AlphabeticNumbering) token.numbering).getCharacter();
        } else {
            return '\0';
        }
    }

    public static boolean isJustSpacedLetters(List<RechtspraakElement> untagged, int ix) {
        // TODO also check if the letters are inline...
        if (untagged.size() > ix + 1) {
            NumberingNumber numbering = untagged.get(ix).numbering;
            if (hasAlphabeticNumberWithNoTerminal(numbering)
                    && !(numbering.isFirstNumbering()
                    || isFirstPlausibleAlphabeticNumWalkingBackFrom(
                    (char) (((AlphabeticNumbering) numbering).getCharacter() - 1),
                    untagged,
                    ix))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isJustStartOfSentence(List<RechtspraakElement> untagged, int ix) {
        if (untagged.size() > ix + 1) {
            NumberingNumber numbering = untagged.get(ix).numbering;
            if (hasArabicNumberWithNoTerminal(numbering)
                    && !(numbering.isFirstNumbering()
                    || isFirstPlausibleNumberingWalkingBackFrom(numbering.mainNum() - 1, untagged, ix))) {
                return true;
            } else if (numbering instanceof AlphabeticNumbering
                    && !(numbering.isFirstNumbering()
                    || isFirstPlausibleAlphabeticNumWalkingBackFrom((char) (((AlphabeticNumbering) numbering).getCharacter() - 1), untagged, ix))
                    ) {
                return true;
            }
        }
        return false;
    }

    public static boolean isProbablyName(List<RechtspraakElement> untagged, int ix) {
        char num = getUppercaseCharNumbering(untagged.get(ix));

        // If this element is an uppercase char, it might be part of a name
        if (num != '\0') {

            //noinspection UnnecessaryLocalVariable
            int startToken = ix, lastToken = ix;
            if (!untagged.get(ix).followsLineBreak) {
                for (int i = ix - 1; num != '\0' && i >= 0; i--) {
                    RechtspraakElement token = untagged.get(ix);
                    num = getUppercaseCharNumbering(token);
                    if (num != '\0') {
                        // Having multiple letters after each other
                        // doesn't make sense for a list
                        // ex.
                        //    A. [T] S Eliot said some thing
                        // ex.
                        //    C [B] de Mill said otherwise
                        // startToken = i;
                        //if (!token.followsLineBreak) {
                        //num = '\0';
                        //}
                        return true;
                    } else num = '\0';
                }
            }

            for (int i = ix + 1; num != '\0' && i < untagged.size(); i++) {
                RechtspraakElement token = untagged.get(ix);
                if (!token.followsLineBreak) {
                    num = getUppercaseCharNumbering(token);
                    if (num != '\0') {
                        // Having multiple letters after each other
                        // doesn't make sense for a list
                        // NOTE: this fails on the case
                        //    [A.] T S Eliot said some thing
                        //    [B.] C B de Mill said otherwise
                        // startToken = i;
                        return true;
                    }
                } else num = '\0';
            }

            if (lastToken - (startToken - 1) > 1) {
                // Having multiple letters after each other doesn't make sense for a list
                // NOTE: this fails on the case
                //    [A.] T S Eliot said some thing
                //    [B.] C B de Mill said otherwise
                return true;
            } else {
                // startToken == lastToken

                // Single char.... if this is an A., only return
                // true if it's followed by a known last name
                RechtspraakElement token = untagged.get(startToken);
                return getUppercaseCharNumbering(token) == 'A'
                        && startToken < untagged.size() - 1
                        && !token.followsLineBreak
                        && KnownSurnamesNl.startsWithKnownName(untagged.get(startToken + 1));
            }
            //List<Character> stringOfChars = new ArrayList<>(lastToken-(startToken-1));
        }

        return false;
    }

    public static boolean looksLikeNumberingButProbablyIsnt(List<RechtspraakElement> elements, int ix) {
        return partOfWasGetekend(elements, ix) // [w.][g.]
                || afterWasGetekend(elements, ix) // w.g. [A.] van der Hoeven
                || (hasCharNumberingAndDoesntFollowsPreviousChar(elements, ix, 'f')) // florijnen
                || (hasCharNumberingAndDoesntFollowsPreviousChar(elements, ix, 'e')) // euro
                || (hasCharNumberingAndDoesntFollowsPreviousChar(elements, ix, 'u')) // u heeft
                || (_s(elements, ix)) // 's
                || (isSpelledOut(elements, ix)) // 2 (twee)
                || (isJustStartOfSentence(elements, ix)) // 7 patronen
                || (isJustSpacedLetters(elements, ix)) // u i t s p r a a k
                || (isProbablyName(elements, ix)) //
                ;
    }

    public static boolean _s(List<RechtspraakElement> elements, int ix) {
        return (ix > 0
                && isCharNumbering('s', elements.get(ix))
                && elements.get(ix - 1).getTextContent().endsWith("'")
                && !isFirstPlausibleAlphabeticNumWalkingBackFrom((char) ('s' - 1), elements, ix)
        );
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
