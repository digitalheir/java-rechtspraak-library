
package org.leibnizcenter.rechtspraak.cfg.rule;

import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.cfg.CYK;
import org.leibnizcenter.rechtspraak.cfg.rule.interfaces.Rule;
import org.leibnizcenter.rechtspraak.cfg.rule.type.interfaces.NonTerminal;

import java.security.InvalidParameterException;

/**
 * Extends {@see Rule}
 */
public abstract class StochasticRule implements Comparable,Rule {

    /**
     * left hand side (upper case char)
     */
    private final NonTerminal LHS;
    /**
     * left hand side (some string)
     */
    private final RightHandSide RHS;

    private final double priorProbability;

    @SuppressWarnings("WeakerAccess")
    public StochasticRule(NonTerminal left, RightHandSide right, double probability) {
        LHS = left;
        RHS = right;
        this.priorProbability = probability;
        if (Rule.isReflexiveRule(this)) throw new InvalidParameterException("Reflexive rules not allowed");
    }

    @Override
    public int compareTo(@NotNull Object o) {
        Rule other = (Rule) o;

        int lhsComparison = LHS.compareTo(other.getLHS());
        if (lhsComparison != 0) return lhsComparison;
        return (RHS.compareTo(other.getRHS()));
    }

    public boolean equals(Object x) {
        if (!(x instanceof StochasticRule)) return false;
        Rule y = (StochasticRule) x;
        return LHS == y.getLHS() && RHS.equals(y.getRHS());
    }

    public RightHandSide getRHS() {
        return RHS;
    }

    public NonTerminal getLHS() {
        return LHS;
    }


    public String toString() {
        return LHS + " -> " + (Rule.isEpsilonRule(this) ? "ε" : RHS);
    }

    public double getPriorProbability() {
        return priorProbability;
    }
    public abstract double getLogProbability(CYK.ParseTreeContainer... inputs);
}




