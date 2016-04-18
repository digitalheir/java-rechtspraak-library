package org.leibnizcenter.rechtspraak.cfg;
/**
 * This class encapsulates Context Free Grammars.
 * Usage: java CFG <grammar_file> [<conversion instruction>]
 * a grammar file is specified as follows:
 * <ul>
 * <li> Each line consists of white-space separated strings which</li>
 * <li> The first string of any line must have length 1 and is a variable</li>
 * <li> Subsequent strings on a given line are the right hand sides
 * of productions from the variable for that line</li>
 * <li> The first variable in the file is the start variable</li>
 * <li> A single double-quote symbol " represents the empty
 * string epsilon
 * (we think of " as two single quotes with nothing in between)</li>
 * <li>  Any character which isn't a variable, is a terminal</li>
 * </ul>
 * For example a*b* with rule-set {S->AB,A->",A->aA,B->",B->bB} is
 * given by the file which looks like:
 * <pre>S AB
 * A " aA
 * B " bB</pre>
 * Conversion Instructions:  There are three possible flags which may not be
 * combined at the moment:  -removeEpsilons, -removeUnits, -makeCNF.
 * If no flags are specified, the program tries to create a CFG and gives some
 * information.
 * <ol>
 * <li>  -removeEpsilons  -- given an arbitrary grammar, modify the grammar to an
 * equivalent grammar with only one allowable epsilon
 * transtion:  start ---> epsilon</li>
 * <li>  -removeUnits     -- given a grammar with no epsilon transitions, except possibly
 * from the start variable, modify the grammar to an equivalent
 * grammar with no unit transitions</li>
 * <li>  -makeCNF         -- given a grammar with no epsilon transitions and no unit
 * transitions, convert into an equivalent grammar in Chomsky
 * Normal Form</li>
 * </ol>
 * removeEpsilons() is tested by hasNoEpsilonsExceptForStart(),
 * removeUnits() is tested by hasUnitTransitions(), and
 * makeCNF() is tested by isChomskyNormalFormGrammar()
 **/


import com.google.common.collect.*;
import org.leibnizcenter.rechtspraak.cfg.rule.RightHandSide;
import org.leibnizcenter.rechtspraak.cfg.rule.Rule;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Type;
import org.leibnizcenter.rechtspraak.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A class that represents a Stochastic Context Free Grammar (SCFG)
 */
@SuppressWarnings("unused")
public class Grammar {
    final Type start;
    final SortedSet<Rule> ruleSet;
    /**
     * Maps from left hand side to right hand side
     */
    final Multimap<NonTerminal, RightHandSide> ruleMap;
    /**
     * Maps from RHS to LHS
     */
    final Multimap<Terminal, Rule> terminals;
    /**
     * All left-hand sides
     */
    final Set<NonTerminal> variableSet;

    /**
     * Maps from RHS to LHS -> RHS
     */
    final Multimap<NonTerminal, Rule> unaryProductionRules;
    /**
     * Maps from RHS to LHS -> RHS
     */
    final Multimap<Pair<NonTerminal, NonTerminal>, Rule> binaryProductionRules;

    public Grammar(
            Type start,
            Collection<Rule> rules) {
        this.start = start;
        this.ruleSet = new TreeSet<>(rules);
        this.variableSet = rules.stream().map(Rule::getLHS).collect(Collectors.toSet());

        ruleMap = getRuleMap(ruleSet);
        terminals = getTerminals(ruleSet);
        binaryProductionRules = getBinaryProductionRules(ruleSet);
        unaryProductionRules = getUnaryProductionRules(ruleSet);
    }

    private static Multimap<NonTerminal, RightHandSide> getRuleMap(SortedSet<Rule> ruleSet) {
        ImmutableMultimap.Builder<NonTerminal, RightHandSide> mmb = new ImmutableMultimap.Builder<>();
        ruleSet.forEach((r) -> mmb.put(r.getLHS(), r.getRHS()));
        return mmb.build();
    }

