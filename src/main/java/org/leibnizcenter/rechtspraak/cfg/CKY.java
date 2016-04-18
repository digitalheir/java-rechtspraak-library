package org.leibnizcenter.rechtspraak.cfg;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.cfg.rule.Rule;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Type;
import org.leibnizcenter.rechtspraak.util.Pair;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements CKY algorithm with unary production. Works on grammar in Chomsky Normal Form (with unary production
 * rules permitted).
 * Created by maarten on 17-4-16.
 */
public class CKY {
    /**
     * Don't instantiate
     */
    private CKY() {
        throw new IllegalStateException();
    }

    /**
     * Implements CKY algorithm with unary production. Works on grammar in Chomsky Normal Form (with unary production
     * rules permitted).
     *
     * @return most probable parse
     */
    public static Score getBestParseTree(List<Terminal> words, Grammar grammar, NonTerminal goal) {
        if (!grammar.isInChomskyNormalFormWithUnaries()) throw new InvalidParameterException();

        Vector<Vector<Map<NonTerminal, Score>>> scoreMap = handleTerminals(words, grammar);
        handleNonTerminals(words, grammar, scoreMap);

        // TODO what if nothing found?
        return scoreMap
                .get(0).get(words.size() - 1)
                .get(goal);
    }

    private static Vector<Vector<Map<NonTerminal, Score>>> handleTerminals(List<Terminal> words, Grammar grammar) {
        Vector<Vector<Map<NonTerminal, Score>>> scoreMap = new Vector<>(words.size());
        scoreMap.setSize(words.size());
        Maps.asMap(grammar.variableSet, t -> new Score[words.size()][words.size()]);


        for (int i = 0; i < words.size(); i++) {
            scoreMap.set(i, new Vector<>(words.size()));
            scoreMap.get(i).setSize(words.size());

            // Handle terminal rules
            scoreMap.get(i).set(i, new HashMap<>(grammar.variableSet.size()));
            for (Rule nt : grammar.terminals.get(words.get(i))) {
                addScore(scoreMap, i, i, nt.getLHS(), new Score(nt, (Terminal) nt.getRHS().get(0)));
            }

            // Handle unary rules
            handleUnaryRules(grammar, scoreMap, i, i);
        }


        return scoreMap;
    }

    private static boolean addScore(Vector<Vector<Map<NonTerminal, Score>>> scoreMap, int i, int j, NonTerminal type, Score s) {
        if (s.probability == 0.0)
            System.err.println("Warning: probability of 0 encountered. Underflow has likely occurred.");

        Map<NonTerminal, Score> cell = scoreMap.get(i).get(j);
        if (cell == null) {
            cell = new HashMap<>();
            scoreMap.get(i).set(j, cell);
        }

        if (cell.get(type) == null || s.compareTo(cell.get(type)) > 0) {
            cell.put(type, s);
            return true;
        } else {
            return false;
        }
    }

    private static void handleNonTerminals(List<Terminal> words, Grammar grammar, Vector<Vector<Map<NonTerminal, Score>>> scoreMap) {
        for (int span = 2; span <= words.size(); span++) {
            for (int begin = 0; begin <= words.size() - span; begin++) { // first word
                int end = begin + span; // exclusive end
                // A -> B C
                for (int split = begin + 1; split < end; split++) { // first word of second part

                    // Try out all rules; add those that stick
                    for (Map.Entry<NonTerminal, CKY.Score> B : scoreMap.get(begin).get(split - 1).entrySet()) {
                        for (Map.Entry<NonTerminal, CKY.Score> C : scoreMap.get(split).get(end - 1).entrySet()) {
                            for (Rule r : grammar.binaryProductionRules.get(new Pair<>(B.getKey(), C.getKey()))) {
                                addScore(scoreMap,
                                        begin, end - 1,
                                        r.getLHS(),
                                        new Score(r, B.getValue(), C.getValue()));
                            }
                        }
                    }

                    ///////////////////////////////////////////////////

                    //TODO throw error if no solution found

                    //handle unary rules
                    handleUnaryRules(grammar, scoreMap, begin, end - 1);
                }
            }
        }
    }

    private static void handleUnaryRules(Grammar grammar, Vector<Vector<Map<NonTerminal, Score>>> scoreMap, int i, int j) {
        boolean added = true;
        while (added) {
            added = false;
            Map<NonTerminal, Score> value = scoreMap.get(i).get(j);
            for (Map.Entry<NonTerminal, Score> B : value.entrySet()) {
                for (Rule r : grammar.unaryProductionRules.get(B.getKey())) {
                    //noinspection SuspiciousMethodCalls
                    added = addScore(scoreMap, i, j, r.getLHS(), new Score(r, B.getValue()));
                }
            }
        }
    }

    public static class Score implements Comparable<Score>, TypeScore {
        private final double probability;
        private final List<TypeScore> inputs;
        private final Rule rule;


        protected Score(Rule rule, Score... inputs) {
            ArrayList<TypeScore> ins = Lists.newArrayList(inputs);
            if (inputs.length != rule.getRHS().size()
                    || !rule.getRHS().match(ins.stream().map(TypeScore::getType).collect(Collectors.toList())))
                throw new InvalidParameterException();

            this.rule = rule;

            double prob = rule.getProbability();
            this.probability = rule.getProbability(inputs);
            this.inputs = ins;
        }

        public Score(Rule rule, Terminal terminal) {
            this.rule = rule;
            this.probability = rule.getProbability();
            this.inputs = Collections.singletonList(terminal);
        }

        public Type getType() {
            return rule.getLHS();
        }

        @Override
        public int compareTo(@NotNull Score o) {
            return Double.compare(probability, o.probability);
        }

        @Override
        public String toString() {
            return rule + " (" + probability + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Score score = (Score) o;

            return Double.compare(score.probability, probability) == 0
                    && inputs.equals(score.inputs) && rule.equals(score.rule);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(probability);
            result = (int) (temp ^ (temp >>> 32));
            result = 31 * result + inputs.hashCode();
            result = 31 * result + rule.hashCode();
            return result;
        }

        public double getProbability() {
            return probability;
        }
    }
}
