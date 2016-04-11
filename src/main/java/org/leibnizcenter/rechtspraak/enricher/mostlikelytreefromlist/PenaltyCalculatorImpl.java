package org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist;

import com.google.common.collect.ImmutableList;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.interfaces.PenaltyCalculator;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.LabeledToken;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.NamedImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.LabeledTokenNode;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Maarten on 2016-04-11.
 */
public class PenaltyCalculatorImpl implements PenaltyCalculator {


    /**
     * @param addToPath
     * @param nodeToAdd
     * @return a number that reflects how unhappy we are by adding <code>newNode</code> to <code>addToNode</code>, where
     * a higher number is worse.
     */
    @Override
    public int getPenalty(Deque<ImmutableTree> addToPath, ImmutableTree nodeToAdd) {
        int penalty = 0;
        // First out was last in, i.e. this is the node that we append the given title to
        Iterator<ImmutableTree> iterator = addToPath.iterator();
        ImmutableTree addToNode = iterator.next();
        ImmutableTree addToNodeParent = iterator.hasNext() ? iterator.next() : null;

        if (isNewSection(nodeToAdd)) {
            NamedImmutableTree titleNodeToAdd = getTitleNodeFromSection(nodeToAdd);
            Numbering numberingToAdd = getNumberingFromTitleNode(titleNodeToAdd);
            NamedImmutableTree previousSection = hasPreviousSection(addToNode);
            if (previousSection != null) {
                // nth section
                if (!hasTitleSameMarkupAsMostSections(getSectionChildren(addToNode), titleNodeToAdd)) penalty += 1;
                Numbering latestNumbering = getNumberingForSection(previousSection);
                if (!isNumberingInSequence(latestNumbering, numberingToAdd)) penalty += 3;
            } else {
                // First section
                if (!numberingToAdd.getNumbering().isFirstNumbering()) penalty += 3;
            }
        }

//                if (!isNumberingInSequenceWithSecondToLat(nextChildToAdd)) penalty += 3;
//        if (addToNodeParent != null && Collections3.isNullOrEmpty(addToNodeParent.getChildren())) {
//            if (hasSameMarkup(addToNodeParent.token, newNode)) penalty += 2;
//            // isSubsectionOfSameMarkupAndNumberingInSequence
//            if (hasSameMarkup(addToNodeParent.token, newNode) && isNumberingInSequence(parent, newNode)) penalty += 5;
//        }
        return penalty;
    }

    private List<ImmutableTree> getSectionChildren(ImmutableTree addToNode) {
        return addToNode.getChildren().stream()
                .filter(PenaltyCalculatorImpl::isSectionNode)
                .collect(Collectors.toList());
    }


    private static NamedImmutableTree getTitleNodeFromSection(ImmutableTree nodeToAdd) {
        return (NamedImmutableTree) nodeToAdd.getChildren().get(0);
    }

    private static NamedImmutableTree hasPreviousSection(ImmutableTree addToNode) {
        if (Collections3.isNullOrEmpty(addToNode.getChildren())) return null;
        Iterator<ImmutableTree> prevSection = addToNode.getChildren()
                .reverse()// todo we can make this somewhat faster by iterating from size()-1 to 0
                .stream()
                .filter(PenaltyCalculatorImpl::isSectionNode)
                .limit(1).collect(Collectors.toSet()).iterator();
        return prevSection.hasNext() ? (NamedImmutableTree) prevSection.next() : null;
    }

    private static boolean isSectionNode(ImmutableTree child) {
        return child instanceof NamedImmutableTree
                && TokenTree.TAG_SECTION.equals(((NamedImmutableTree) child).getName());
    }

    private static boolean isNumberingInSequence(Numbering first, Numbering second) {
        return (second == null && first == null)
                || (second != null
                && first != null
                && second.isSuccedentOf(first)
        );
    }

    private static Numbering getNumberingForSection(NamedImmutableTree section) {
        if (!TokenTree.TAG_SECTION.equals(section.getName())) throw new InvalidParameterException();

        if (Collections3.isNullOrEmpty(section.getChildren())
                || !(section.getChildren().get(0) instanceof NamedImmutableTree)
                || !TokenTree.TAG_TITLE.equals((getTitleNodeFromSection(section)).getName()))
            return null;

        return getNumberingFromTitleNode(getTitleNodeFromSection(section));
    }

