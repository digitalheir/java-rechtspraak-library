package org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.interfaces.PenaltyCalculator;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.SubSectionNumber;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.NamedImmutableTree;
import org.leibnizcenter.rechtspraak.util.immutabletree.LabeledTokenNode;

import java.security.InvalidParameterException;
import java.util.*;
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
        Iterator<ImmutableTree> iterator = addToPath.iterator();
        // First out was last in, i.e. this is the node that we append the given title to
        ImmutableTree addToNode = iterator.next();
        ImmutableTree addToNodeParent = iterator.hasNext() ? iterator.next() : null;

        if (isNewSection(nodeToAdd)) {
            penalty += getSectionPenalty(nodeToAdd, addToPath);
        } else if (nodeToAdd instanceof LabeledTokenNode) {
            switch (((LabeledTokenNode) nodeToAdd).token.getTag()) {
                case NEWLINE:
                    break;
                case NR:
                    penalty += getNrPenalty(addToNode);
                    break;
                case SECTION_TITLE:
                    penalty += getTitlePenalty(addToNode, addToNodeParent);
                    break;
                case TEXT_BLOCK:
                    penalty += getTextPenalty(nodeToAdd, addToNode, addToNodeParent);
                    break;
            }

        }

        //todo more features?

        return penalty;
    }

    private int getTitlePenalty(ImmutableTree addToNode, ImmutableTree addToNodeParent) {
        int penalty = 0;
        if (alreadyContainsTitle(addToNode)) penalty += 1;
        if (!isTitleContainerNode(addToNode)) penalty += 1;
        return penalty;
    }

    private int getNrPenalty(ImmutableTree addToNode) {
        int penalty = 0;
        if (alreadyContainsNr(addToNode)) penalty += 1;
        if (alreadyContainsTitle(addToNode)) penalty += 1;
        if (!isTitleContainerNode(addToNode)) penalty += 1;
        return penalty;
    }

    private boolean alreadyContainsNr(ImmutableTree addToNode) {
        return Collections3.thisOrEmpty(addToNode.getChildren()).stream()
                .filter(PenaltyCalculatorImpl::isNrNode)
                .limit(1)
                .collect(Collectors.toSet()).size() > 0;
    }

    private boolean alreadyContainsTitle(ImmutableTree addToNode) {
        return Collections3.thisOrEmpty(addToNode.getChildren()).stream()
                .filter(PenaltyCalculatorImpl::isTitleTextNode)
                .limit(1)
                .collect(Collectors.toSet()).size() > 0;
    }

    private int getTextPenalty(ImmutableTree nodeToAdd, ImmutableTree addToNode, ImmutableTree addToNodeParent) {
        if (isTitleOrNr(addToNode) || isTitleOrNr(addToNodeParent))
            System.err.println("Should not happen: trying to add a text block to a section number or title");

        int penalty = 0;
//        ImmutableList<ImmutableTree> siblings = addToNode.getChildren();
//
//        // Does not follow text node: penalty + 1
//        if (Collections3.size(siblings) > 0 && !isTextNode(siblings.get(siblings.size() - 1)))
//            penalty += 1;

        // We rather not get text in our title containers
        if (isTitleContainerNode(addToNode))
            penalty += 1;

        if (couldveBeenAddedToLastSectionButIsnt(addToNode))
            penalty += 3;

        return penalty;
    }

    private boolean couldveBeenAddedToLastSectionButIsnt(ImmutableTree addToNode) {
        return (addToNode.getChildren() != null
                && isSectionNode(Collections3.last(addToNode.getChildren())));
    }


    private boolean isTitleOrNr(ImmutableTree node) {
        if (node instanceof NamedImmutableTree) {
            switch (((NamedImmutableTree) node).getName()) {
                case TokenTree.TAG_NR:
                case TokenTree.TAG_TITLE:
                    return true;
            }
        }
        return false;
    }

    private int getSectionPenalty(ImmutableTree nodeToAdd, Deque<ImmutableTree> addToPath) {
        //if(addToNode)

        Iterator<ImmutableTree> iterator = addToPath.iterator();
        // First out was last in, i.e. this is the node that we append the given title to
        ImmutableTree addToNode = iterator.next();
        ImmutableTree parentOfNodeToAddTo = iterator.hasNext() ? iterator.next() : null;

        int penalty = 0;
        NamedImmutableTree titleNodeToAdd = getTitleNodeFromSection(nodeToAdd);
        Numbering numberingToAdd = getNumberingFromTitleNode(titleNodeToAdd);
        NamedImmutableTree previousSection = hasPreviousSection(addToNode);
        if (previousSection != null) {
            // nth section

            List<NamedImmutableTree> sectionChildren = getSectionChildren(addToNode);
            if (!hasTitleSameMarkupAsMostSections(sectionChildren, titleNodeToAdd))
                penalty += 1;


            ImmutableTree lastSection = sectionChildren.get(sectionChildren.size() - 1);
            if (!hasSameMarkup(getTitleNodeFromSection(lastSection), titleNodeToAdd))
                penalty += 1;

            Numbering latestNumbering = getNumberingForSection(previousSection);
            if (!isNumberingInDirectSequence(latestNumbering, numberingToAdd))
                penalty += 1;

            if (isSectionNode(nodeToAdd)) {
                if (!isPlausibleNextSectionInSuccession((NamedImmutableTree) nodeToAdd, addToNode))
                    penalty += 4;
            }
        } else {
            // First section

            penalty += 1;//addToPath.size();  // a priori we don't like to start subsections

            if (!isPlausibleFirstSectionOfParent(nodeToAdd, addToNode)) {
                penalty += 5;
            }

            if (parentOfNodeToAddTo != null) {
                List<NamedImmutableTree> parentSections = getSectionChildren(parentOfNodeToAddTo);
//                if (hasTitleSameMarkupAsMostSections(parentSections, titleNodeToAdd)) //TODO only for special markup
//                    penalty += 1;

                if (!Collections3.isNullOrEmpty(parentSections)) {
                    NamedImmutableTree lastParentSection = parentSections.get(parentSections.size() - 1);
                    if (parentSections.size() > 0 && isNumberingInDirectSequence(getNumberingForSection(lastParentSection), numberingToAdd))
                        penalty += 1;
                }
            }
        }
        return penalty;
    }

    private List<NamedImmutableTree> getSectionChildren(ImmutableTree addToNode) {
        return Collections3.thisOrEmpty(addToNode.getChildren()).stream()
                .filter(PenaltyCalculatorImpl::isSectionNode)
                .map(e -> (NamedImmutableTree) e)
                .collect(Collectors.toList());
    }


    private static NamedImmutableTree getTitleNodeFromSection(ImmutableTree nodeToAdd) {
        return (NamedImmutableTree) nodeToAdd.getChildren().get(0);
    }

    public static NamedImmutableTree hasPreviousSection(ImmutableTree addToNode) {
        ImmutableList<ImmutableTree> children = addToNode.getChildren();
        if (Collections3.isNullOrEmpty(children)) return null;
        for (int i = children.size() - 1; i >= 0; i--) {
            if (PenaltyCalculatorImpl.isSectionNode(children.get(i))) return (NamedImmutableTree) children.get(i);
        }
        return null;
    }

    private static boolean isTitleTextNode(ImmutableTree child) {
        return child instanceof LabeledTokenNode
                && Label.SECTION_TITLE.equals(((LabeledTokenNode) child).token.getTag());
    }

    private boolean isTextNode(ImmutableTree node) {
        return node instanceof LabeledTokenNode
                && Label.TEXT_BLOCK.equals(((LabeledTokenNode) node).token.getTag());
    }


    public static boolean isNrNode(ImmutableTree child) {
        return child instanceof LabeledTokenNode
                && Label.NR.equals(((LabeledTokenNode) child).token.getTag());
    }

    public static boolean isSectionNode(ImmutableTree child) {
        return child instanceof NamedImmutableTree
                && TokenTree.TAG_SECTION.equals(((NamedImmutableTree) child).getName());
    }

    public static boolean isTitleContainerNode(ImmutableTree child) {
        return child instanceof NamedImmutableTree
                && TokenTree.TAG_TITLE.equals(((NamedImmutableTree) child).getName());
    }

    /**
     * @param first
     * @param second
     * @return whether numberings are in direct sequence, so no subsection
     */
    private static boolean isNumberingInDirectSequence(Numbering first, Numbering second) {
        return (second == null && first == null)
                || (second != null
                && first != null
                && second.isSuccedentOf(first)
                && !(isPlausibleFirstSubsection(second))
        );
    }

    public static Numbering getNumberingForSection(NamedImmutableTree section) {
        if (!TokenTree.TAG_SECTION.equals(section.getName())) throw new InvalidParameterException();

        if (Collections3.isNullOrEmpty(section.getChildren())
                || !(section.getChildren().get(0) instanceof NamedImmutableTree)
                || !TokenTree.TAG_TITLE.equals(getTitleNodeFromSection(section).getName())) return null;

        return getNumberingFromTitleNode(getTitleNodeFromSection(section));
    }

    private static Numbering getNumberingFromTitleNode(NamedImmutableTree titleNode) {
        if (!TokenTree.TAG_TITLE.equals(titleNode.getName())) throw new InvalidParameterException();

        Numbering numberingToAdd;
        if (!Collections3.isNullOrEmpty(titleNode.getChildren())
                && titleNode.getChildren().get(0) instanceof LabeledTokenNode
                // Title numbering must be first element of title
                && Label.NR.equals(((LabeledTokenNode) titleNode.getChildren().get(0)).token.getTag())) {
            numberingToAdd = (Numbering) ((LabeledTokenNode) titleNode.getChildren().get(0)).token.getToken();
        } else numberingToAdd = null;
        return numberingToAdd;
    }


    private static NamedImmutableTree getTitleNodeFromSection(NamedImmutableTree titleNode) {
        return (NamedImmutableTree) titleNode.getChildren().get(0);
    }


    private static boolean hasTitleSameMarkupAsMostSections(List<NamedImmutableTree> sectionNodes,
                                                            NamedImmutableTree titleElementToAdd) {
        if (sectionNodes.size() == 0) return true;

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

        return !(Collections3.isNullOrEmpty(n1.getChildren())
                || Collections3.isNullOrEmpty(n2.getChildren()))

                && (
                getEmphasisForTitleTexts(n1).equals(getEmphasisForTitleTexts(n2))
                        || getEmphasisForTitleNr(n1).equals(getEmphasisForTitleNr(n2))
                        || getAllEmphases(allTokenDescendents(n1)).equals(getAllEmphases(allTokenDescendents(n2)))
        );

    }

    private static Set<String> getAllEmphases(List<TokenTreeLeaf> tokens) {
        return tokens.stream().map(TokenTreeLeaf::getEmphasis).reduce(Sets.newHashSet(), Sets::union);
    }

    private static Set<String> getEmphasisForTitleTexts(NamedImmutableTree node) {
        if (!Collections3.isNullOrEmpty(node.getChildren())) {
            return node.getChildren().stream()
                    .filter(child ->
                            child instanceof LabeledTokenNode
                                    && Label.SECTION_TITLE.equals(((LabeledTokenNode) child).token.getTag()))
                    .map(child -> Collections3.thisOrEmpty(((LabeledTokenNode) child).token.getToken().getEmphasis()))
                    .reduce(Sets.newHashSet(), Sets::union);
        }
        return ImmutableSet.of();
    }

    private static Set<String> getEmphasisForTitleNr(NamedImmutableTree node) {
        if (!Collections3.isNullOrEmpty(node.getChildren())) {
            return node.getChildren().stream()
                    .filter(child ->
                            child instanceof LabeledTokenNode
                                    && Label.NR.equals(((LabeledTokenNode) child).token.getTag()))
                    .map(child -> ((LabeledTokenNode) child).token.getToken().getEmphasis())
                    .reduce(Sets.newHashSet(), Sets::union);
        }
        return ImmutableSet.of();
    }

    private static List<TokenTreeLeaf> allTokenDescendents(ImmutableTree node) {
        return allTokenDescendents(node, new ArrayList<>());
    }

    private static List<TokenTreeLeaf> allTokenDescendents(ImmutableTree node, List<TokenTreeLeaf> acc) {
        if (node instanceof LabeledTokenNode) acc.add(((LabeledTokenNode) node).token.getToken());
        else if (!Collections3.isNullOrEmpty(node.getChildren()))
            for (ImmutableTree child : node.getChildren()) allTokenDescendents(child, acc);
        return acc;
    }


    private static boolean isNewSection(ImmutableTree nodeToAdd) {
        return nodeToAdd instanceof NamedImmutableTree
                && TokenTree.TAG_SECTION.equals(((NamedImmutableTree) nodeToAdd).getName());
//                && !Collections3.isNullOrEmpty(nodeToAdd.getChildren())
//                && nodeToAdd.getChildren().size() > 0
//                && nodeToAdd.getChildren().get(0) instanceof NamedImmutableTree
//                && TokenTree.TAG_SECTION.equals((getTitleNodeFromSection(nodeToAdd)).getName());
//        Numbering numb = (Numbering) nodeToAdd;
//        if (Collections3.isNullOrEmpty(children)) return (numb.getNumbering().isFirstNumbering());
//
//        Numbering prev = getPrevNumber(children);
//        if (prev != null) return (numb.getNumbering().isSuccedentOf(prev.getNumbering()));
//        else return numb.getNumbering().isFirstNumbering();
    }

    public static boolean isPlausibleFirstSectionOfParent(ImmutableTree nodeToAdd, ImmutableTree parentNode) {
        if (hasPreviousSection(parentNode) == null
                && isSectionNode(nodeToAdd)) {
            Numbering numberingForSection = PenaltyCalculatorImpl.getNumberingForSection((NamedImmutableTree) nodeToAdd);
            return isFirstNonNumberedSectionInNonNumberedSection(numberingForSection, parentNode)
                    || (numberingForSection != null && (numberingForSection.getNumbering().isFirstNumbering()
                    || isPlausibleFirstSubsection(numberingForSection)))
                    ;
        } else {
            return false;
        }
    }

    private static boolean isFirstNonNumberedSectionInNonNumberedSection(Numbering numberingForSection, ImmutableTree parentNode) {
        return hasPreviousSection(parentNode) == null
                && numberingForSection == null
                && !(isSectionNode(parentNode)
                && getNumberingForSection((NamedImmutableTree) parentNode) == null);
    }

    private static boolean isPlausibleFirstSubsection(Numbering numberingForSection) {
        return numberingForSection.getNumbering() instanceof SubSectionNumber
                && ((SubSectionNumber) numberingForSection.getNumbering()).firstSubsection();
    }

    public static boolean isPlausibleNextSectionOfParent(NamedImmutableTree nodeToAdd, ImmutableTree parentNode) {
        return isPlausibleFirstSectionOfParent(nodeToAdd, parentNode)
                || isPlausibleNextSectionInSuccession(nodeToAdd, parentNode);
    }

    private static boolean isPlausibleNextSectionInSuccession(NamedImmutableTree nodeToAdd, ImmutableTree parentNode) {
        if (!isSectionNode(parentNode) && getNumberingForSection(nodeToAdd) == null) return true;

        NamedImmutableTree prev = hasPreviousSection(parentNode);
        if (prev != null) {
            Numbering prevNum = getNumberingForSection(prev);

            return isNumberingInDirectSequence(prevNum, getNumberingForSection(nodeToAdd));
        } else {
            return false;
        }
    }
//
//    private static Numbering getPrevNumber(ImmutableList<ImmutableTree> children) {
//        Set<ImmutableTree> prevz = children.reverse().stream()
//                .filter(e -> e.token instanceof Numbering)
//                .limit(1).collect(Collectors.toSet());
//        if (!Collections3.isNullOrEmpty(prevz)) return (Numbering) prevz.iterator().next().token;
//        else return null;
//    }
}
