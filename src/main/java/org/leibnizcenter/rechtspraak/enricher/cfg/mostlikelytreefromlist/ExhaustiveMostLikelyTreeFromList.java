package org.leibnizcenter.rechtspraak.enricher.cfg.mostlikelytreefromlist;

import org.leibnizcenter.rechtspraak.enricher.cfg.mostlikelytreefromlist.abstracts.MostLikelyTreeFromList;
import org.leibnizcenter.rechtspraak.enricher.cfg.mostlikelytreefromlist.interfaces.PenaltyCalculator;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.TaggedToken ;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.util.immutabletree.ImmutableTree;
import org.leibnizcenter.util.immutabletree.NamedImmutableTree;
import org.leibnizcenter.util.immutabletree.LabeledTokenNode;

import java.util.*;

import static org.leibnizcenter.rechtspraak.tagging.Label.*;

/**
 * Finds the most likely tree given a list of tagged tokens
 * Created by maarten on 7-4-16.
 */
public class ExhaustiveMostLikelyTreeFromList extends MostLikelyTreeFromList<TokenTreeLeaf, Label> {
    public ExhaustiveMostLikelyTreeFromList(List<TokenTreeLeaf> tokens, List<Label> labels, PenaltyCalculator penaltyCalculator) {
        super(tokens, labels, penaltyCalculator);
    }



    @Override
    public boolean canAppendChildTo(ImmutableTree childToAdd,
                                    Deque<ImmutableTree> addToPath, int index) {
        Iterator<ImmutableTree> i = addToPath.iterator();
        ImmutableTree nodeToAddTo = i.next();
        ImmutableTree parentOfNodeToAddTo = i.hasNext() ? i.next() : null;

        if (nodeToAddTo instanceof LabeledTokenNode) {
            return false; // Can't add to terminal nodes
        }

        if (childToAdd instanceof LabeledTokenNode) {
            switch (((LabeledTokenNode) childToAdd).token.getTag()) {
                case NEWLINE:
                    // We can add new lines anywhere
                    return true;
                case NR:
                case SECTION_TITLE:
                    // We can only add nr and section nodes to <title> nodes
                    return PenaltyCalculatorImpl.isTitleContainerNode(nodeToAddTo);
                case TEXT_BLOCK:
                    // We can't add text blocks to <title> nodes
                    return !PenaltyCalculatorImpl.isTitleContainerNode(nodeToAddTo);
                default:
                    throw new IllegalStateException();
            }
        }

//        throw new IllegalStateException();
        if (childToAdd instanceof NamedImmutableTree) {
            if (
                    PenaltyCalculatorImpl.isSectionNode(nodeToAddTo)
                    && PenaltyCalculatorImpl.isSectionNode(childToAdd)
                    && PenaltyCalculatorImpl.hasPreviousSection(nodeToAddTo) == null
                    && !PenaltyCalculatorImpl.isPlausibleFirstSectionOfParent(childToAdd, nodeToAddTo)
                    && PenaltyCalculatorImpl.isPlausibleNextSectionOfParent((NamedImmutableTree) childToAdd, parentOfNodeToAddTo)
                    ) {
                return false;
            }

            if (nodeToAddTo instanceof NamedImmutableTree) {
                if (
                        ((NamedImmutableTree) childToAdd).getName().matches("section|#root")
                                && ((NamedImmutableTree) nodeToAddTo).getName().matches("section|#root")
                        ) return true;
            }
        }
        return true; // Allow any other node to move freely
    }

    @Override
    public List<ImmutableTree> getPossibleNodesToAdd(int index) {
        TaggedToken<TokenTreeLeaf, Label> nodeToAdd = new TaggedToken<>(getTokens().get(index), getLabels().get(index));
        final List<ImmutableTree> possibleNodesToAdd = new ArrayList<>(5);
        final LabeledTokenNode labeledTokenNode = new LabeledTokenNode(nodeToAdd);
        switch (nodeToAdd.getTag()) {
            //TODO possible part of a *.info block?
            case NR:
            case SECTION_TITLE:
                // Could either be the start of a new section title (and thus a new section),
                // or part of an existing section title (hence no break)
                possibleNodesToAdd.add(
                        new NamedImmutableTree(TokenTree.TAG_SECTION, null)
                                .addChild(
                                        new NamedImmutableTree(TokenTree.TAG_TITLE, null)
                                                .addChild(labeledTokenNode)
                                ));
            case NEWLINE:
            case TEXT_BLOCK:
                possibleNodesToAdd.add(labeledTokenNode);
                break;
        }
        return possibleNodesToAdd;
    }
}
