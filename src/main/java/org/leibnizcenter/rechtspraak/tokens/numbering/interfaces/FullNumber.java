package org.leibnizcenter.rechtspraak.tokens.numbering.interfaces;

/**
 * Created by maarten on 27-3-16.
 */
public interface FullNumber extends SingleTokenNumbering {
    static boolean isFirstNumberInSequence(FullNumber checkFor) {
        return NumberingNumber.isFirstNumberInSequence(checkFor.mainNum());
    }
    default boolean couldBeFirstInSequence() {
        return isFirstNumbering();
    }

    /**
     * @return The most important numbering as integer. For example, in '2.3.1' it's '2', in 'IX' it's '9'.
     */
    int mainNum();

    String canonicalRepresentation();

    FullNumber succ();
}
