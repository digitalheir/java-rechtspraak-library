//package org.leibnizcenter.rechtspraak.enricher.cfg.rule.interfaces;
//
//import org.leibnizcenter.rechtspraak.enricher.cfg.ScoreChart;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.RightHandSide;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.NonTerminal;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.Terminal;
//
///**
// * Rule for use in a CFG
// * Created by Maarten on 2016-04-24.
// */
//@SuppressWarnings("unused")
//public interface Rule {
//
//    /**
//     * Test to see if a rule just produce its own type
//     **/
//    @SuppressWarnings("WeakerAccess")
//    static boolean isReflexiveRule(Rule r) {
//        return (r.getRHS().size() == 1 && r.getLHS().equals(r.getRHS().get(0)));
//    }
//
//    /**
//     * @return whether this rule looks like A -> B, where A and B are non-terminals
//     */
//    static boolean isUnaryProduction(Rule r) {
//        RightHandSide RHS = r.getRHS();
//        return RHS.size() == 1 && RHS.get(0) instanceof NonTerminal;
//    }
//
//    /**
//     * @return whether this rule looks like A -> B C, where A, B and C are non-terminals
//     */
//    static boolean isBinaryProduction(Rule r) {
//        RightHandSide RHS = r.getRHS();
//        return RHS.size() == 2
//                && RHS.get(0) instanceof NonTerminal
//                && RHS.get(1) instanceof NonTerminal;
//    }
//
//    static boolean isEpsilonRule(Rule r) {
//        return r.getRHS().size() == 0;
//    }
//
//    static boolean isTerminal(Rule rule) {
//        return rule.getRHS().size() == 1 && rule.getRHS().get(0) instanceof Terminal;
//    }
//
//
//    NonTerminal getLHS();
//
//    RightHandSide getRHS();
//
//    double getLogProbability(ScoreChart.ParseTreeContainer... inputs);
//    double getPriorProbability();
//
//    ScoreChart.ParseTreeContainer apply(double logProb, ScoreChart.ParseTreeContainer... inputs);
//}
