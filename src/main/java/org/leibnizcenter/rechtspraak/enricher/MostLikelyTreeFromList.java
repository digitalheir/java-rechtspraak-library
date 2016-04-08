package org.leibnizcenter.rechtspraak.enricher;

import com.google.common.collect.ImmutableList;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.Pair;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by maarten on 7-4-16.
 */
public class MostLikelyTreeFromList {

    public static TokenTree getMostLikelyTree(List<TokenTreeLeaf> l, List<Label> labels) {
        ImmutableList<TokenTreeLeaf> titles =
                ImmutableList.copyOf(
                        Collections3.zip(l.stream(), labels.stream())
                                .filter(p -> p.getValue().equals(Label.SECTION_TITLE))
                                .map(Pair::getKey)
                                .collect(Collectors.toList()));


        //TokenTreeLeaf[] arr = titles.toArray(new TokenTreeLeaf[titles.size()]);

        // Make every possible combination of these titles


        recurse(new ImmutableTree(titleToAdd, ImmutableList.of()), 0, Integer.MIN_VALUE, titles);
    }

    private static void recurse(final ImmutableTree treeSoFar,
                                final int scoreSoFar,
                                final int maxSoFar,
                                final ImmutableList<TokenTreeLeaf> remainingTitles) {
        if (remainingTitles.size() > 0) {
            TokenTreeLeaf titleToAdd = remainingTitles.get(0);
            for (Deque<ImmutableTree> possibleParentPaths : getPossibleNodesToAddTo(treeSoFar)) {
                // First out was last in, i.e. the node that we append the given title to
                ImmutableTree addTo = possibleParentPaths.pop();

                if (!isNumberingInSequenceWithLast(addTo.children, titleToAdd)) penalty += 3;
//                if (!isNumberingInSequenceWithSecondToLat(nextChildToAdd)) penalty += 3;
                if (!hasNumberingSameMarkupAsMost(nextChildToAdd)) penalty += 1;
                if (hasSameMarkup(possibleParentPaths.peek(), titleToAdd)) penalty += 2;
                if (isSubsectionOfSameMarkupAndNumbering(nextChildToAdd)) penalty += 5;

                ImmutableTree nextChildToAdd = addTo.addChild(new ImmutableTree(titleToAdd, null));


                while (possibleParentPaths.size() > 0) {
                    ImmutableTree parent = possibleParentPaths.pop();
                    nextChildToAdd = parent.replaceChild(
                            // This will always be the last node, because we need to keep the same linear order of the text
                            // (See implementation of addPossibleNodesToAddAsChild)
                            parent.children.size() - 1,
                            nextChildToAdd
                    );
                }

                ImmutableTree newTree = nextChildToAdd;

                int penalty = 0;


                int newScoreSoFar = scoreSoFar - penalty;

                if (newScoreSoFar) {

                }
            }
            ImmutableList<TokenTreeLeaf> newRemainingTitles
                    = remainingTitles.subList(1, remainingTitles.size());
        }
    }

    private static boolean hasSameMarkup(ImmutableTree n1, TokenTreeLeaf n2) {
        if(n1 == null || n2 == null) return true;

        n1.token
    }

    private static boolean isNumberingInSequenceWithLast(ImmutableList<ImmutableTree> children, TokenTreeLeaf titleToAdd) {
        if (!(titleToAdd instanceof Numbering)) return true;

        Numbering numb = (Numbering) titleToAdd;
        if (Collections3.isNullOrEmpty(children)) return (numb.getNumbering().isFirstNumbering());

        Set<ImmutableTree> prevz = children.reverse().stream()
                .filter(e -> e.token instanceof Numbering)
                .limit(1).collect(Collectors.toSet());
        if (!Collections3.isNullOrEmpty(prevz)) {
            Numbering prev = (Numbering) prevz.iterator().next().token;
            return (numb.getNumbering().isSuccedentOf(prev.getNumbering()));
        } else {
            return numb.getNumbering().isFirstNumbering();
        }
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