    private static ImmutableMultimap<NonTerminal, Rule> getUnaryProductionRules(SortedSet<Rule> ruleSet) {
        ImmutableMultimap.Builder<NonTerminal, Rule> mmb3 = new ImmutableMultimap.Builder<>();
        ruleSet.stream()
                .filter(Rule::isUnaryProduction)
                .forEach((r) -> mmb3.put((NonTerminal) r.getRHS().get(0), r));
        return mmb3.build();
    }

    private static ImmutableMultimap<Pair<NonTerminal, NonTerminal>, Rule> getBinaryProductionRules(SortedSet<Rule> ruleSet) {
        ImmutableMultimap.Builder<Pair<NonTerminal, NonTerminal>, Rule> mmb4 = new ImmutableMultimap.Builder<>();
        ruleSet.stream()
                .filter(Rule::isBinaryProduction)
                .forEach((r) -> mmb4.put(
                        new Pair<>((NonTerminal) r.getRHS().get(0), (NonTerminal) r.getRHS().get(1)),
                        r)
                );
        return mmb4.build();
    }

    private static ImmutableMultimap<Terminal, Rule> getTerminals(SortedSet<Rule> ruleSet) {
        ImmutableMultimap.Builder<Terminal, Rule> mmb2 = new ImmutableMultimap.Builder<>();
        ruleSet.stream()
                .filter(Rule::isTerminal)
                .forEach((r) -> mmb2.put((Terminal) r.getRHS().get(0), r));
        return mmb2.build();
    }

    /**
     * @return true if given element represents a variable
     */
    public boolean hasVariable(NonTerminal c) {
        return variableSet.contains(c);
    }

//    /**
//     * The following constructor tries to build the
//     * grammar using a supposed filename. It handles
//     * exceptions itself.
//     */
//    public Grammar(File file) throws IOException, MalformedGrammarException {
//        this();
//            FileInputStream fis;
//                fis = new FileInputStream(file);
//                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//                StringTokenizer tokens;
//                int line_num = 0;
//                char currVar;
//                variableSet = new TreeSet<>();
//
//
//                while (br.ready()) {
//                    //String tok;
//                    String line = br.readLine();
//                    if (line == null) continue; //when line ends immediately in '\n'
//                    tokens = new StringTokenizer(line, " \t");
//                    if (!tokens.hasMoreTokens()) { // empty line
//                        System.out.println("empty");
//                        continue;
//                    }
//
//                    // get the variable
//                    String variable = tokens.nextToken();
//                    if (variable.length() != 1)
//                        throw new MalformedGrammarException();
//                    currVar = variable.charAt(0);
//                    variableSet.add(new Character(currVar));
//
//                    // start variable is at the beginning of the file
//                    if (line_num == 0)
//                        start = currVar;
//
//                    // get the productions
//                    while (tokens.hasMoreTokens()) {
//                        String prod = tokens.nextToken();
//                        if (prod.equals("\"")) // epsilon production
//                            ruleSet.add(new Rule(currVar, ""));
//                        else if (prod.indexOf('\"') != -1) {
//                            throw new MalformedGrammarException();
//                        } else
//                            ruleSet.add(new Rule(currVar, prod));
//                    }
//                    line_num++;
//                }
//                fis.close();
//            makeRuleMap();
//    }
//
//    public void createFile(String filename)
//            throws FileNotFoundException {
//        FileOutputStream fos = null;
//        PrintWriter out = null;
//
//        try {
//            fos = new FileOutputStream(filename);
//            out = new PrintWriter(fos);
//
//            Object[] LHSarray = variableSet.toArray();
//            Character LHS;
//            Object[] RHSarray;
//            for (int i = 0; i < LHSarray.length; i++) {
//                LHS = (Character) LHSarray[i];
//                out.print(LHS);
//                RHSarray = ((ArrayList) ruleMap.get(LHS)).toArray();
//                for (int j = 0; j < RHSarray.length; j++)
//                    out.print(" " + RHSarray[j]);
//                out.print("\n");
//            }
//
//            if (out != null) out.close();
//            if (fos != null) fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append("Variable set is {");
        temp.append(String.join(" , ", variableSet.stream().map(Object::toString).collect(Collectors.toSet())));
        temp.append("}\nStart variable is ").append(start);
        for (NonTerminal var : variableSet) {
            Collection<RightHandSide> al = ruleMap.get(var);
            temp.append("\n").append(var.toString()).append(" -> ");
            temp.append(String.join(" | ", al.stream().map(Object::toString).collect(Collectors.toSet())));
        }
        return temp.toString();
    }

    /**
     * @return true if epsilons transitions exist in the grammar
     */
    public boolean hasEpsilons() {
        return ruleSet.stream().filter(r -> r.getRHS().size() <= 0)
                .limit(1)
                .collect(Collectors.toSet()).size() > 0;
    }

    /**
     * @return true if only epsilon transition is from the start variable.
     * This is okay for a Chomsky Normal Form Grammar
     */
    public boolean hasNoEpsilonsExceptForStart() {
        return ruleSet.stream()
                .filter(this::isNonStartEpsilonRule)
                .limit(1)
                .collect(Collectors.toSet()).size() > 0;
    }

    private boolean isNonStartEpsilonRule(Rule r) {
        return !r.getLHS().equals(start) && r.getRHS().size() <= 0;
    }


    /**
     * @return true if the grammar is in Chomsky Normal Form:
     * <ol>
     * <li> Only epsilon production is from start variable</li>
     * <li> No unit productions</li>
     * <li> All non-terminal production are of length 2 and contain no terminals</li>
     * <li> The start symbol is not on the right hand side of any production</li>
     * </ol>
     */
    public boolean isInChomskyNormalFormWithUnaries() {
        return ruleSet.stream()
                .filter(this::isNotInChomskyNormalFormWithUnaries).limit(1)
                .count() == 0;
    }

    private boolean isNotInChomskyNormalFormWithUnaries(Rule r) {
        RightHandSide rhs = r.getRHS();
        return isNonStartEpsilonRule(r)
                || rhs.size() > 2
                || (rhs.size() == 2 && (rhs.get(0).isTerminal() || rhs.get(1).isTerminal()))
                || rhs.contains(start);
    }

