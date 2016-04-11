package org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.abstracts;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.interfaces.PenaltyCalculator;
import org.leibnizcenter.rechtspraak.tokens.TaggedToken;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.NamedImmutableTree;

import java.util.*;
import java.util.stream.Collectors;


public abstract class MostLikelyTreeFromList<TokenType, LabelType> {
    private final List<TokenType> tokens;
    private final List<LabelType> labels;
    private final PenaltyCalculator penaltyCalculator;

    public MostLikelyTreeFromList(List<TokenType> tokens,
                                  List<LabelType> labels,
                                  PenaltyCalculator penaltyCalculator) {
        this.tokens = tokens;
        this.labels = labels;
        this.penaltyCalculator = penaltyCalculator;
    }

    private static Map.Entry<ImmutableTree, Integer> max(Map.Entry<ImmutableTree, Integer> a1, Map.Entry<ImmutableTree, Integer> a2) {
        if (a2.getKey() == null
                || a1.getValue() > a2.getValue()) return a1;
        else return a2;
    }

    /**
     * <p>
     * Finds the most likely tree given a list of tagged tokens
     * </p>
     * Created by maarten on 7-4-16.
     */
    public ImmutableTree getMostLikelyTree() {
//        ImmutableList<TokenTreeLeaf> titles =
//                ImmutableList.copyOf(
//                        Collections3.zip(tokens.stream(), labels.stream())
//                                .filter(p -> p.getValue().equals(Label.SECTION_TITLE) || p.getValue().equals(Label.NR))
//                                .map(Pair::getKey)
//                                .collect(Collectors.toList()));

        ImmutableList<TaggedToken<TokenType, LabelType>> taggedTokens =
                ImmutableList.copyOf(
                        Collections3
                                .zip(getTokens().stream(), getLabels().stream()).map(TaggedToken::new)
                                .collect(Collectors.toList()));

        // Make every possible combination of the tokens
        NamedImmutableTree root = new NamedImmutableTree("#root", ImmutableList.of());
        Map.Entry<ImmutableTree, Integer> startingTree = Maps.immutableEntry(root, 0);
        Map.Entry<ImmutableTree, Integer> maxTreeStarting = Maps.immutableEntry(null, Integer.MIN_VALUE);

        Map.Entry<ImmutableTree, Integer> bestTree = recurse(
                startingTree, // start w/ empty tree
                maxTreeStarting,
                taggedTokens
        );
        return bestTree.getKey();
    }

    private Map.Entry<ImmutableTree, Integer> recurse(final Map.Entry<ImmutableTree, Integer> treeSoFar,
                                                      Map.Entry<ImmutableTree, Integer> maxSoFar,
                                                      ImmutableList<TaggedToken<TokenType, LabelType>> remainingNodesToAdd) {
        if (remainingNodesToAdd.size() <= 0) return max(treeSoFar, maxSoFar);// No more title to add; return maximum
        else {
            // else... Recurse into all possibilities depth-first, returning the tree with the highest score
            TaggedToken<TokenType, LabelType> tokenToAdd = remainingNodesToAdd.get(0);
            remainingNodesToAdd = remainingNodesToAdd.subList(1, remainingNodesToAdd.size());

            for (Map.Entry<ImmutableTree, Integer> possibleNewTree : getPossibleNewTrees(treeSoFar, getPossibleNodesToAdd(tokenToAdd))) {
                /**
                 * Only keep searching while currentScore > maxSoFar. When currentScore <= maxSoFar,
                 * it will never improve because we will subtract penalty >= 0 in every recursion.
                 * So we can already discard it.
                 */
                if (possibleNewTree.getValue() > maxSoFar.getValue()) {
                    maxSoFar = recurse(
                            possibleNewTree,
                            maxSoFar,
                            remainingNodesToAdd);
                }
            }
            return maxSoFar;
        }
    }


