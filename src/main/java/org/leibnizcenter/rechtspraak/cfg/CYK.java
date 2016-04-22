package org.leibnizcenter.rechtspraak.cfg;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.cfg.rule.Rule;
import org.leibnizcenter.rechtspraak.cfg.rule.TypeContainer;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Type;
import org.leibnizcenter.rechtspraak.util.MutableMatrix;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements CKY algorithm with unary production. Works on grammar in Chomsky Normal Form (with unary production
 * rules permitted).
 * Created by maarten on 17-4-16.
 */
public class CYK {
    /**
     * Don't instantiate
     */
    private CYK() {
        throw new IllegalStateException();
    }

    /**
     * Implements CKY algorithm with unary production. Works on grammar in Chomsky Normal Form (with unary production
     * rules permitted).
     *
     * @return most probable parse
     */
    public static ParseTreeContainer getBestParseTree(List<Terminal> words, Grammar grammar, NonTerminal goal) {
        MutableMatrix<Map<NonTerminal, ParseTreeContainer>> scoreMap = getParseTrees(words, grammar);

        // TODO what if nothing found?
        return scoreMap
                .get(0, words.size() - 1)
                .get(goal);
    }

    public static MutableMatrix<Map<NonTerminal, ParseTreeContainer>> getParseTrees(List<Terminal> words, Grammar grammar) {
        if (!grammar.isInChomskyNormalFormWithUnaries())
            throw new InvalidParameterException("Given grammar should be in Chomsky normal form (unaries are allowed)");


        MutableMatrix<Map<NonTerminal, ParseTreeContainer>> mutable = new MutableMatrix<>(words.size(), words.size());
        for (int i = 0; i < words.size(); i++)
            for (int j = i; j >= i && j < words.size(); j++)
                mutable.set(i, j, new HashMap<>(grammar.variableSet.size()));

        handleTerminals(words, grammar, mutable);
        handleNonTerminals(words, grammar, mutable);
        return mutable;
    }

    private static void handleTerminals(List<Terminal> words,
                                        Grammar grammar,
                                        MutableMatrix<Map<NonTerminal, ParseTreeContainer>> scoreMap) {
        // Init score keeper
        Vector<Map<NonTerminal, ParseTreeContainer>> scoresToAdd = new Vector<>(words.size());
        for (int i = 0; i < words.size(); i++) scoresToAdd.add(new HashMap<>(grammar.variableSet.size()));

        ////////////

        // Handle terminal rules
        for (int i = 0; i < words.size(); i++) {
            Terminal terminal = words.get(i);
            for (Rule nt : grammar.terminals.get(terminal)) {
                // Add all terminals that can be made, IF they are higher than the current score
                addScore(scoresToAdd.get(i), new ParseTreeContainer(nt, terminal));
            }
        }

        // Add scores
        for (int i = 0; i < words.size(); i++) {
            scoreMap.set(i, i, scoresToAdd.get(i));
        }

        // Handle unary rules
        for (int i = 0; i < words.size(); i++) {
            handleUnaryRules(grammar, scoreMap.get(i, i));
        }
    }

    private static boolean addScore(Map<NonTerminal, ParseTreeContainer> cell, ParseTreeContainer score) {
        assert cell != null;

        NonTerminal result = score.getResult();
        if ((cell.get(result) == null || score.compareTo(cell.get(result)) > 0)) {
            cell.put(result, score);
            return true;
        } else {
            return false;
        }
    }