//    public static void main(String[] args)
//            throws IOException, MalformedGrammarException {
//        if (args.length < 1 || args.length > 2) {
//            System.out.println("Usage: java Parser <grammar_file> [<conversion instruction>]");
//            System.exit(1);
//        }
//
//        String fname = args[0];
//        String conversionInstruction = (args.length == 2) ? args[1] : null;
//
//        // Build the cfg
//        CFG cfg = new CFG(new File(fname));
//        cfg.makeRuleMap();
//
//        fname = (new StringTokenizer(fname, ".")).nextToken();
//        if (conversionInstruction != null) {
//            String suffix = "";
//            if (conversionInstruction.equals("-removeEpsilons")) {
//                cfg.removeEpsilons();
//                suffix = "_noeps";
//            }
//            if (conversionInstruction.equals("-removeUnits")) {
//                cfg.removeUnits();
//                suffix = "_nounits";
//            }
//            if (conversionInstruction.equals("-makeCNF")) {
//                cfg.makeCNF();
//                suffix = "_cnf";
//            }
//            cfg.createFile(fname + suffix + ".cfg");
//        }
//    }
//
//    /**
//     * given an arbitrary grammar, modify the grammar to an
//     * equivalent grammar with only one allowable epsilon
//     * transition:  start ---> epsilon
//     **/
//    public void removeEpsilons() {
//
//	/* Before we do anything, add a new start symbol */
//        Rule newStart = new Rule(new PlaceHolderTerminal(), start);
//        start = newStart.getLHS();
//        ruleSet.add(newStart);
//        makeRuleMap();
//
//
//        Object[] vars = variableSet.toArray();
//        Iterator itr;
//        Rule rule;
//        char symbol;
//        TreeSet tempSet = new TreeSet();
//
//        int test = 0;
//
//        while (!hasNoEpsilonsExceptForStart()) {
//
//            for (int i = 0; i < vars.length; i++) {
//                symbol = ((Character) vars[i]).charValue();
//                System.out.println("i: " + i + "  curr symbol: " + symbol);
//                //try to remove the rule that correspons to this symbol deriving an epsilon
//                //if you successfully remove it, you need to update other symbols...
//                if (ruleSet.remove(new Rule(symbol, ""))) {
//                    //System.out.println("removed epsilon rule for symbol: "+symbol);
//                    itr = ruleSet.iterator();
//                    while (itr.hasNext()) {
//                        rule = ((Rule) itr.next()).copyAndReplaceAll(symbol, "");
//                        if (rule != null) {
//                            //Here I add to the new rule to tempSet, and at the end of the loop
//                            //do a batch add to the ruleSet. The reason for this is that a TreeSet
//                            //is not allowed to change while an iterator is operating on it.
//                            if (!rule.isEpsilonRule() || rule.getLHS() == start)
//                                tempSet.add(rule);
//                        }//if
//                    }//while
//                }//if
//
//            }//for
//
//            //add all the new rules made to the ruleSet
//            ruleSet.addAll(tempSet);
//            tempSet = new TreeSet();
//            //update the ruleMap so that hasNoEpsilonsExceptForStart() will work correctly
//            makeRuleMap();
//        }
//    }
//
//    /**
//     * given a grammar with no epsilon transitions, except possibly
//     * from the start variable, modify the grammar to an equivalent
//     * grammar with no unit transitions
//     *
//     * @throws MalformedGrammarException if there is a non-start
//     *                                   epsilon transition in the input
//     **/
//    public Grammar removeUnits() throws MalformedGrammarException {
//        Set<Rule> newRules = new HashSet<>(ruleSet.size());
//        ruleSet.stream().forEach((rule) -> {
//                    if (rule.isUnitRule() && !rule.isReflexiveRule()) {
//                        if (rule.getRHS().size() != 1 || !(rule.getRHS().get(0) instanceof NonTerminal))
//                            throw new IllegalStateException();
//                        newRules.addAll(ruleMap.get((NonTerminal) rule.getRHS().get(0)).stream()
//                                .map(rhs -> new Rule(rule.getLHS(), rhs))
//                                .collect(Collectors.toSet()));
//                    } else newRules.add(rule);
//                }
//        );
//
//        return new Grammar(start, newRules);
//
//
//        if (!hasNoEpsilonsExceptForStart())
//            throw new MalformedGrammarException("When removing unit transitions" +
//                    " may not have non-start epsilon transitions");
//        Rule rule;
//        Iterator iter1;
//        Iterator iter2;
//
//        TreeSet removeSet; //will be used for batch removal
//        TreeSet addSet; //will be used for batch addition
//
//
//        while (hasUnitTransitions()) {
//
//            removeSet = new TreeSet();
//            addSet = new TreeSet();
//            iter1 = ruleSet.iterator();
//
//            while (iter1.hasNext()) {
//                rule = (Rule) iter1.next();
//                if (rule.isUnitRule()) {
//
//                    char LHS = rule.getRHS().charAt(0);
//                    if (variableSet.contains(new Character(LHS))) {
//                        removeSet.add(rule);
//                    //the new rules I create here are the bounds of the subset.
//                    //the subSet method returns the set that falls with in the range: [rule1,rule2)
//                    //hopefully all rules have RHS's strictly less than "zzzzzzzzzzz"
//
//                    SortedSet subset = ruleSet.subSet(new Rule(LHS, ""), new Rule(LHS, "zzzzzzzzzzzzzzz"));
//                    iter2 = subset.iterator();
//                    while (iter2.hasNext()) {
//                        Rule currRule = (Rule) iter2.next();
//                        Rule newRule = new Rule(rule.getLHS(), currRule.getRHS());
//                        if (!newRule.isReflexiveRule())
//                            addSet.add(newRule);
//
//                    } //while
//                    //}//if
//                }//if
//            }//while
//
//            ruleSet.removeAll(removeSet);
//            ruleSet.addAll(addSet);
//
//            //update the map
//            makeRuleMap();
//
//        }//while hass unit transitions
//
//
//    }
//
//    /**
//     * given an arbitrary grammar, return it in CNF
//     */
//    public Grammar convertToChomskyNormalForm() throws MalformedGrammarException {
//    if(isInCNF())return this;

