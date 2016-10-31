////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package org.leibnizcenter.cfg.earleyparser;
//
//import org.leibnizcenter.cfg.Grammar;
//import org.leibnizcenter.cfg.category.Category;
//import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;
//import org.leibnizcenter.cfg.earleyparser.parse.ScanProbability;
//import org.leibnizcenter.cfg.token.Token;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.leibnizcenter.cfg.earleyparser.Parser.parseAndCountTokens;
//
//    // TODO delete
//public class Parser2 {
//
//    public static <E> ParseTreeWithScore getViterbiParseWithScore(NonTerminal S, Grammar grammar, Iterable<Token<E>> tokens, ScanProbability scanProb) {
//        Parser.ChartWithInputPosition chart = parseAndCountTokens(S, grammar, tokens, scanProb);
//        List parses = (List) chart.chart.getCompletedStates(chart.index, Category.START).stream().map((state) -> {
//            return new ParseTreeWithScore(Parser.getViterbiParse(state, chart.chart), chart.chart.getViterbiScore(state), grammar.getSemiring());
//        }).collect(Collectors.toList());
//        if (parses.size() > 1) {
//            throw new Error("Found more than one Viterbi parses. This is a bug.");
//        } else {
//            return parses.size() == 0 ? null : (ParseTreeWithScore) parses.get(0);
//        }
//    }
//
//}
