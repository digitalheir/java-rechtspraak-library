//package org.leibnizcenter.rechtspraak.markup.docs.features;
//
//import CrfFeature;
//import org.leibnizcenter.rechtspraak.markup.docs.Label;
//import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
//import org.leibnizcenter.util.Doubles;
//
///**
// * Created by maarten on 29-2-16.
// */
//public class TransitionFeature extends CrfFeature<RechtspraakElement, Label> {
//    private final Label to;
//    private final Label from;
//
//    public TransitionFeature(Label from, Label to) {
//        this.to = to;
//        this.from = from;
//    }
//
//    @Override
//    public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
//        previousTag = previousTag == null ? Label.NULL : previousTag;
//        return Doubles.asDouble(previousTag.equals(from) && currentTag.equals(to));
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        TransitionFeature that = (TransitionFeature) o;
//
//        return to == that.to && from == that.from;
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = to != null ? to.hashCode() : 0;
//        result = 31 * result + (from != null ? from.hashCode() : 0);
//        return result;
//    }
//}
