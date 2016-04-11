package org.leibnizcenter.rechtspraak.enricher;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.Pair;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by maarten on 7-4-16.
 */
public class MostLikelyTreeFromList {

    public static ImmutableTree getMostLikelyTree(List<TokenTreeLeaf> l, List<Label> labels) {
        ImmutableList<TokenTreeLeaf> titles =
                ImmutableList.copyOf(
                        Collections3.zip(l.stream(), labels.stream())
                                .filter(p -> p.getValue().equals(Label.SECTION_TITLE) || p.getValue().equals(Label.NR))
                                .map(Pair::getKey)
                                .collect(Collectors.toList()));

        //TokenTreeLeaf[] arr = titles.toArray(new TokenTreeLeaf[titles.size()]);

        // Make every possible combination of these titles
        return recurse(new ImmutableTree(null, ImmutableList.of()), 0,
                Maps.immutableEntry(null,Integer.MIN_VALUE), titles).getKey();
    }

    private static Map.Entry<ImmutableTree, Integer> recurse(final ImmutableTree treeSoFar,
                                                             final int scoreSoFar,
                                                             Map.Entry<ImmutableTree, Integer> maxSoFar,
                                                             final ImmutableList<TokenTreeLeaf> titles) {
        ImmutableList<TokenTreeLeaf> newRemainingTitles
                = titles.subList(1, titles.size());
        if (titles.size() > 0) {
            TokenTreeLeaf titleToAdd = titles.get(0);

            for (Deque<ImmutableTree> possibleParentPaths : getPossibleNodesToAddTo(treeSoFar)) {
                // First out was last in, i.e. the node that we append the given title to
                ImmutableTree addTo = possibleParentPaths.pop();

                int penalty = getPenalty(possibleParentPaths, addTo, titleToAdd);

                ImmutableTree nextRoot = addTo.addChild(new ImmutableTree(titleToAdd, null));
                while (possibleParentPaths.size() > 0) {
                    ImmutableTree parent = possibleParentPaths.pop();
                    nextRoot = parent.replaceChild(
                            // This will always be the last node, because we need to keep the same linear order of the text
                            // (See implementation of addPossibleNodesToAddAsChild)
                            parent.children.size() - 1,
                            nextRoot
                    );
                }
                ImmutableTree newTree = nextRoot;

                int newScoreSoFar = scoreSoFar - penalty;
                maxSoFar = max(maxSoFar, recurse(newTree, newScoreSoFar, maxSoFar, newRemainingTitles));
            }
            return maxSoFar;
        } else {
            return max(Maps.immutableEntry(treeSoFar, scoreSoFar), maxSoFar);
        }
    }

    private static Map.Entry<ImmutableTree, Integer> max(Map.Entry<ImmutableTree, Integer> a1, Map.Entry<ImmutableTree, Integer> a2) {
        if (a2.getKey() == null
                || a1.getValue() > a2.getValue()) return a1;
        else return a2;
    }

    /**
     * @param parents
     * @param addToNode
     * @param newNode
     * @return a number that reflects how unhappy we are by adding <code>newNode</code> to <code>addToNode</code>, where
     * a higher number is worse.
     */
    private static int getPenalty(Deque<ImmutableTree> parents, ImmutableTree addToNode, TokenTreeLeaf newNode) {
        int penalty = 0;

        //todo more features?

        if (!isNumberingInSequenceWithLast(addToNode.children, newNode)) penalty += 3;
//                if (!isNumberingInSequenceWithSecondToLat(nextChildToAdd)) penalty += 3;
        if (!hasNumberingSameMarkupAsMost(addToNode.children, newNode)) penalty += 1;
        if (Collections3.isNullOrEmpty(parents.peek().children)) {
            TokenTreeLeaf parent = parents.peek().token;
            if (hasSameMarkup(parent, newNode)) penalty += 2;
            // isSubsectionOfSameMarkupAndNumberingInSequence
            if (hasSameMarkup(parent, newNode) && isNumberingInSequence(parent, newNode)) penalty += 5;
        }
        return penalty;
    }

    private static boolean isNumberingInSequence(TokenTreeLeaf n1, TokenTreeLeaf n2) {
        return Numbering.is(n1)
                && Numbering.is(n2)
                && ((Numbering) n2).isSuccedentOf(((Numbering) n1));
    }

    private static boolean hasNumberingSameMarkupAsMost(ImmutableList<ImmutableTree> elements, TokenTreeLeaf compareWith) {
        int overHalf = (int) Math.ceil(((double) elements.size()) / 2.0);
        return elements.stream()
                .filter(e -> hasSameMarkup(e.token, compareWith))
                .limit(overHalf)
                .collect(Collectors.toSet()).size() >= overHalf;
    }

    private static boolean hasSameMarkup(TokenTreeLeaf n1, TokenTreeLeaf n2) {
        return n1 == null || n2 == null || com.google.common.base.Objects.equal(n1.getEmphasis(), n2.getEmphasis());
    }

    private static boolean isNumberingInSequenceWithLast(ImmutableList<ImmutableTree> children, TokenTreeLeaf titleToAdd) {
        if (!(titleToAdd instanceof Numbering)) return true;

        Numbering numb = (Numbering) titleToAdd;
        if (Collections3.isNullOrEmpty(children)) return (numb.getNumbering().isFirstNumbering());

        Numbering prev = getPrevNumber(children);
        if (prev != null) return (numb.getNumbering().isSuccedentOf(prev.getNumbering()));
        else return numb.getNumbering().isFirstNumbering();

    }

    private static Numbering getPrevNumber(ImmutableList<ImmutableTree> children) {
        Set<ImmutableTree> prevz = children.reverse().stream()
                .filter(e -> e.token instanceof Numbering)
                .limit(1).collect(Collectors.toSet());
        if (!Collections3.isNullOrEmpty(prevz)) return (Numbering) prevz.iterator().next().token;
        else return null;
    }


    private static List<Deque<ImmutableTree>> getPossibleNodesToAddTo(ImmutableTree root) {
        int depth = root.depth(Depth.RightMostBranches);
        List<Deque<ImmutableTree>> accumulator = new ArrayList<>(depth);
        addPossibleNodesToAddAsChild(root, new ArrayDeque<>(depth), accumulator);
        return accumulator;
    }

    /**
     * Recursively adds right-paths to <code>accumulator</code>, starting with given root node
     *
     * @param root
     * @param lastDeque
     * @param accumulator
     */
    private static void addPossibleNodesToAddAsChild(ImmutableTree root,
                                                     Deque<ImmutableTree> lastDeque,
                                                     List<Deque<ImmutableTree>> accumulator) {
        ArrayDeque<ImmutableTree> newDeque = new ArrayDeque<>(lastDeque);
        newDeque.push(root);
        accumulator.add(newDeque);
        if (!Collections3.isNullOrEmpty(root.children)) {
            addPossibleNodesToAddAsChild(root.children.get(
                    root.children.size() - 1),
                    newDeque,
                    accumulator
            );
        }
    }


}
