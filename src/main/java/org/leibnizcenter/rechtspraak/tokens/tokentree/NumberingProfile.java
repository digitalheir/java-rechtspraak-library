package org.leibnizcenter.rechtspraak.tokens.tokentree;

import org.leibnizcenter.rechtspraak.tokens.numbering.*;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.AlphabeticNumbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Created by maarten on 2-4-16.
 */
public class NumberingProfile extends ArrayList<Numbering> {
    private final Class clazz;
    private final String terminal;

    private NumberingProfile(Class clazz, String terminal) {
        super();
        this.clazz = clazz;
        this.terminal = terminal;
    }

    public NumberingProfile(Numbering element) {
        this(getSuperClass(element), element.getTerminal());
    }

    public NumberingProfile(NumberingNumber element) {
        this(getSuperClass(element), element.getTerminal());
    }

    private static Class getSuperClass(Numbering element) {
        NumberingNumber num = element.getNumbering();
        return getSuperClass(num);
    }

    private static Class getSuperClass(NumberingNumber num) {
        if (num instanceof AmbiguousAlphabeticOrRomanNumeral) return AmbiguousAlphabeticOrRomanNumeral.class;
        if (num instanceof AlphabeticNumbering) return AlphabeticNumbering.class;
        if (num instanceof RomanNumeral) return RomanNumeral.class;
        if (num instanceof ArabicNumbering) return ArabicNumbering.class;
        if (num instanceof SubSectionNumber) return SubSectionNumber.class;
        throw new IllegalStateException(num.getClass() + "");
    }

    public boolean fits(NumberingNumber n) {
        return Objects.equals(n.getTerminal(), terminal) && clazz.isInstance(n);
    }

    public boolean fits(TokenTreeLeaf element) {
        return element instanceof Numbering && fits(((Numbering) element).getNumbering());
    }

    public void addInstance(Numbering numbering) {
        add(numbering);
    }

    public String getTerminal() {
        return terminal;
    }

    public static boolean isSameProfileSuccession(Numbering n1, Numbering n2) {
        return fitSameProfile(n1, n2)
                && n2.isSuccedentOf(n1);
    }

    private static boolean fitSameProfile(Numbering n1, Numbering n2) {
        return getSuperClass(n1).equals(getSuperClass(n2))
                && Objects.equals(n1.getTerminal(), n2.getTerminal());
    }

    public static boolean isSameProfileSuccession(Map.Entry<Integer, Numbering> numbering1, Map.Entry<Integer, Numbering> numbering2) {
        return numbering2.getKey().compareTo(numbering1.getKey()) > 0
                && isSameProfileSuccession(numbering1.getValue(), numbering2.getValue());
    }

    public static boolean isSameProfile(Numbering n1, Numbering n2) {
        return fitSameProfile(n1, n2);
    }
}
