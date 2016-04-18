package org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.abstracts;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.interfaces.PenaltyCalculator;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.NamedImmutableTree;

import java.util.*;


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

        // Make every possible combination of the tokens
        NamedImmutableTree root = new NamedImmutableTree("#root", ImmutableList.of());
        Map.Entry<ImmutableTree, Integer> startingTree = Maps.immutableEntry(root, 0);
        Map.Entry<ImmutableTree, Integer> maxTreeStarting = Maps.immutableEntry(null, Integer.MIN_VALUE);

        Map.Entry<ImmutableTree, Integer> bestTree = recurse(
                startingTree, // start w/ empty tree
                maxTreeStarting,
                0
        );
        return bestTree.getKey();
    }

    private Map.Entry<ImmutableTree, Integer> recurse(final Map.Entry<ImmutableTree, Integer> treeSoFar,
                                                      Map.Entry<ImmutableTree, Integer> maxSoFar,
                                                      int index) {
        if (index >= tokens.size()) return max(treeSoFar, maxSoFar);// No more title to add; return maximum
        else {
            // else... Recurse into all possibilities depth-first, returning the tree with the highest score

            for (Map.Entry<ImmutableTree, Integer> possibleTreeStub : getPossibleNewSubTrees(treeSoFar,
                    getPossibleNodesToAdd(index), index)) {
                /**
                 * Only keep searching while currentScore > maxSoFar. When currentScore <= maxSoFar,
                 * it will never improve because we will subtract penalty >= 0 in every recursion.
                 * So we can already discard it.
                 */
                if (possibleTreeStub.getValue() > maxSoFar.getValue()) {
                    maxSoFar = recurse(
                            possibleTreeStub,
                            maxSoFar,
                            index + 1);
//                    System.out.println(index + ": Max so far: " + maxSoFar.getValue() + " (" + ")");
//                }else{
//                    System.out.println("nvm");
                }
            }
            return maxSoFar;
        }
    }


    /**
     * @param treeSoFar          Tree so far, to which to add a node in every way possible
     * @param possibleNodesToAdd Nodes to maybe add to treeSoFar
     * @return Trees that we can make from treeSoFar, adding tokenToAdd, along with its score.
     * The result will be domain-specific. Order is important for computational reasons: we
     * should return the most likely tree first (as a local optimum), measured by their penalty.
     */
    public List<Map.Entry<ImmutableTree, Integer>> getPossibleNewSubTrees(
            Map.Entry<ImmutableTree, Integer> treeSoFar,
            List<ImmutableTree> possibleNodesToAdd, int index) {
        List<Map.Entry<ImmutableTree, Integer>> possibleNewTrees = new ArrayList<>(possibleNodesToAdd.size() * 10);
        possibleNodesToAdd.forEach((nodeToAdd) ->
                getPossiblePathsToAddTo(nodeToAdd, treeSoFar.getKey(), index).forEach((possibleParentPath) -> {
                    int penalty = getPenaltyCalculator().getPenalty(possibleParentPath, nodeToAdd);
                    if (penalty < 0) throw new Error("Penalty must be >= 0, or we can't guarantee optimizations");
                    ImmutableTree newTree = addToTree(nodeToAdd, possibleParentPath);
                    possibleNewTrees.add(Maps.immutableEntry(newTree, treeSoFar.getValue() - penalty));
                }));
        possibleNewTrees.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));
        return possibleNewTrees;
    }

    /**
     * @param nodeToAdd Node to add to the tree path specified by addToPath
     * @param addToPath tree path; add given node to first element (pop)
     * @return Final element in addToPath, with nodeToAdd added to the tree
     */
    protected static ImmutableTree addToTree(ImmutableTree nodeToAdd, Deque<ImmutableTree> addToPath) {
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


    /**
     * @param nodeToAdd
     * @param root
     * @return The possible paths that we append the given node to.
     */
    protected Deque<Deque<ImmutableTree>> getPossiblePathsToAddTo(ImmutableTree nodeToAdd, ImmutableTree root, int index) {
        return getPossiblePathsToAddTo(
                nodeToAdd,
                root,
                index,
                new ArrayDeque<>(root.depth(ImmutableTree.Depth.RightMostBranches)),
                null
        );
    }

    /**
     * Recursively adds right-paths to <code>accumulator</code>, starting with given root node
     *
     * @param nodeToCheck Some node in a right-branching path from the root
     * @param accumulator List that collects possible paths to append the given child to
     * @return The possible paths that we append the given node to
     */
    private Deque<Deque<ImmutableTree>> getPossiblePathsToAddTo(
            ImmutableTree nodeToAdd,
            ImmutableTree nodeToCheck,
            int index,
            Deque<Deque<ImmutableTree>> accumulator,
            Deque<ImmutableTree> addedLast) {
        // Make next contender path to append node to
        ArrayDeque<ImmutableTree> nextContender;
        if (addedLast != null) nextContender = new ArrayDeque<>(addedLast);
        else nextContender = new ArrayDeque<>();
        nextContender.push(nodeToCheck);

        // Check if this is a valid path to add the node to
        if (canAppendChildTo(nodeToAdd, nextContender, index)) {
            accumulator.push(nextContender);
        }

        // If this node had children, check whether the right-most child is also a valid contender
        if (!Collections3.isNullOrEmpty(nodeToCheck.getChildren())) {
            getPossiblePathsToAddTo(
                    nodeToAdd,
                    nodeToCheck.getChildren().get(nodeToCheck.getChildren().size() - 1),
                    index,
                    accumulator,
                    nextContender
            );
        }
//        ik drink sojamelk omdat ik een vrouw wil worden en het smaakt naar dat eetbare klei
        return accumulator;
    }

    /**
     * @param nodeToAdd Node that we may want to append as child to given tree
     * @param addToPath given tree path of which we may wish to append node to children
     * @return Whether we may append nodeToAdd as a child of addAsChildOfNode
     */
    public abstract boolean canAppendChildTo(ImmutableTree nodeToAdd, Deque<ImmutableTree> addToPath, int index);

    /**
     * Expand token to all possible nodes.
     *
     * @param nodeToAdd index for token that we expand into one or more trees
     * @return Might return a single a singleton list of this the given node, or something more complex.
     * For example, when the node to add is a section title, the list would contain a <section> node containing a
     * <title> node.
     */
    public abstract List<ImmutableTree> getPossibleNodesToAdd(int nodeToAdd);


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
