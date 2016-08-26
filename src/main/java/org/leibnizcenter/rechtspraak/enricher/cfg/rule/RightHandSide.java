//package org.leibnizcenter.rechtspraak.enricher.cfg.rule;
//
//import com.google.common.collect.ImmutableList;
//import org.jetbrains.annotations.NotNull;
//import org.leibnizcenter.rechtspraak.enricher.cfg.ScoreChart;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.Terminal;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.Type;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//
///**
// * Represents righ hand side of a grammar rule
// * Created by maarten on 18-4-16.
// */
//public class RightHandSide implements Comparable<RightHandSide> {
//    public final Term term;
//
//    public RightHandSide(@NotNull Term t) {
//        this.term = t;
//    }
//
//    public RightHandSide(@NotNull Type... types) {
//        this(new Term(types));
//    }
//
//    public RightHandSide(List<Type> types) {
//        this(new Term(types));
//    }
//
//
//    @Override
//    public int compareTo(@NotNull RightHandSide other) {
//        for (int i = 0; i < size(); i++) {
//            if (other.size() <= i) return 1;
//            int compareTo = get(i).compareTo(other.get(i));
//            if (compareTo != 0) return compareTo;
//        }
//
//        return Integer.compare(size(), other.size());
//    }
//
//    @NotNull
//    public Type get(int i) {
//        return term.get(i);
//    }
//
//    public int size() {
//        return term.size();
//    }
//
//
//    @SuppressWarnings("unused")
//    public Term getTerm() {
//        return term;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        RightHandSide that = (RightHandSide) o;
//
//        return term.equals(that.term);
//
//    }
//
//    @Override
//    public int hashCode() {
//        return term.hashCode();
//    }
//
//    public boolean contains(Type type) {
//        return term.contains(type);
//    }
//
//    @SuppressWarnings("unused")
//    public boolean match(Type... elements) {
//        if (elements.length != size()) return false;
//        for (int i = 0; i < elements.length; i++) if (!elements[i].equals(get(i))) return false;
//        return true;
//    }
//
//
//    public boolean match(ScoreChart.ParseTreeContainer[] elements) {
//        if (elements.length != size()) return false;
//        for (int i = 0; i < elements.length; i++) if (!elements[i].getType().equals(get(i))) return false;
//        return true;
//    }
//
//    public boolean match(List<Type> elements) {
//        return term.equals(elements);
//    }
//
//    @Override
//    public String toString() {
//        return String.join(" ", this.term.stream().map(t -> "<" + t.toString() + ">").collect(Collectors.toList()));
//    }
//
//    public boolean hasNonSolitaryTerminal() {
//        return size() > 1
//                && term.stream().filter(t -> t instanceof Terminal).limit(1).count() > 0;
//    }
//
//    public Stream<Type> stream() {
//        return term.stream();
//    }
//
//
//    public Collection<RightHandSide> enumerateWaysToOmit(Set<Type> typesToCheck) {
//        if (!containsAny(typesToCheck)) return Collections.emptySet();
//
//        Set<RightHandSide> returnSet = new HashSet<>(size());
//        for (int i = 0; i < size(); i++) {
//            if (typesToCheck.contains(get(i))) {
//                ImmutableList.Builder<Type> builder = ImmutableList.builder();
//                if (i > 0) builder.addAll(term.subList(0, i));
//                if (i < size() - 1) builder.addAll(term.subList(i + 1, size()));
//                RightHandSide newRHS = new RightHandSide(new Term(builder.build()));
//                returnSet.add(newRHS);
//
//                returnSet.addAll(newRHS.enumerateWaysToOmit(typesToCheck));
//            }
//        }
//
//        return returnSet;
//    }
//
//    public boolean containsAny(Set<Type> searchFor) {
//        return term.stream().filter(searchFor::contains).limit(1).count() > 0;
//    }
//
//}