    /**
     * @param treeSoFar          Tree so far, to which to add a node in every way possible
     * @param possibleNodesToAdd Nodes to maybe add to treeSoFar
     * @return Trees that we can make from treeSoFar, adding tokenToAdd, along with its score.
     * The result will be domain-specific.
     */
    private Collection<Map.Entry<ImmutableTree, Integer>> getPossibleNewTrees(Map.Entry<ImmutableTree, Integer> treeSoFar, List<ImmutableTree> possibleNodesToAdd) {
        Collection<Map.Entry<ImmutableTree, Integer>> possibleNewTrees = new HashSet<>(possibleNodesToAdd.size() * 10);
        possibleNodesToAdd.forEach((nodeToAdd) ->
                getPossiblePathsToAddTo(nodeToAdd, treeSoFar.getKey()).forEach((possibleParentPath) -> {
                    int penalty = getPenaltyCalculator().getPenalty(possibleParentPath, nodeToAdd);
                    if (penalty < 0) throw new Error("Penalty must be >= 0, or we can't guarantee optimizations");
                    ImmutableTree newTree = addToTree(nodeToAdd, possibleParentPath);
                    possibleNewTrees.add(Maps.immutableEntry(newTree, treeSoFar.getValue() - penalty));
                }));
        return possibleNewTrees;
    }

    /**
     * @param nodeToAdd Node to add to the tree path specified by addToPath
     * @param addToPath tree path; add given node to first element (pop)
     * @return Final element in addToPath, with nodeToAdd added to the tree
     */
    private static ImmutableTree addToTree(ImmutableTree nodeToAdd, Deque<ImmutableTree> addToPath) {
        ImmutableTree addTo = addToPath.pop();
        ImmutableTree nextRoot = addTo.addChild(nodeToAdd);
        while (addToPath.size() > 0) {
            ImmutableTree parent = addToPath.pop();
            nextRoot = parent.replaceChild(
                    // This will always be the last node, because we need to keep the same linear order of the text
                    // (See implementation of addPossibleNodesToAddAsChild)
                    parent.getChildren().size() - 1,
                    nextRoot
            );
        }
        return nextRoot;
    }


    private Collection<Deque<ImmutableTree>> getPossiblePathsToAddTo(ImmutableTree nodeToAdd, ImmutableTree root) {
        return getPossiblePathsToAddTo(
                nodeToAdd,
                root,
                new ArrayList<>(root.depth(ImmutableTree.Depth.RightMostBranches))
        );
    }

    /**
     * Recursively adds right-paths to <code>accumulator</code>, starting with given root node
     *
     * @param nodeToCheck Some node in a right-branching path from the root
     * @param accumulator List that collects possible paths to append the given child to
     */
    private Collection<Deque<ImmutableTree>> getPossiblePathsToAddTo(
            ImmutableTree nodeToAdd,
            ImmutableTree nodeToCheck,
            List<Deque<ImmutableTree>> accumulator) {
        // Make next contender path to append node to
        ArrayDeque<ImmutableTree> nextContender;
        if (accumulator.size() > 0) nextContender = new ArrayDeque<>(accumulator.get(accumulator.size() - 1));
        else nextContender = new ArrayDeque<>();
        nextContender.push(nodeToCheck);

        // Check if this is a valid path to add the node to
        if (canAppendChildTo(nodeToAdd, nextContender.peek())) accumulator.add(nextContender);

        // If this node had children, check whether the right-most child is also a valid contender
        if (!Collections3.isNullOrEmpty(nodeToCheck.getChildren())) {
            getPossiblePathsToAddTo(
                    nodeToAdd,
                    nodeToCheck.getChildren().get(nodeToCheck.getChildren().size() - 1),
                    accumulator
            );
        }
        return accumulator;
    }

    /**
     * @param nodeToAdd Node that we may want to append as child to given tree
     * @param addAsChildOfNode given tree of which we may wish to append node to children
     * @return Whether we may append nodeToAdd as a child of addAsChildOfNode
     */
    public abstract boolean canAppendChildTo(ImmutableTree nodeToAdd, ImmutableTree addAsChildOfNode);

    /**
     * Expand token to all possible nodes.
     *
     * @param nodeToAdd token that we expand into one or more trees
     * @return Might return a single a singleton list of this the given node, or something more complex.
     * For example, when the node to add is a section title, the list would contain a <section> node containing a
     * <title> node.
     */
    public abstract List<ImmutableTree> getPossibleNodesToAdd(TaggedToken<TokenType, LabelType> nodeToAdd);


    @SuppressWarnings("WeakerAccess")
    public PenaltyCalculator getPenaltyCalculator() {
        return penaltyCalculator;
    }

    @SuppressWarnings("WeakerAccess")
    public List<LabelType> getLabels() {
        return labels;
    }

    public List<TokenType> getTokens() {
        return tokens;
    }
}
