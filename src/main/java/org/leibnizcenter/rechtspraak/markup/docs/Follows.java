package org.leibnizcenter.rechtspraak.markup.docs;

import org.crf.crf.CrfFeature;

/**
 * Created by maarten on 28-2-16.
 */
public class Follows extends CrfFeature<String, String> {
    private final String checkFor;

    public Follows(String checkFor) {
        super();
        if (checkFor == null) throw new NullPointerException();
        this.checkFor = checkFor;
    }

    @Override
    public double value(String[] sequence, int indexInSequence, String currentTag, String previousTag) {
        if (checkFor.equals(sequence[indexInSequence])
                && currentTag.startsWith(sequence[indexInSequence])
                && (
                currentTag.endsWith("X")
                        || (indexInSequence > 0 && (
                        previousTag.startsWith(sequence[indexInSequence - 1]) &&
                                sequence[indexInSequence].equals(sequence[indexInSequence - 1]) || currentTag.endsWith(sequence[indexInSequence - 1])
                )
                )
        )) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Follows follows = (Follows) o;
        return checkFor.equals(follows.checkFor);
    }

    @Override
    public int hashCode() {
        return checkFor.hashCode();//checkFor != null ?  : 0;
    }
}
