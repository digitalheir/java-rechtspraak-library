//package org.leibnizcenter.rechtspraak.enricher.cfg;
///**
// * This class encapsulates Context Free Grammars.
// * Usage: java CFG <grammar_file> [<conversion instruction>]
// * a grammar file is specified as follows:
// * <ul>
// * <li> Each line consists of white-space separated strings which</li>
// * <li> The first string of any line must have length 1 and is a variable</li>
// * <li> Subsequent strings on a given line are the right hand sides
// * of productions from the variable for that line</li>
// * <li> The first variable in the file is the start variable</li>
// * <li> A single double-quote symbol " represents the empty
// * string epsilon
// * (we think of " as two single quotes with nothing in between)</li>
// * <li>  Any character which isn't a variable, is a terminal</li>
// * </ul>
// * For example a*b* with rule-set {S->AB,A->",A->aA,B->",B->bB} is
// * given by the file which looks like:
// * <pre>S AB
// * A " aA
// * B " bB</pre>
// * Conversion Instructions:  There are three possible flags which may not be
// * combined at the moment:  -removeEpsilons, -removeUnits, -makeCNF.
// * If no flags are specified, the program tries to create a CFG and gives some
// * information.
// * <ol>
// * <li>  -removeEpsilons  -- given an arbitrary grammar, modify the grammar to an
// * equivalent grammar with only one allowable epsilon
// * transtion:  start ---> epsilon</li>
// * <li>  -removeUnits     -- given a grammar with no epsilon transitions, except possibly
// * from the start variable, modify the grammar to an equivalent
// * grammar with no unit transitions</li>
// * <li>  -makeCNF         -- given a grammar with no epsilon transitions and no unit
// * transitions, convert into an equivalent grammar in Chomsky
// * Normal Form</li>
// * </ol>
// * removeEpsilons() is tested by hasNoEpsilonsExceptForStart(),
// * removeUnits() is tested by hasUnitTransitions(), and
// * makeCNF() is tested by isChomskyNormalFormGrammar()
// **/
//
//
//import com.google.common.collect.*;
//import org.jetbrains.annotations.NotNull;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.RightHandSide;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.StandardRule;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.interfaces.Rule;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.Term;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.NonTerminalImpl;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.NonTerminal;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.Terminal;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.Type;
//import org.leibnizcenter.util.Collections3;
//
//import java.util.*;
//import java.util.function.Consumer;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * A class that represents a Stochastic Context Free Grammar (SCFG)
// */
//@SuppressWarnings("unused")
//public class Grammar implements Iterable<Term> {
//    final NonTerminal start;
//    final SortedSet<Rule> ruleSet;
//    /**
//     * Maps from left hand side to right hand side
//     */
//    final Multimap<NonTerminal, RightHandSide> ruleMap;
//    /**
//     * Maps from RHS to LHS
//     */
//    final Multimap<Terminal, Rule> terminals;
//    /**
//     * All left-hand sides
//     */
//    final Set<NonTerminal> variableSet;
//
//    /**
//     * Maps from RHS to LHS -> RHS
//     */
//    final Map<NonTerminal, Set<Rule>> unaryProductionRules;
//    /**
//     * Maps from RHS to LHS -> RHS
//     */
//    final Map<NonTerminal, Map<NonTerminal, Set<Rule>>> binaryProductionRules;
//
//    public Grammar(
//            NonTerminal start,
//            Collection<Rule> rules) {
//        this.start = start;
//        this.ruleSet = new TreeSet<>(rules);
//        this.variableSet = rules.stream().map(Rule::getLHS).collect(Collectors.toSet());
//
//        ruleMap = getRuleMap(ruleSet);
//        terminals = getTerminals(ruleSet);
//        binaryProductionRules = getBinaryProductionRules(ruleSet);
//        unaryProductionRules = getUnaryProductionRules(ruleSet);
//    }
//
//    private static Multimap<NonTerminal, RightHandSide> getRuleMap(SortedSet<Rule> ruleSet) {
//        ImmutableMultimap.Builder<NonTerminal, RightHandSide> mmb = new ImmutableMultimap.Builder<>();
//        ruleSet.forEach((r) -> mmb.put(r.getLHS(), r.getRHS()));
//        return mmb.build();
//    }
//
//    private static Map<NonTerminal, Set<Rule>> getUnaryProductionRules(SortedSet<Rule> ruleSet) {
//        Map<NonTerminal, Set<Rule>> mmb3 = new HashMap<>(ruleSet.size());
//        ruleSet.stream()
//                .filter(Rule::isUnaryProduction)
//                .forEach((r) -> {
//                            NonTerminal rhs = (NonTerminal) r.getRHS().get(0);
//                            Set<Rule> set = mmb3.getOrDefault(rhs, new LinkedHashSet<>());
//                            set.add(r);
//                            if (!mmb3.containsKey(rhs)) mmb3.put(rhs, set);
//                        }
//                );
//        return mmb3;
//    }
//
//    private static Map<NonTerminal, Map<NonTerminal, Set<Rule>>> getBinaryProductionRules(SortedSet<Rule> ruleSet) {
//        Map<NonTerminal, Map<NonTerminal, Set<Rule>>> BtoCtoRules = new HashMap<>(ruleSet.size());
//
//        ruleSet.stream()
//                .filter(Rule::isBinaryProduction)
//                .forEach((r) -> {
//                    @NotNull NonTerminal B = (NonTerminal) r.getRHS().get(0);
//                    @NotNull NonTerminal C = (NonTerminal) r.getRHS().get(1);
//                    Map<NonTerminal, Set<Rule>> CtoRules = BtoCtoRules.getOrDefault(B, new HashMap<>());
//                    Set<Rule> rules = CtoRules.getOrDefault(C, new LinkedHashSet<>());
//                    rules.add(r);
//                    if (!CtoRules.containsKey(C)) CtoRules.put(C, rules);
//                    if (!BtoCtoRules.containsKey(B)) BtoCtoRules.put(B, CtoRules);
//                });
//        return BtoCtoRules;
//    }
//
//    private static ImmutableMultimap<Terminal, Rule> getTerminals(SortedSet<Rule> ruleSet) {
//        ImmutableMultimap.Builder<Terminal, Rule> mmb2 = new ImmutableMultimap.Builder<>();
//        ruleSet.stream()
//                .filter(Rule::isTerminal)
//                .forEach((r) -> mmb2.put((Terminal) r.getRHS().get(0), r));
//        return mmb2.build();
//    }
//
//    /**
//     * @return true if given element represents a variable
//     */
//    public boolean hasVariable(NonTerminal c) {
//        return variableSet.contains(c);
//    }
//
////    /**
////     * The following constructor tries to build the
////     * grammar using a supposed filename. It handles
////     * exceptions itself.
////     */
////    public Grammar(File file) throws IOException, MalformedGrammarException {
////        this();
////            FileInputStream fis;
////                fis = new FileInputStream(file);
////                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
////                StringTokenizer tokens;
////                int line_num = 0;
////                char currVar;
////                variableSet = new TreeSet<>();
////
////
////                while (br.ready()) {
////                    //String tok;
////                    String line = br.readLine();
////                    if (line == null) continue; //when line ends immediately in '\n'
////                    tokens = new StringTokenizer(line, " \t");
////                    if (!tokens.hasMoreTokens()) { // empty line
////                        System.out.println("empty");
////                        continue;
////                    }
////
////                    // get the variable
////                    String variable = tokens.nextToken();
////                    if (variable.length() != 1)
////                        throw new MalformedGrammarException();
////                    currVar = variable.charAt(0);
////                    variableSet.add(new Character(currVar));
////
////                    // start variable is at the beginning of the file
////                    if (line_num == 0)
////                        start = currVar;
////
////                    // get the productions
////                    while (tokens.hasMoreTokens()) {
////                        String prod = tokens.nextToken();
////                        if (prod.equals("\"")) // epsilon production
////                            ruleSet.add(new Rule(currVar, ""));
////                        else if (prod.indexOf('\"') != -1) {
////                            throw new MalformedGrammarException();
////                        } else
////                            ruleSet.add(new Rule(currVar, prod));
////                    }
////                    line_num++;
////                }
////                fis.close();
////            makeRuleMap();
////    }
////
////    public void createFile(String filename)
////            throws FileNotFoundException {
////        FileOutputStream fos = null;
////        PrintWriter out = null;
////
////        try {
////            fos = new FileOutputStream(filename);
////            out = new PrintWriter(fos);
////
////            Object[] LHSarray = variableSet.toArray();
////            Character LHS;
////            Object[] RHSarray;
////            for (int i = 0; i < LHSarray.length; i++) {
////                LHS = (Character) LHSarray[i];
////                out.print(LHS);
////                RHSarray = ((ArrayList) ruleMap.get(LHS)).toArray();
////                for (int j = 0; j < RHSarray.length; j++)
////                    out.print(" " + RHSarray[j]);
////                out.print("\n");
////            }
////
////            if (out != null) out.close();
////            if (fos != null) fos.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//
//    public String toString() {
//        StringBuilder temp = new StringBuilder();
//        temp.append("Variable set is {");
//        temp.append(String.join(" , ", variableSet.stream().map(Object::toString).collect(Collectors.toSet())));
//        temp.append("}\nStart variable is ").append(start);
//        for (NonTerminal var : variableSet) {
//            Collection<RightHandSide> al = ruleMap.get(var);
//            temp.append("\n").append(var.toString()).append(" -> ");
//            temp.append(String.join(" | ", al.stream().map(Object::toString).collect(Collectors.toSet())));
//        }
//        return temp.toString();
//    }
//
//    /**
//     * @return true if epsilons transitions exist in the grammar
//     */
//    public boolean hasEpsilons() {
//        return ruleSet.stream().filter(r -> r.getRHS().size() <= 0)
//                .limit(1)
//                .collect(Collectors.toSet()).size() > 0;
//    }
//
//    /**
//     * @return true if only epsilon transition is from the start variable.
//     * This is okay for a Chomsky Normal Form Grammar
//     */
//    public boolean hasNoEpsilonsExceptForStart() {
//        return ruleSet.stream()
//                .filter(this::isNonStartEpsilonRule)
//                .limit(1)
//                .collect(Collectors.toSet()).size() > 0;
//    }
//
//    private boolean isNonStartEpsilonRule(Rule r) {
//        return !r.getLHS().equals(start) && r.getRHS().size() <= 0;
//    }
//
//
//    /**
//     * @return true if the grammar is in Chomsky Normal Form:
//     * <ol>
//     * <li> Only epsilon production is from start variable</li>
//     * <li> No unit productions</li>
//     * <li> All non-terminal production are of length 2 and contain no terminals</li>
//     * <li> The start symbol is not on the right hand side of any production</li>
//     * </ol>
//     */
//    public boolean isInChomskyNormalFormWithUnaries() {
//        return ruleSet.stream()
//                .filter(this::isNotInChomskyNormalFormWithUnaries).limit(1)
//                .count() == 0;
//    }
//
//    private boolean isNotInChomskyNormalFormWithUnaries(Rule r) {
//        RightHandSide rhs = r.getRHS();
//        return isNonStartEpsilonRule(r)
//                || rhs.size() > 2
//                || (rhs.size() == 2 && (rhs.get(0).isTerminal() || rhs.get(1).isTerminal()))
//                || rhs.contains(start);
//    }
//
////    public static void main(String[] args)
////            throws IOException, MalformedGrammarException {
////        if (args.length < 1 || args.length > 2) {
////            System.out.println("Usage: java Parser <grammar_file> [<conversion instruction>]");
////            System.exit(1);
////        }
////
////        String fname = args[0];
////        String conversionInstruction = (args.length == 2) ? args[1] : null;
////
////        // Build the cfg
////        CFG cfg = new CFG(new File(fname));
////        cfg.makeRuleMap();
////
////        fname = (new StringTokenizer(fname, ".")).nextToken();
////        if (conversionInstruction != null) {
////            String suffix = "";
////            if (conversionInstruction.equals("-removeEpsilons")) {
////                cfg.removeEpsilons();
////                suffix = "_noeps";
////            }
////            if (conversionInstruction.equals("-removeUnits")) {
////                cfg.removeUnits();
////                suffix = "_nounits";
////            }
////            if (conversionInstruction.equals("-makeCNF")) {
////                cfg.makeCNF();
////                suffix = "_cnf";
////            }
////            cfg.createFile(fname + suffix + ".cfg");
////        }
////    }
////
////    /**
////     * given an arbitrary grammar, modify the grammar to an
////     * equivalent grammar with only one allowable epsilon
////     * transition:  start ---> epsilon
////     **/
////    public void removeEpsilons() {
////
////	/* Before we do anything, add a new start symbol */
////        Rule newStart = new Rule(new PlaceHolderTerminal(), start);
////        start = newStart.getLHS();
////        ruleSet.add(newStart);
////        makeRuleMap();
////
////
////        Object[] vars = variableSet.toArray();
////        Iterator itr;
////        Rule rule;
////        char symbol;
////        TreeSet tempSet = new TreeSet();
////
////        int test = 0;
////
////        while (!hasNoEpsilonsExceptForStart()) {
////
////            for (int i = 0; i < vars.length; i++) {
////                symbol = ((Character) vars[i]).charValue();
////                System.out.println("i: " + i + "  curr symbol: " + symbol);
////                //try to remove the rule that correspons to this symbol deriving an epsilon
////                //if you successfully remove it, you need to update other symbols...
////                if (ruleSet.remove(new Rule(symbol, ""))) {
////                    //System.out.println("removed epsilon rule for symbol: "+symbol);
////                    itr = ruleSet.iterator();
////                    while (itr.hasNext()) {
////                        rule = ((Rule) itr.next()).copyAndReplaceAll(symbol, "");
////                        if (rule != null) {
////                            //Here I add to the new rule to tempSet, and at the end of the loop
////                            //do a batch add to the ruleSet. The reason for this is that a TreeSet
////                            //is not allowed to change while an iterator is operating on it.
////                            if (!rule.isEpsilonRule() || rule.getLHS() == start)
////                                tempSet.add(rule);
////                        }//if
////                    }//while
////                }//if
////
////            }//for
////
////            //add all the new rules made to the ruleSet
////            ruleSet.addAll(tempSet);
////            tempSet = new TreeSet();
////            //update the ruleMap so that hasNoEpsilonsExceptForStart() will work correctly
////            makeRuleMap();
////        }
////    }
////
////    /**
////     * given a grammar with no epsilon transitions, except possibly
////     * from the start variable, modify the grammar to an equivalent
////     * grammar with no unit transitions
////     *
////     * @throws MalformedGrammarException if there is a non-start
////     *                                   epsilon transition in the input
////     **/
////    public Grammar removeUnits() throws MalformedGrammarException {
////        Set<Rule> newRules = new HashSet<>(ruleSet.size());
////        ruleSet.stream().forEach((rule) -> {
////                    if (rule.isUnitRule() && !rule.isReflexiveRule()) {
////                        if (rule.getRHS().size() != 1 || !(rule.getRHS().get(0) instanceof NonTerminal))
////                            throw new IllegalStateException();
////                        newRules.addAll(ruleMap.get((NonTerminal) rule.getRHS().get(0)).stream()
////                                .map(rhs -> new Rule(rule.getLHS(), rhs))
////                                .collect(Collectors.toSet()));
////                    } else newRules.add(rule);
////                }
////        );
////
////        return new Grammar(start, newRules);
////
////
////        if (!hasNoEpsilonsExceptForStart())
////            throw new MalformedGrammarException("When removing unit transitions" +
////                    " may not have non-start epsilon transitions");
////        Rule rule;
////        Iterator iter1;
////        Iterator iter2;
////
////        TreeSet removeSet; //will be used for batch removal
////        TreeSet addSet; //will be used for batch addition
////
////
////        while (hasUnitTransitions()) {
////
////            removeSet = new TreeSet();
////            addSet = new TreeSet();
////            iter1 = ruleSet.iterator();
////
////            while (iter1.hasNext()) {
////                rule = (Rule) iter1.next();
////                if (rule.isUnitRule()) {
////
////                    char LHS = rule.getRHS().charAt(0);
////                    if (variableSet.contains(new Character(LHS))) {
////                        removeSet.add(rule);
////                    //the new rules I create here are the bounds of the subset.
////                    //the subSet method returns the set that falls with in the range: [rule1,rule2)
////                    //hopefully all rules have RHS's strictly less than "zzzzzzzzzzz"
////
////                    SortedSet subset = ruleSet.subSet(new Rule(LHS, ""), new Rule(LHS, "zzzzzzzzzzzzzzz"));
////                    iter2 = subset.iterator();
////                    while (iter2.hasNext()) {
////                        Rule currRule = (Rule) iter2.next();
////                        Rule newRule = new Rule(rule.getLHS(), currRule.getRHS());
////                        if (!newRule.isReflexiveRule())
////                            addSet.add(newRule);
////
////                    } //while
////                    //}//if
////                }//if
////            }//while
////
////            ruleSet.removeAll(removeSet);
////            ruleSet.addAll(addSet);
////
////            //update the map
////            makeRuleMap();
////
////        }//while hass unit transitions
////
////
////    }
////
//
//    /**
//     * given an arbitrary grammar, return it in CNF
//     * The orderings START,TERM,BIN,DEL,UNIT and START,BIN,DEL,UNIT,TERM lead to the least (i.e. quadratic) blow-up.
//     */
//    public Grammar convertToChomskyNormalFormWithUnaries() throws MalformedGrammarException {
//        if (isInChomskyNormalFormWithUnaries()) return this;
//
//        return CNF_START().CNF_TERM().CNF_BIN().CNF_DEL();//.CNF_UNIT();
//
////        if (hasUnitTransitions() || !hasNoEpsilonsExceptForStart())
////            throw new MalformedGrammarException("When converting to Chomsky Normal Form" +
////                    " must not have non-start epsilon transitions" +
////                    " and must have gotten rid of unit transitions");
////        Iterator iter;
////        TreeSet addSet;
////
////        int test = 0;
////        while (!isInChomskyNormalForm()) {
////
////            iter = ruleSet.iterator();
////            addSet = new TreeSet();
////
////            while (iter.hasNext()) {
////                Rule rule = (Rule) iter.next();
////                String RHS = rule.getRHS();
////
////                if (rule.isEpsilonRule()) continue; //do nothing, it must be the start symbol
////
////                if (rule.isUnitRule()) continue; //do nothing, it must be a terminal
////
////                //if one or both characters in the RHS are terminals we have to process the rule
////                if (RHS.length() == 2) {
////                    char c1 = RHS.charAt(0);
////                    char c2 = RHS.charAt(1);
////                    String newRHS = "";
////
////                    if (!variableSet.contains(new Character(c1))) {
////                        newRHS += addNewRuleIfNeeded(c1 + "", addSet);
////                    } else newRHS += c1;
////
////                    if (!variableSet.contains(new Character(c2))) {
////                        newRHS += addNewRuleIfNeeded(c2 + "", addSet);
////                    } else newRHS += c2;
////
////                    rule.setRHS(newRHS);
////
////                    continue;
////                }//if RHS length == 2
////
////                String substr = RHS.substring(1, RHS.length());
////                rule.setRHS(RHS.charAt(0) + "" + addNewRuleIfNeeded(substr, addSet));
////            }
////            ruleSet.addAll(addSet);
////            makeRuleMap();
////            // System.out.println(toString());
////
////
////        }
////    }
////
////    //used by makeCNF() if it needs to make a new rule.  If it creates
////    //a new rule it will add it to the addSet argument.
////    private char addNewRuleIfNeeded(String RHS, TreeSet addSet) {
////        //System.out.print("addNewRuleIfNeeded: RHS = "+RHS);
////        Iterator iter = variableSet.iterator();
////        ArrayList al;
////        Character symbol;
////        while (iter.hasNext()) {
////            symbol = (Character) iter.next();
////            //System.out.println("Symbol = "+symbol);
////            al = (ArrayList) ruleMap.get(symbol);
////
////            //if the symbol was added to the varlist, but not yet to the
////            //ruleSet or ruleMap, the ArrayList will be null.  The addSet
////            //passed in must then have the rule for this symbol
////            if (al == null) {
////                Iterator iter2 = addSet.iterator();
////                while (iter2.hasNext()) {
////                    Rule tmp = (Rule) iter2.next();
////                    if (tmp.getRHS().equals(RHS))
////                        return tmp.getLHS();
////                }
////                continue;
////            }
////            if (al.size() == 1 && al.get(0).equals(RHS))
////                return symbol.charValue();
////        }
////
////        //hopefully the getUnusedSymbol() method does not run out of
////        //characters return '\0'
////        Rule newRule = new Rule(getUnusedSymbol(), RHS);
////        addSet.add(newRule);
////        return newRule.getLHS();
//    }
//
//    /**
//     * Eliminate ε-rules
//     * An ε-rule is a rule of the form
//     * A → ε,
//     * where A is not the grammar's start symbol.
//     * To eliminate all rules of this form, first determine the set of all non-terminals that derive ε.
//     * Hopcroft and Ullman (1979) call such non-terminals nullable, and compute them as follows:
//     * If a rule A → ε exists, then A is nullable.
//     * If a rule A → X1 ... Xn exists, and each Xi is nullable, then A is nullable, too.
//     * Obtain an intermediate grammar by replacing each rule
//     * A → X1 ... Xn
//     * by all versions with some nullable Xi omitted. By deleting in this grammar each ε-rule, unless its left-hand
//     * side is the start symbol, the transformed grammar is obtained.[2]:90
//     *
//     * @return new Grammar with all epsilon rules deleted, except possibly for the start variable
//     */
//    public Grammar CNF_DEL() {
//        // get all nullable nonterminals
//        Set<Type> nullableNonTerminals = ruleSet.stream().filter(r -> r.getRHS().size() == 0).map(Rule::getLHS).collect(Collectors.toSet());
//        boolean changed;
//        do {
//            changed = addNullables(nullableNonTerminals, ruleSet);
//        } while (changed);
//
//        // Create intermediate grammar where we omit epsilon rules
//        Set<Rule> newRules = possibleRulesWithEpsilonsOmitted(ruleSet, nullableNonTerminals);
//        // Remove all epsilon rules
//        newRules = newRules.stream().filter(r -> r.getLHS().equals(start) || !Rule.isEpsilonRule(r)).collect(Collectors.toSet());
//        return new Grammar(start, newRules);
//    }
//
//    private Set<Rule> possibleRulesWithEpsilonsOmitted(Set<Rule> ruleSet, Set<Type> nullables) {
//        final Set<Rule> newRules = new HashSet<>(ruleSet.size() * 5);
//        ruleSet.forEach(r -> {
//            newRules.add(r);
//
//            // Get all versions with nullable's omitted
//            if (r.getRHS().containsAny(nullables)) {
//                newRules.addAll(r.getRHS()
//                        .enumerateWaysToOmit(nullables).stream().map(rhs -> new StandardRule(r.getLHS(), rhs,
//                                r.getPriorProbability()//TODO probability applied
//                        )).collect(Collectors.toSet()));
//            }
//        });
//        return newRules;
//    }
//
//    private boolean addNullables(Set<Type> nullableNonTerminalsSoFar, Collection<Rule> ruleSet) {
//        boolean changed = false;
//        for (Rule r : ruleSet) {
//            if (!nullableNonTerminalsSoFar.contains(r.getLHS())
//                    && rhsConsistsOfOnlyNullables(nullableNonTerminalsSoFar, r)
//                    ) {
//                nullableNonTerminalsSoFar.add(r.getLHS());
//                changed = true;
//            }
//        }
//        return changed;
//    }
//
//    private boolean rhsConsistsOfOnlyNullables(Set<Type> nullableNonTerminalsSoFar, Rule r) {
//        //noinspection RedundantCast
//        return r.getRHS().stream().filter(a -> a.isTerminal()
//                || !nullableNonTerminalsSoFar.contains((NonTerminal) a)).count() <= 0;
//    }
//
//    /**
//     * BIN: Eliminate right-hand sides with more than 2 nonterminals[edit]
//     * Replace each rule
//     * A → X1 X2 ... Xn
//     * with more than 2 non-terminals X1,...,Xn by rules
//     * A → X1 A1,
//     * A1 → X2 A2,
//     * ... ,
//     * An-2 → Xn-1 Xn,
//     * where Ai are new non-terminal symbols. Again, this doesn't change the grammar's produced language.[2]:93
//     */
//    public Grammar CNF_BIN() {
//        Collection<Rule> newRules = new HashSet<>(ruleSet.size() ^ 2);
//
//        ruleSet.stream().forEach(rule -> {
//                    if (rule.getRHS().size() > 2) {
//                        newRules.addAll(makeBinaryProductionRules(rule, new ArrayList<>(rule.getRHS().size())));
//                    } else {
//                        newRules.add(rule);
//                    }
//                }
//        );
//        return new Grammar(getStartSymbol(), newRules);
//    }
//
//    private Collection<? extends Rule> makeBinaryProductionRules(Rule rule, List<Rule> accumulator) {
//        if (rule.getRHS().hasNonSolitaryTerminal())
//            throw new IllegalStateException("Rule is not supposed to contain terminals at this point");
//
//        if (rule.getRHS().size() > 2) {
//            NonTerminal newNonTerminal = getUnusedNonTerminal(null, Sets.union(variableSet, accumulator.stream().map(Rule::getLHS).collect(Collectors.toSet())));
//            Rule newBinaryRule = new StandardRule(rule.getLHS(), new RightHandSide(rule.getRHS().get(0), newNonTerminal), rule.getPriorProbability());
//
//            List<Type> term = rule.getRHS().getTerm();
//            Rule remainderRule = new StandardRule(newNonTerminal, new RightHandSide(Collections3.subList(term, 1)), 1.0);
//
//            accumulator.add(newBinaryRule);
//            return makeBinaryProductionRules(remainderRule, accumulator);
//        } else {
//            accumulator.add(rule);
//            return accumulator;
//        }
//    }
//
//
//    public NonTerminal getStartSymbol() {
//        return start;
//    }
//
//    /**
//     * START: Eliminate the start symbol from right-hand sides
//     * Introduce a new start symbol S0, and a new rule
//     * S0 → S,
//     * where S is the previous start symbol. This doesn't change the grammar's produced
//     * language, and S0 won't occur on any rule's right-hand side.
//     */
//    public Grammar CNF_START() {
//        Grammar g = this;
//        Type startSymbol = g.getStartSymbol();
//        Set<Rule> newRules = Sets.newHashSet(g.ruleSet);
//
//        NonTerminal newStartSymbol = getUnusedNonTerminal("s0", variableSet);
//        newRules.add(new StandardRule(newStartSymbol, new RightHandSide(startSymbol), 1.0));
//        return new Grammar(newStartSymbol, newRules);
//    }
//
//    @NotNull
//    private NonTerminal getUnusedNonTerminal(String pref, Collection<NonTerminal> existingVars) {
//        NonTerminal newStartSymbol;
//        if (pref == null || existingVars.contains(new NonTerminalImpl(pref))) {
//            do {
//                newStartSymbol = new NonTerminalImpl(UUID.randomUUID().toString());
//            } while (existingVars.contains(newStartSymbol));
//        } else newStartSymbol = new NonTerminalImpl(pref);
//        return newStartSymbol;
//    }
//
//    /**
//     * Eliminate rules with non-solitary terminals
//     * To eliminate each rule
//     * <p>
//     * A → X1 ... a ... Xn
//     * with a terminal symbol a being not the only symbol on the right-hand side,
//     * introduce, for every such terminal, a new non-terminal symbol Na, and a new
//     * rule
//     * <p>
//     * Na → a.
//     * Change every rule
//     * <p>
//     * A → X1 ... a ... Xn
//     * to
//     * <p>
//     * A → X1 ... Na ... Xn.
//     * If several terminal symbols occur on the right-hand side, simultaneously
//     * replace each of them by its associated non-terminal symbol. This doesn't
//     * change the grammar's produced language.
//     */
//    public Grammar CNF_TERM() {
//        Grammar g = this;
//        Collection<Rule> newRules = Sets.newHashSet();
//
//        g.ruleSet.stream()
//                .forEach(r -> {
//                            if (r.getRHS().hasNonSolitaryTerminal()) {
//                                // Replace every solitary terminal with the LHS of a new rule
//                                RightHandSide newRhs = new RightHandSide(r.getRHS().stream()
//                                        .map(t -> {
//                                            if (t.isTerminal()) {
//                                                NonTerminal newNonTerminal = getUnusedNonTerminal("N_" + t.toString(), Sets.union(g.variableSet, newRules.stream().map(Rule::getLHS).collect(Collectors.toSet())));
//                                                Rule newRule = new StandardRule(newNonTerminal, new RightHandSide(t), 1.0);
//                                                newRules.add(newRule);
//                                                return newNonTerminal;
//                                            } else return t;
//                                        })
//                                        .collect(Collectors.toList()));
//                                newRules.add(new StandardRule(r.getLHS(), newRhs,
//                                        r.getPriorProbability()
//                                ));
//                            } else newRules.add(r);
//                        }
//                );
//        return new Grammar(g.start, newRules);
//    }
//
//
//    public Stream<Term> generate(NonTerminal start) {
//        final Iterator iterator = new Iterator();
//        return Stream.generate(iterator::next);
//    }
//
//
//    @Override
//    public java.util.Iterator<Term> iterator() {
//        return new Iterator();
//    }
//
//    @Override
//    public void forEach(Consumer<? super Term> action) {
//
//    }
//
//    @Override
//    public java.util.Spliterator<Term> spliterator() {
//        return null;
//    }
//
//    public Collection<Term> expand(Term term) {
//        Collection<Term> newTerms1 = new HashSet<>();
//        for (int i = 0; i < term.size(); i++) {
//            if (!term.get(i).isTerminal()) newTerms1.addAll(expand(term, i));
//        }
//        return ImmutableSet.copyOf(newTerms1);
//    }
//
//    private Collection<Term> expand(Term term, int index) {
//        Collection<RightHandSide> RHSs = ruleMap.get((NonTerminal) term.get(index));
//        Collection<Term> newTerms = new HashSet<>();
//        for (RightHandSide expandWith : RHSs) {
//            ImmutableList.Builder<Type> builder = ImmutableList.builder();
//            if (index > 0) builder.addAll(term.subList(0, index));
//            builder.addAll(expandWith.getTerm());
//            if (index < term.size() - 1) builder.addAll(term.subList(index + 1, term.size()));
//            Term newTerm = new Term(builder.build());
//            newTerms.add(newTerm);
//        }
//        return ImmutableSet.copyOf(newTerms);
//    }
//
//    public Collection<Rule> getBinaryProductionRules(NonTerminal B, NonTerminal C) {
//        Map<NonTerminal, Set<Rule>> fromCtoRules = binaryProductionRules.get(B);
//        if (fromCtoRules == null) return Collections.emptySet();
//        Set<Rule> rules = fromCtoRules.get(C);
//        return rules == null ? Collections.emptySet() : rules;
//    }
//
//    public Set<Rule> getUnaryProductionRules(NonTerminal key) {
//        Set<Rule> v = unaryProductionRules.get(key);
//        return v != null ? v : Collections.emptySet();
//    }
//
//    // TODO return term with probability
//    private class Iterator implements java.util.Iterator<Term> {
//        private final Deque<Term> terms = new ArrayDeque<>();
//
//        public Iterator() {
//            terms.addAll(ruleMap.get(start).stream().map(RightHandSide::getTerm).collect(Collectors.toList()));
//        }
//
//
//        @Override
//        public boolean hasNext() {
//            //todo search more efficiently
//            expandUntilThereIsATerminal();
//            return terms.size() > 0;
//        }
//
//        private void expandUntilThereIsATerminal() {
//            while (terms.size() > 0 && !terms.stream().anyMatch(Term::isTerminal)) {
//                Term expandMe = terms.pop();
//                Collection<Term> expandedMemes = expand(expandMe);
//                terms.addAll(expandedMemes); // Add to end of queue
//                if (expandedMemes.stream().anyMatch(Term::isTerminal)) return; // Expanded to a terminal term!
//            }
//        }
//
//        @Override
//        public Term next() {
//            expandUntilThereIsATerminal();
//            if (terms.size() <= 0)
//                throw new NoSuchElementException();
//            assert terms.stream().anyMatch(Term::isTerminal);
//
//            for (int i = 0; i < terms.size(); i++) { // Make sure we don't loop forever, but throw an exception after we come full circle
//                Term term = terms.pop();
//                if (term.isTerminal())
//                    return new Term(term.stream().map(t -> (Terminal) t).collect(Collectors.toList()));
//                else terms.addLast(term);
//            }
//            throw new IllegalStateException();
//        }
//
//        @Override
//        public void remove() {
//            throw new Error("Not implemented");
//        }
//
//        @Override
//        public void forEachRemaining(Consumer<? super Term> action) {
//            Objects.requireNonNull(action);
//            while (hasNext()) action.accept(next());
//        }
//    }
//
//    //TODO
////    private class Spliterator implements java.util.Spliterator<Term> {
////        @Override
////        public boolean tryAdvance(Consumer<? super Term> action) {
////            return false;
////        }
////
////        @Override
////        public void forEachRemaining(Consumer<? super Term> action) {
////
////        }
////
////        @Override
////        public java.util.Spliterator<Term> trySplit() {
////            return null;
////        }
////
////        @Override
////        public long estimateSize() {
////            return 0;
////        }
////
////        @Override
////        public long getExactSizeIfKnown() {
////            return 0;
////        }
////
////        @Override
////        public int characteristics() {
////            return 0;
////        }
////
////        @Override
////        public boolean hasCharacteristics(int characteristics) {
////            return false;
////        }
////
////        @Override
////        public Comparator<? super Term> getComparator() {
////            return null;
////        }
////    }
//
//}
//
//
//
//
//
//
