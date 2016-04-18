package org.leibnizcenter.rechtspraak.cfg.rule;

import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.cfg.CKY;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;

import java.security.InvalidParameterException;

/**
 * A class that keeps track of rules
 * for a CFG
 */
public class Rule implements Comparable {

    /**
     * left hand side (upper case char)
     */
    public final NonTerminal LHS;
    /**
     * left hand side (some string)
     */
    public final RightHandSide RHS;
    private final double priorProbability;

    public Rule(NonTerminal left, RightHandSide right, double probability) {
        LHS = left;
        RHS = right;
        this.priorProbability = probability;
        if (isReflexiveRule()) throw new InvalidParameterException("Reflexive rules not allowed");
    }

    /**
     * @return number between 0 and 1 inclusive
     */
    public double getProbability(CKY.Score... inputs) {
        double prob = priorProbability;
        for (CKY.Score s : inputs) prob *= s.getProbability();
        return prob;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        Rule other = (Rule) o;

        int lhsComparison = LHS.compareTo(other.LHS);
        if (lhsComparison != 0) return lhsComparison;
        return (RHS.compareTo(other.RHS));
    }

    public boolean equals(Object x) {
        if (!(x instanceof Rule)) return false;
        Rule y = (Rule) x;
        return LHS == y.LHS && RHS.equals(y.RHS);
    }

    /**
     * @return whether this rule looks like A -> B, where A and B are non-terminals
     */
    public boolean isUnaryProduction() {
        return RHS.size() == 1 && RHS.get(0) instanceof NonTerminal;
    }

    /**
     * @return whether this rule looks like A -> B C, where A, B and C are non-terminals
     */
    public boolean isBinaryProduction() {
        return RHS.size() == 2
                && RHS.get(0) instanceof NonTerminal
                && RHS.get(1) instanceof NonTerminal;
    }

    public boolean isEpsilonRule() {
        return RHS.size() == 0;
    }


    public RightHandSide getRHS() {
        return RHS;
    }

//    public void setRHS(RightHandSide RHS) {
//        this.RHS = RHS;
//    }


    public NonTerminal getLHS() {
        return LHS;
    }


    /**
     * Test to see if a rule just produce its own type
     **/
    public boolean isReflexiveRule() {
        return (RHS.size() == 1 && LHS.equals(RHS.get(0)));
    }

    public String toString() {
        return LHS + " -> " + (isEpsilonRule() ? "ε" : RHS);
    }


    public boolean isTerminal() {
        return RHS.size() == 1 && RHS.get(0) instanceof Terminal;
    }
}




