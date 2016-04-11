package org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist;

import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.abstracts.MostLikelyTreeFromList;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.interfaces.PenaltyCalculator;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.TaggedToken;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.NamedImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.LabeledTokenNode;

import java.util.*;

/**
 * Finds the most likely tree given a list of tagged tokens
 * Created by maarten on 7-4-16.
 */
public class MostLikelyTreeFromListImpl extends MostLikelyTreeFromList<TokenTreeLeaf, Label> {
    public MostLikelyTreeFromListImpl(List<TokenTreeLeaf> tokens, List<Label> labels, PenaltyCalculator penaltyCalculator) {
        super(tokens, labels, penaltyCalculator);
    }

    @Override
    public boolean canAppendChildTo(ImmutableTree childToAdd,
                                    ImmutableTree nodeToAddTo) {
        if (nodeToAddTo instanceof LabeledTokenNode) {
            return false; // Can't add to terminal nodes
        } else if (
                nodeToAddTo instanceof NamedImmutableTree
                        && TokenTree.TAG_TITLE.equals(((NamedImmutableTree) nodeToAddTo).getName())) {
            // Only allow newline, nr, section title in <title> nodes
            if (!(childToAdd instanceof LabeledTokenNode)) return false;

            switch (((LabeledTokenNode) childToAdd).token.getTag()) {
                case NEWLINE:
                case NR:
                case SECTION_TITLE:
                    return true;
                case TEXT_BLOCK:
                    return false;
                default:
                    throw new IllegalStateException();
            }
        } else return true; // Allow any node in any other node than <title>
    }

    @Override
    public List<ImmutableTree> getPossibleNodesToAdd(TaggedToken<TokenTreeLeaf, Label> nodeToAdd) {
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