//        if (hasUnitTransitions() || !hasNoEpsilonsExceptForStart())
//            throw new MalformedGrammarException("When converting to Chomsky Normal Form" +
//                    " must not have non-start epsilon transitions" +
//                    " and must have gotten rid of unit transitions");
//        Iterator iter;
//        TreeSet addSet;
//
//        int test = 0;
//        while (!isInChomskyNormalForm()) {
//
//            iter = ruleSet.iterator();
//            addSet = new TreeSet();
//
//            while (iter.hasNext()) {
//                Rule rule = (Rule) iter.next();
//                String RHS = rule.getRHS();
//
//                if (rule.isEpsilonRule()) continue; //do nothing, it must be the start symbol
//
//                if (rule.isUnitRule()) continue; //do nothing, it must be a terminal
//
//                //if one or both characters in the RHS are terminals we have to process the rule
//                if (RHS.length() == 2) {
//                    char c1 = RHS.charAt(0);
//                    char c2 = RHS.charAt(1);
//                    String newRHS = "";
//
//                    if (!variableSet.contains(new Character(c1))) {
//                        newRHS += addNewRuleIfNeeded(c1 + "", addSet);
//                    } else newRHS += c1;
//
//                    if (!variableSet.contains(new Character(c2))) {
//                        newRHS += addNewRuleIfNeeded(c2 + "", addSet);
//                    } else newRHS += c2;
//
//                    rule.setRHS(newRHS);
//
//                    continue;
//                }//if RHS length == 2
//
//                String substr = RHS.substring(1, RHS.length());
//                rule.setRHS(RHS.charAt(0) + "" + addNewRuleIfNeeded(substr, addSet));
//            }
//            ruleSet.addAll(addSet);
//            makeRuleMap();
//            // System.out.println(toString());
//
//
//        }
//    }
//
//    //used by makeCNF() if it needs to make a new rule.  If it creates
//    //a new rule it will add it to the addSet argument.
//    private char addNewRuleIfNeeded(String RHS, TreeSet addSet) {
//        //System.out.print("addNewRuleIfNeeded: RHS = "+RHS);
//        Iterator iter = variableSet.iterator();
//        ArrayList al;
//        Character symbol;
//        while (iter.hasNext()) {
//            symbol = (Character) iter.next();
//            //System.out.println("Symbol = "+symbol);
//            al = (ArrayList) ruleMap.get(symbol);
//
//            //if the symbol was added to the varlist, but not yet to the
//            //ruleSet or ruleMap, the ArrayList will be null.  The addSet
//            //passed in must then have the rule for this symbol
//            if (al == null) {
//                Iterator iter2 = addSet.iterator();
//                while (iter2.hasNext()) {
//                    Rule tmp = (Rule) iter2.next();
//                    if (tmp.getRHS().equals(RHS))
//                        return tmp.getLHS();
//                }
//                continue;
//            }
//            if (al.size() == 1 && al.get(0).equals(RHS))
//                return symbol.charValue();
//        }
//
//        //hopefully the getUnusedSymbol() method does not run out of
//        //characters return '\0'
//        Rule newRule = new Rule(getUnusedSymbol(), RHS);
//        addSet.add(newRule);
//        return newRule.getLHS();
//    }


    public Type getStartSymbol() {
        return start;
    }


}






