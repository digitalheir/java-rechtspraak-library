package org.leibnizcenter.rechtspraak.util.numbering;

/**
 * Represents a section numbering, so an integer (compare to {@link SubSectionNumber})
 * Created by maarten on 16-2-16.
 */
public interface FullSectionNumber extends NumberingNumber {
    static boolean isFirstNumberInSequence(FullSectionNumber checkFor) {
        return NumberingNumber.isFirstNumberInSequence(checkFor.mainNum());
    }

    FullSectionNumber succ();
}
