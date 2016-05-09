package org.leibnizcenter.rechtspraak.enricher.cfg.rule;

import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.enricher.cfg.MalformedGrammarException;
import org.leibnizcenter.rechtspraak.enricher.cfg.ScoreChart;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.interfaces.Rule;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.NonTerminalImpl;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.NonTerminal;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.Type;
import org.leibnizcenter.util.Regex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
public class StandardRule extends StochasticRule {
    private static final Pattern SINGLE_RULE = Pattern.compile("\\s*(.*)\\s*(->|→)\\s*(.*)\\s*");
    private static final Pattern PROB = Pattern.compile("\\s*(.*)(\\([0-9]+(?:\\.[0-9]+)?%?\\))?\\s*");


    public StandardRule(NonTerminal left, RightHandSide right, double probability) {
        super(left, right, probability);
    }

    /**
     * Recall that log(a*b) = log(a) + log(b)
     *
     * @return Logarithm of a number between 0 and 1 inclusive
     */
    public double getLogProbability(ScoreChart.ParseTreeContainer... inputs) {
        return getLogProbability(getPriorProbability(), inputs);
    }

    @Override
    public ScoreChart.ParseTreeContainer apply(double logProb, ScoreChart.ParseTreeContainer... inputs) {
        return new ScoreChart.ParseTreeContainer(logProb, this, inputs);
    }

    public static double getLogProbability(double priorProbability, ScoreChart.ParseTreeContainer... inputs) {
        double prob = Math.log(priorProbability);
        for (ScoreChart.ParseTreeContainer s : inputs)
            prob += s.getLogProbability();
        return prob;
    }

    @SuppressWarnings("WeakerAccess")
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
                NonTerminal LHS = new NonTerminalImpl(LHString.trim());
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
        return new StandardRule(LHS, RHS, prob);
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

    @SuppressWarnings("unused")
    public static Collection<Rule> parseRules(String string) throws IOException, MalformedGrammarException {
        return parseRules(new BufferedReader(new StringReader(string)));
    }

    public boolean equals(Object x) {
        if (!(x instanceof StandardRule)) return false;
        Rule y = (StandardRule) x;
        return getLHS() == y.getLHS() && getRHS().equals(y.getRHS());
    }
}




