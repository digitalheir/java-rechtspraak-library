package org.leibnizcenter.util.numbering;

/**
 * Created by maarten on 27-3-16.
 */
public interface FullNumber extends SingleTokenNumbering {
    static boolean isFirstNumberInSequence(FullNumber checkFor) {
        return NumberingNumber.isFirstNumberInSequence(checkFor.mainNum());
    }

    String canonicalRepresentation();

    FullNumber succ();
}
