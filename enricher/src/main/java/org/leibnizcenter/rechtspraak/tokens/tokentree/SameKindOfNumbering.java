package org.leibnizcenter.rechtspraak.tokens.tokentree;

import org.leibnizcenter.rechtspraak.tokens.numbering.*;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * Created by maarten on 2-4-16.
 */
public enum SameKindOfNumbering implements BiPredicate<TokenTreeLeaf, TokenTreeLeaf> {
    ALPHABETIC((n1, n2) -> Numbering.isAlphabetic(n1) && Numbering.isAlphabetic(n2)),
    ANY_NUMBERING((n1, n2) -> Numbering.is(n1) && Numbering.is(n2)),

    SAME_KIND_OF_NUMBERING(SameKindOfNumbering::sameKindOfNumbering),
    SAME_KIND_OF_NUMBERING_SAME_TERMINAL((n1, n2) -> sameKindOfNumbering(n1, n2) && Objects.equals(((Numbering) n1).getTerminal(), ((Numbering) n2).getTerminal())),

    ALPHABETIC_SAME_TERMINAL((n1, n2) -> Numbering.isAlphabetic(n1) && Numbering.isAlphabetic(n2) && Objects.equals(((Numbering) n1).getTerminal(), ((Numbering) n2).getTerminal())),
    ANY_NUMBERING_SAME_TERMINAL((n1, n2) -> Numbering.is(n1) && Numbering.is(n2) && Objects.equals(((Numbering) n1).getTerminal(), ((Numbering) n2).getTerminal()));

    private final BiPredicate<TokenTreeLeaf, TokenTreeLeaf> predicate;

    SameKindOfNumbering(BiPredicate<TokenTreeLeaf, TokenTreeLeaf> predicate) {
        this.predicate = predicate;
    }

    public static boolean isSameProfileSuccession(Numbering n1, Numbering n2) {
        return fitSameProfile(n1, n2)
                && n2.isSuccedentOf(n1);
    }

    private static boolean fitSameProfile(Numbering n1, Numbering n2) {
        return SAME_KIND_OF_NUMBERING.test(n1, n2)
                && Objects.equals(n1.getTerminal(), n2.getTerminal());
    }

    private static boolean sameKindOfNumbering(TokenTreeLeaf n1, TokenTreeLeaf n2) {
        return (Numbering.isRoman(n1) && Numbering.isRoman(n2))
                || (Numbering.isAlphabetic(n1) && Numbering.isAlphabetic(n2))
                || (Numbering.isArabic(n1) && Numbering.isArabic(n2))
                || (Numbering.isNonNumeric(n1) && Numbering.isNonNumeric(n2))
                || (Numbering.isCompositeNumbering(n1) && Numbering.isCompositeNumbering(n2));
    }

    public static boolean isSameProfileSuccession(Map.Entry<Integer, Numbering> numbering1, Map.Entry<Integer, Numbering> numbering2) {
        return numbering2.getKey().compareTo(numbering1.getKey()) > 0
                && isSameProfileSuccession(numbering1.getValue(), numbering2.getValue());
    }

    public static boolean isSameProfile(Numbering n1, Numbering n2) {
        return fitSameProfile(n1, n2);
    }

    @Override
    public boolean test(TokenTreeLeaf o, TokenTreeLeaf o2) {
        return predicate.test(o, o2);
    }

    public List getList(java.util.List<TokenTreeLeaf> elements, int ix) {
        if (elements.get(ix) instanceof Numbering) return ((Numbering) elements.get(ix)).getSequence(this);
        return null;
    }

    public static class List extends ArrayList<Numbering> {
        public final SameKindOfNumbering profile;
        public boolean taintedByImplausibleNumbering;

        public List(SameKindOfNumbering profile) {
            super();
            this.profile = profile;
        }

        public void taintByImplausibleNumbering() {
            taintedByImplausibleNumbering=true;
        }
        public boolean isTaintedByImplausibleNumbering() {
            return taintedByImplausibleNumbering;
        }
    }
}