    private static Numbering getNumberingFromTitleNode(NamedImmutableTree titleNode) {
        if (!TokenTree.TAG_TITLE.equals(titleNode.getName())) throw new InvalidParameterException();

        Numbering numberingToAdd;
        if (!Collections3.isNullOrEmpty(titleNode.getChildren())
                && titleNode.getChildren().get(0) instanceof LabeledTokenNode
                // Title numbering must be first element of title
                && Label.NR.equals(((LabeledTokenNode) titleNode.getChildren().get(0)).token.getTag())) {
            numberingToAdd = (Numbering) getTitleNodeFromSection(titleNode).token.getToken();
        } else numberingToAdd = null;
        return numberingToAdd;
    }

    private static LabeledTokenNode getTitleNodeFromSection(NamedImmutableTree titleNode) {
        return (LabeledTokenNode) titleNode.getChildren().get(0);
    }


    private static boolean hasTitleSameMarkupAsMostSections(List<ImmutableTree> sectionNodes,
                                                            NamedImmutableTree titleElementToAdd) {
        int overHalf = (int) Math.ceil(((double) sectionNodes.size()) / 2.0);
        return sectionNodes.stream()
                .filter(e -> hasSameMarkup(getTitleNodeFromSection(e), titleElementToAdd))
                .limit(overHalf)
                .collect(Collectors.toSet()).size() >= overHalf;
    }


    /**
     * Either both titles have a title text that have the same markup,
     * or both have a number that have the same markup,
     * or other children exist and they have the same markup
     *
     * @param n1
     * @param n2
     * @return
     */
    private static boolean hasSameMarkup(NamedImmutableTree n1, NamedImmutableTree n2) {
        if (!TokenTree.TAG_TITLE.equals(n1.getName())) throw new InvalidParameterException();
        if (!TokenTree.TAG_TITLE.equals(n2.getName())) throw new InvalidParameterException();

        if (Collections3.isNullOrEmpty(n1.getChildren())
                || Collections3.isNullOrEmpty(n2.getChildren())) return false;

        return hasSameMarkup(getTitleTextFrom(n1), getTitleTextFrom(n2))
                || hasSameMarkup(getTitleNrFrom(n1), getTitleNrFrom(n2))
                || hasSameMarkup(allTokenDescendents(n1), allTokenDescendents(n2));
    }

    private static List<LabeledTokenNode> allTokenDescendents(ImmutableTree node) {
        return allTokenDescendents(node, new ArrayList<>());
    }

    private static List<LabeledTokenNode> allTokenDescendents(ImmutableTree node, List<LabeledTokenNode> acc) {
        if (node instanceof LabeledTokenNode) acc.add((LabeledTokenNode) node);
        else if (!Collections3.isNullOrEmpty(node.getChildren()))
            for (ImmutableTree child : node.getChildren()) allTokenDescendents(child, acc);
        return acc;
    }


    private static boolean isNewSection(ImmutableTree nodeToAdd) {
        return nodeToAdd instanceof NamedImmutableTree
                && TokenTree.TAG_SECTION.equals(((NamedImmutableTree) nodeToAdd).getName())
                && !Collections3.isNullOrEmpty(nodeToAdd.getChildren())
                && nodeToAdd.getChildren().size() > 0
                && nodeToAdd.getChildren().get(0) instanceof NamedImmutableTree
                && TokenTree.TAG_SECTION.equals((getTitleNodeFromSection(nodeToAdd)).getName());


//        Numbering numb = (Numbering) nodeToAdd;
//        if (Collections3.isNullOrEmpty(children)) return (numb.getNumbering().isFirstNumbering());
//
//        Numbering prev = getPrevNumber(children);
//        if (prev != null) return (numb.getNumbering().isSuccedentOf(prev.getNumbering()));
//        else return numb.getNumbering().isFirstNumbering();
    }

    private static Numbering getPrevNumber(ImmutableList<ImmutableTree> children) {
        Set<ImmutableTree> prevz = children.reverse().stream()
                .filter(e -> e.token instanceof Numbering)
                .limit(1).collect(Collectors.toSet());
        if (!Collections3.isNullOrEmpty(prevz)) return (Numbering) prevz.iterator().next().token;
        else return null;
    }
}