    private static void handleNonTerminals(List<Terminal> words,
                                           Grammar grammar,
                                           MutableMatrix<Map<NonTerminal, ParseTreeContainer>> builder) {
        for (int span = 2; span <= words.size(); span++) {
            int numberOfSpans  = words.size()-span+1;
            int numberOfSplits  = span-1;
            System.out.println(span + " / " + words.size() + " : "+(numberOfSpans*numberOfSplits));
            for (int begin = 0; begin <= words.size() - span; begin++) { // first word
                int end = begin + span; // exclusive end
                Map<NonTerminal, ParseTreeContainer> cell = new HashMap<>(grammar.variableSet.size());
                builder.set(begin, end - 1, cell);
                // A -> B C
                for (int split = begin + 1; split < end; split++) { // first word of second part

                    // Try out all rules; add those that stick
                    Set<Map.Entry<NonTerminal, ParseTreeContainer>> possibleBValues = builder.get(begin, split - 1).entrySet();//ImmutableSet.copyOf(  );
                    //System.out.println("|B| = " + possibleBValues.size());
                    for (Map.Entry<NonTerminal, ParseTreeContainer> B : possibleBValues) {
                        Set<Map.Entry<NonTerminal, ParseTreeContainer>> possibleCValues = builder.get(split, end - 1).entrySet();//ImmutableSet.copyOf(  );
                        //System.out.println("|C| = " + possibleCValues.size());
                        for (Map.Entry<NonTerminal, ParseTreeContainer> C : possibleCValues) {
                            for (Rule r : grammar.getBinaryProductionRules(B.getKey(), C.getKey())) {
                                addScore(cell, new ParseTreeContainer(r, B.getValue(), C.getValue()));
                            }
                        }
                    }

                    ///////////////////////////////////////////////////

//                    if (cell.size() == 0) {
//                        System.err.println("WARNING: No rules found ");
//                    }
                    //handle unary rules
                    handleUnaryRules(grammar, cell);
                }
            }
        }
    }

    private static void handleUnaryRules(Grammar grammar, Map<NonTerminal, ParseTreeContainer> map) {
        boolean added;
        do {
            added = false;
            Set<Map.Entry<NonTerminal, ParseTreeContainer>> entries = map.entrySet();
            Collection<ParseTreeContainer> toAdd = new ArrayList<>(entries.size());


            // Find all applicable unary rules
            for (Map.Entry<NonTerminal, ParseTreeContainer> B : entries) {
                toAdd.addAll(
                        grammar.unaryProductionRules.get(B.getKey()).stream()
                                .map(r -> new ParseTreeContainer(r, B.getValue()))
                                .collect(Collectors.toList())
                );
            }

            // See if we can add the results
            for (ParseTreeContainer ptc : toAdd) if (addScore(map, ptc)) added = true;

        } while (added);
    }

    public static ParseTreeContainer getBestParseTree(List<Terminal> words, Grammar dg) {
        return getBestParseTree(words, dg, dg.getStartSymbol());
    }

    public static class ParseTreeContainer implements Comparable<ParseTreeContainer>, TypeContainer {
        private final double logProbability;
        private final List<TypeContainer> inputs;
        private final Rule rule;


        protected ParseTreeContainer(Rule rule, ParseTreeContainer... inputs) {
            ArrayList<TypeContainer> ins = Lists.newArrayList(inputs);
            if (inputs.length != rule.getRHS().size()
                    || !rule.getRHS().match(ins.stream().map(TypeContainer::getType).collect(Collectors.toList())))
                throw new InvalidParameterException();

            this.rule = rule;

            this.logProbability = rule.getLogProbability(inputs);
            this.inputs = ins;
        }

        public ParseTreeContainer(Rule rule, Terminal terminal) {
            this.rule = rule;
            this.logProbability = rule.getLogProbability();
            this.inputs = Collections.singletonList(terminal);
        }

        public Type getType() {
            return rule.getLHS();
        }

        public NonTerminal getResult() {
            return rule.getLHS();
        }

        @Override
        public int compareTo(@NotNull ParseTreeContainer o) {
            return Double.compare(logProbability, logProbability);
        }

        @Override
        public String toString() {
            return rule + " (e^" + logProbability + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ParseTreeContainer that = (ParseTreeContainer) o;

            return Double.compare(that.logProbability, logProbability) == 0
                    && inputs.equals(that.inputs)
                    && rule.equals(that.rule);

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(logProbability);
            result = (int) (temp ^ (temp >>> 32));
            result = 31 * result + inputs.hashCode();
            result = 31 * result + rule.hashCode();
            return result;
        }

        public double getLogProbability() {
            return logProbability;
        }
    }
}
