package org.leibnizcenter.rechtspraak.cfg.rule;

import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.cfg.CYK;
import org.leibnizcenter.rechtspraak.cfg.MalformedGrammarException;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Type;
import org.leibnizcenter.rechtspraak.util.Regex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     * Recall that log(a*b) = log(a) + log(b)
     *
     * @return Logarithm of a number between 0 and 1 inclusive
     */
    public double getLogProbability(CYK.ParseTreeContainer... inputs) {
        double prob = Math.log(priorProbability);
        for (CYK.ParseTreeContainer s : inputs) prob += s.getLogProbability();
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

    private static final Pattern SINGLE_RULE = Pattern.compile("\\s*(.*)\\s*(->|→)\\s*(.*)\\s*");
    private static final Pattern PROB = Pattern.compile("\\s*(.*)(\\([0-9]+(?:\\.[0-9]+)?%?\\))?\\s*");

    public static Collection<Rule> parseRules(BufferedReader reader) throws IOException, MalformedGrammarException {
        Collection<Rule> rules = new HashSet<>(100);

        reader.lines().forEach(line -> {
            if (!Regex.CONSECUTIVE_WHITESPACE.matcher(line).matches()) { // ignore white lines
                Matcher m = SINGLE_RULE.matcher(line);
                if (!m.matches())
                    throw new MalformedGrammarException("Rule could not be parsed: " + line);
                String LHString = m.group(1);
                String RHString = m.group(3);
                if (SINGLE_RULE.matcher(LHString).matches() || SINGLE_RULE.matcher(RHString).matches())
                    throw new MalformedGrammarException("Rule should contain just one arrow. " + line);
                NonTerminal LHS = new NonTerminal(LHString.trim());
                String[] RHSs = RHString.split("\\|");
                for (String rhs : RHSs) {
                    Rule r = parseRule(line, LHS, rhs);
                    rules.add(r);
                }
            }
        });
        return rules;
    }

    @NotNull
    private static Rule parseRule(String line, NonTerminal LHS, String rhs) throws MalformedGrammarException {
        Matcher ma = PROB.matcher(rhs);
        if (!ma.matches()) throw new MalformedGrammarException("Probability unparseable: "
                + rhs + "\n  in line" + "\n" + line);
        double prob = parseProbability(ma);
        String[] typeStrings = Regex.CONSECUTIVE_WHITESPACE.split(ma.group(1));
        List<Type> types = new ArrayList<>(typeStrings.length);
        for (String typeString : typeStrings) {
            Type type = Type.parse(typeString);
            types.add(type);
        }
        if (types.size() != 1 && types.stream().filter(t -> t == null).count() > 0)
            throw new MalformedGrammarException("Epsilon should be only token: " + rhs);
        RightHandSide RHS = new RightHandSide(types.stream().filter(t -> t != null).collect(Collectors.toList()));
        return new Rule(LHS, RHS, prob);
    }

    private static double parseProbability(Matcher ma) {
        double prob;
        if (ma.group(2) != null) {
            if (ma.group(2).endsWith("%")) {
                prob = Double.parseDouble(ma.group(2)) / 100.0;
            } else {
                prob = Double.parseDouble(ma.group(2));
            }
        } else {
            prob = 1.0;
        }
        return prob;
    }

    public static Collection<Rule> parseRules(String string) throws IOException, MalformedGrammarException {
        return parseRules(new BufferedReader(new StringReader(string)));
    }

    public double getPriorProbability() {
        return priorProbability;
    }
}




