package org.leibnizcenter.rechtspraak.enricher.cfg.mostlikelytreefromlist;

import org.leibnizcenter.rechtspraak.enricher.cfg.mostlikelytreefromlist.interfaces.PenaltyCalculator;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.util.immutabletree.ImmutableTree;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Finds the most likely tree given a list of tagged tokens
 * Created by maarten on 7-4-16.
 */
public class GreedyMostLikelyTreeFromList extends ExhaustiveMostLikelyTreeFromList {
    public GreedyMostLikelyTreeFromList(List<TokenTreeLeaf> tokens, List<Label> labels, PenaltyCalculator penaltyCalculator) {
        super(tokens, labels, penaltyCalculator);
    }


    /**
     * @param treeSoFar          Tree so far, to which to add a node in every way possible
     * @param possibleNodesToAdd Nodes to maybe add to treeSoFar
     * @return Single tree with the highest score.
     */
    @Override
    public List<Map.Entry<ImmutableTree, Integer>> getPossibleNewSubTrees(
            Map.Entry<ImmutableTree, Integer> treeSoFar,
            List<ImmutableTree> possibleNodesToAdd, int index) {
        List<Map.Entry<ImmutableTree, Integer>> possibleTrees = super.getPossibleNewSubTrees(treeSoFar, possibleNodesToAdd, index);


        Integer max = possibleTrees.stream().reduce(
                Integer.MIN_VALUE,
                (integer, entry) -> Math.max(integer, entry.getValue()),
                Math::max);

        List<Map.Entry<ImmutableTree, Integer>> possibilities = possibleTrees.stream()
                .filter(e -> Integer.compare(max, e.getValue()) == 0)
                .limit(1)
                .collect(Collectors.toList());
        if (possibilities.size() > 1)
            System.out.println(possibilities.size());
        return possibilities;
    }
}
