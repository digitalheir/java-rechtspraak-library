package org.leibnizcenter.rechtspraak.cfg.rule;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by maarten on 18-4-16.
 */
public class RightHandSide implements Comparable<RightHandSide> {
    public final ImmutableList<Type> types;

    public RightHandSide(@NotNull ImmutableList<Type> types) {
        this.types = types;
    }

    public RightHandSide(@NotNull Type... types) {
        this(ImmutableList.copyOf(types));
    }


    @Override
    public int compareTo(@NotNull RightHandSide other) {
        for (int i = 0; i < size(); i++) {
            if (other.size() <= i) return 1;
            int compareTo = get(i).compareTo(other.get(i));
            if (compareTo != 0) return compareTo;
        }

        return Integer.compare(size(), other.size());
    }

    @NotNull
    public Type get(int i) {
        return types.get(i);
    }

    public int size() {
        return types.size();
    }

    /**
     * This method is used by the removeEpsilons() method in CFG
     * For example:
     * If we encounter a rule "A -> ε" in removeEpsilons()
     * we want to go through all of the rules that contain the symbol
     * "A" and create new rules that have "ε" instead of "A".
     * But we still want to keep the original rules that had "A".  To do
     * this you can call rule.copyAndReplaceAll('A',null).  This will attempt
     * to replace all occurrence of 'A' with "" and return a new Rule that
     * corresponds to the new RHS.  This rule can then be added to the ruleSet.
     * If no replacements are made it returns null.
     */
    @NotNull
    public RightHandSide copyAndReplaceAll(Type removeType, Type newSymbol) {
        ArrayList<Type> newTypeList = new ArrayList<>(types.size());
        Type ours;
        for (int i = 0; i < size(); i++) {
            ours = get(i);
            if (!ours.equals(removeType))
                newTypeList.add(ours);
            else if (newSymbol != null) newTypeList.add(newSymbol);
        }

        return new RightHandSide(ImmutableList.copyOf(newTypeList));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RightHandSide that = (RightHandSide) o;

        return types.equals(that.types);

    }

    @Override
    public int hashCode() {
        return types.hashCode();
    }

    public boolean contains(Type type) {
        return types.contains(type);
    }

    public boolean match(Type... elements) {
        if (elements.length != size()) return false;
        for (int i = 0; i < elements.length; i++) if (!elements[i].equals(get(i))) return false;
        return true;
    }
    public boolean match(List<Type> elements) {
        return types.equals(elements);
    }

    @Override
    public String toString() {
        return String.join(" ", this.types.stream().map(t -> "<" + t.toString() + ">").collect(Collectors.toList()));
    }

}
