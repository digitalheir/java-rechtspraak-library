package org.leibnizcenter.rechtspraak.cfg;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.cfg.rule.RightHandSide;
import org.leibnizcenter.rechtspraak.cfg.rule.Rule;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class CYKTest {

    @Test
    public void getBestParseTree() throws Exception {
        NonTerminal goal = new NonTerminal("Sentence");
        NonTerminal NP = new NonTerminal("NP");
        NonTerminal VP = new NonTerminal("VP");

        List<Terminal> words = Lists.newArrayList("fish men fish".split(" ")).stream()
                .map(Terminal::new)
                .collect(Collectors.toList());

        Collection<Rule> rules = Lists.newArrayList(
                new Rule(goal, new RightHandSide(NP, VP), 1.0),
                new Rule(NP, new RightHandSide(NP, NP), 0.000000000000000001),
                new Rule(NP, new RightHandSide(new Terminal("fish")), 0.0000000000001),
                new Rule(NP, new RightHandSide(new Terminal("men")), 0.000000000000001),
                new Rule(VP, new RightHandSide(new Terminal("fish")), 0.00000000000000001)
        );
        Grammar grammar = new Grammar(goal, rules);

        System.out.println(grammar);

        CYK.ParseTreeContainer bestParseTree = CYK.getBestParseTree(words, grammar, goal);
        Assert.assertNotNull(bestParseTree);
        Assert.assertTrue(Math.exp(bestParseTree.getLogProbability()) > 0.0);
        Assert.assertTrue(Math.exp(bestParseTree.getLogProbability()) < 1.0);
    }

}