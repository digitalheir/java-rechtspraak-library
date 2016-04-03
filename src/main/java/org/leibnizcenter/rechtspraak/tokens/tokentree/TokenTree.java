package org.leibnizcenter.rechtspraak.tokens.tokentree;

import com.google.common.collect.Maps;
import org.leibnizcenter.rechtspraak.manualannotation.DeterministicTagger;
import org.leibnizcenter.rechtspraak.tokens.Label;
import org.leibnizcenter.rechtspraak.tokens.LabeledToken;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.numbering.ListMarking;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.AlphabeticNumbering;
import org.leibnizcenter.rechtspraak.tokens.quote.Quote;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TextElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.util.Regex;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.leibnizcenter.rechtspraak.tokens.text.IgnoreElement;
import org.w3c.dom.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by maarten on 27-3-16.
 */
public class TokenTree implements TokenTreeVertex {
    private final List<TokenTreeVertex> children;
    private final Node element;

    private static final String TAG_SECTION = "section";
    private static final String TAG_TITLE = "title";
    private static final String TAG_NR = "nr";
    private static final String TAG_TEXT = "text";
    public static final String TAG_POTENTIAL_NR = "potentialnr";
    private static final String TAG_TEXTGROUP = "textgroup";
    private static final String TAG_PARA = "para";
    private static final String TAG_QUOTE = "quote";

    private static final Pattern KNOWN_ELEMENTS = Pattern.compile("(itemized|ordered)list" +
            "|footnote" +
            "|listitem" +
            "|link" +
            "|" + TAG_POTENTIAL_NR +
            "|" + TAG_NR +
            "|" + TAG_TEXT +
            "|" + TAG_TEXTGROUP +
            "|" + TAG_QUOTE +
            "|emphasis" +
            "|section" +
            "|bridgehead" +
            "|(uitspraak|conclusie)\\.info" +
            "|(rs:)?para(block|group)?" +
            "|" + TAG_TITLE +
            "|(informal)?table" +
            "|mediaobject");

    public TokenTree(Element root) {
        element = root;
        children = new ArrayList<>();

        // Make sure we have a reference to all children as they are now; the XML tree might change.
        Node[] originalChildren = getChildren(root);

        for (Node child : originalChildren) {
            switch (child.getNodeType()) {
                case Node.TEXT_NODE:
                    String text = child.getTextContent();
                    if (text.length() != 0 && !Regex.CONSECUTIVE_WHITESPACE.matcher(text).matches()) {
                        // Only process non-whitespace blocks of text
                        children.add(fromTextNode((Text) child));
                    }
                    break;
                case Node.ELEMENT_NODE:
                    children.add(fromElement((Element) child));
                    break;
                case Node.PROCESSING_INSTRUCTION_NODE:
                    children.add(fromProcessingInstruction((ProcessingInstruction) child));
                    break;
                default:
                    throw new IllegalStateException("Unknown node type found");
            }
        }
    }

    public TokenTree(Node e, List<TokenTreeVertex> children) {
        this.element = e;
        this.children = children;
    }

    private static TokenTreeVertex fromProcessingInstruction(ProcessingInstruction pi) {
        switch (pi.getTarget()) {
            case "linebreak":
            case "breakline":
                return new Newline(pi);
            default:
                throw new InvalidParameterException("Unknown PI found: " + pi.getTarget());
        }
    }

    private static Node[] getChildren(Node root) {
        NodeList children = root.getChildNodes();
        Node[] originalChildren = new Node[children.getLength()];
        for (int i = 0; i < children.getLength(); i++) {
            originalChildren[i] = children.item(i);
        }
        return originalChildren;
    }

    public static List<LabeledToken> labelFromAnnotation(List<TokenTreeLeaf> l) {
        return l.stream()
                .map(TokenTree::getFromAnnotation)
                .collect(Collectors.toList());
    }

    private static LabeledToken getFromAnnotation(TokenTreeLeaf el) {
        // TODO inline label based on whether a text node follows element
        if (el instanceof RechtspraakElement) {
            return new LabeledToken(
                    el,
                    Label.get(((RechtspraakElement) el).getAttribute("manualAnnotation"))
            );
        } else if (el instanceof Newline) {
            return new LabeledToken(el, Label.NEWLINE);
        } else {
            throw new IllegalStateException();
        }
    }


    public static List<LabeledToken> labelFromXmlTags(List<TokenTreeLeaf> tokens) {
        ArrayList<LabeledToken> ll = new ArrayList<>(tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            ll.add(
                    new LabeledToken(tokens.get(i),
                            inferLabelFromXmlStructure(tokens, i))
            );
        }
        return ll;
    }

    private static Label inferLabelFromXmlStructure(
            List<TokenTreeLeaf> tokens, int i) {
        TokenTreeLeaf el = tokens.get(i);
        Label label = Label.TEXT_BLOCK; // Default: out
        if (el instanceof Newline) label = Label.NEWLINE;
        else if (el instanceof Quote) label = Label.QUOTE;
        else if (el instanceof Numbering) {
            label = Label.getNumberingType(tokens, i);
        }//todo

        return label;
    }

    private static boolean isTitleInSection(TokenTreeLeaf el) {
        Element title = Xml.getParentWithTagName(el, TAG_TITLE);
        if (title != null) {
            Element section = Xml.getParentWithTagName(title, TAG_SECTION);
            return section != null;
        } else {
            return false;
        }
    }

    private static TokenTreeVertex fromElement(Element e) {
        if (!KNOWN_ELEMENTS.matcher(e.getNodeName()).matches())
            System.err.println("? " + e.getNodeName() + " ?");


        switch (e.getTagName()) {
            case "para":
            case "text":
            case "emphasis":
                // If it's empty, this counts as a newline
                String textContent1 = e.getTextContent();
                if (textContent1 == null || textContent1.length() == 0
                        || Regex.CONSECUTIVE_WHITESPACE.matcher(textContent1).matches())
                    return new Newline(e);

                // If this is a node with a numbering, create a <potentialnumber/> in front
                Text txt = getFirstTextChildOrBreakOnNr(e);
                if (txt != null) {
                    List<TokenTreeVertex> children = findNumberings(txt);
                    if (children.size() > 0) {
                        return new TokenTree(e, children);
                    }
                }
//                if (hasElementsOrLinebreaksAsChildren(e)) {
//                    return new TokenTree(e);
//                } else {
                return new TextElement(e);
//                }
            case "nr":
            case "potentialnr":
                return new Numbering(e);
            case "quote":
                return new Quote(e);
            default:
                if (IgnoreElement.dontTokenize(e.getTagName())) return new IgnoreElement(e);
                return new TokenTree(e);
        }
    }

    private static List<TokenTreeVertex> findNumberings(Text txt) {
        List<TokenTreeVertex> children = new ArrayList<>();

        int index = Quote.startsWithQuoteAtChar(txt.getTextContent());
        if (index > -1) {
            Element quoteElement = Xml.wrapSubstringInElement(txt, index, 1, TAG_QUOTE);
            txt = (Text) quoteElement.getNextSibling();
            children.add(new Quote(quoteElement));
        }

        int listMarking = ListMarking.startsWithListMarkingAtChar(txt.getTextContent());
        if (listMarking > -1) {
            Element element = Xml.wrapSubstringInElement(txt, listMarking, 1, TAG_POTENTIAL_NR);
            txt = (Text) element.getNextSibling();
            children.add(new Numbering(element));
        }

        CharSequence textContent = txt.getTextContent();
        Matcher numberMatcher = Regex.START_WITH_NUM.matcher(textContent);
        while (numberMatcher.find()
                && !Regex.YYYY_MM_DD.matcher(textContent).find()
                && !Regex.DD_MON_YYYY.matcher(textContent).find()) {
            Element potentialNr = Xml.wrapSubstringInElement(txt, numberMatcher.start(1),
                    numberMatcher.end(1) - numberMatcher.start(1), TAG_POTENTIAL_NR);
            txt = (Text) potentialNr.getNextSibling();
            children.add(new Numbering(potentialNr));
            numberMatcher = Regex.START_WITH_NUM.matcher(txt.getTextContent());
        }
        if (children.size() > 0) {
            if (txt.getTextContent().length() > 0) children.add(new TextElement(Xml.wrapNodeInElement(txt, TAG_TEXT)));
        }
        return children;
    }

    private static Text getFirstTextChildOrBreakOnNr(Element root) {
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node c = children.item(i);
            switch (c.getNodeType()) {
                case Element.ELEMENT_NODE:
                    Element element = (Element) c;
                    if ((element).getTagName().equals(TAG_NR)) {
                        return null;
                    } else {
                        Text result = getFirstTextChildOrBreakOnNr(element);
                        if (result != null) return result;
                    }
                case Element.TEXT_NODE:
                    String text = c.getTextContent();
                    if (text.length() != 0 && !Regex.CONSECUTIVE_WHITESPACE.matcher(text).matches()) {
                        return (Text) c;
                    }
                    break;
                case Element.PROCESSING_INSTRUCTION_NODE:
                    // Ignore
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
        return null;
    }

    private static TokenTreeVertex fromTextNode(Text child) {
        String textContent = child.getTextContent();

        Matcher quoteMatcher = Quote.START_WITH_QUOTE.matcher(textContent);
        if (quoteMatcher.find()) {
            Element wrappedText = Xml.wrapNodeInElement(child, TAG_TEXTGROUP);
            Xml.wrapSubstringInElement(child,
                    quoteMatcher.start(1),
                    quoteMatcher.end(1) - quoteMatcher.start(1),
                    null, TAG_QUOTE, null);
            return new TokenTree(wrappedText);
        }

        List<TokenTreeVertex> nums = findNumberings(child);
        if (nums.size() > 0) {
            return new TokenTree(child, nums);
        } else {
            // Wrap textnode in <text>
            Element wrappedText = Xml.wrapNodeInElement(child, TAG_TEXT);
            return new TextElement(wrappedText);
        }
    }

    /**
     * Return the pre-processed list of elements.
     */
    public List<org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf> leafsInPreOrder() {
        List<TokenTreeLeaf> list = leafsInPreOrderRecursive();

        List<Map.Entry<Integer, Numbering>> allNumberings = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Numbering) allNumberings.add(Maps.immutableEntry(i, (Numbering) list.get(i)));
        }

        // Get all sequences of adjacent alphabetic numberings with the same terminals
        setAdjacentAlphabeticNumbers(list);
        setSameProfileNumberingsPreAndSuccessors(allNumberings);
        setPlausiblePreAndSuccessors(allNumberings);

        // Set whether numbers are plausible
        for (int i = 0; i < list.size(); i++) {
            org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf element = list.get(i);
            if (element instanceof Numbering) {
                ((Numbering) element).isPlausibleNumbering = !DeterministicTagger
                        .looksLikeNumberingButProbablyIsnt(list, i);
            }
        }
        return list;
    }

    private List<org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf> leafsInPreOrderRecursive() {
        List<org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf> list = new ArrayList<>();
        for (TokenTreeVertex child : this.children) {
            if (child instanceof TokenTreeLeaf) {
                list.add((TokenTreeLeaf) child);
            } else if (child instanceof TokenTree) {
                list.addAll(((TokenTree) child).leafsInPreOrderRecursive());
            } else {
                throw new IllegalStateException();
            }
        }
        return list;
    }

    private void setAdjacentAlphabeticNumbers(List<TokenTreeLeaf> list) {
        NumberingProfile profile = null;
        for (TokenTreeLeaf element : list) {
            if (element instanceof Numbering && ((Numbering) element).getNumbering() instanceof AlphabeticNumbering) {
                if (profile == null || !profile.fits(element)) profile = new NumberingProfile(((Numbering) element));
                profile.addInstance((Numbering) element);
                ((Numbering) element).setAlphabeticSequence(profile);
            } else {
                profile = null;
            }
        }
    }

    public void setSameProfileNumberingsPreAndSuccessors(List<Map.Entry<Integer, Numbering>> allNumberings) {
        allNumberings.stream().forEach((numbering1) ->
                allNumberings.stream()
                        .filter(numbering2 -> NumberingProfile.isSameProfileSuccession(numbering1, numbering2))
                        .forEach(numbering2 -> {
                            numbering1.getValue().addSameProfileSuccessor(numbering2);
                            numbering2.getValue().addSameProfilePredecessor(numbering1);
                        }));
    }

    public void setPlausiblePreAndSuccessors(List<Map.Entry<Integer, Numbering>> allNumberings) {
        allNumberings.stream()
                .forEach((numbering1) ->
                        allNumberings.stream()
                                .filter(numbering2 -> numbering2.getKey().compareTo(numbering1.getKey()) > 0
                                        && numbering2.getValue().isSuccedentOf(numbering1.getValue()))
                                .forEach(numbering2 -> {
                                    numbering1.getValue().addPlausibleSuccessor(numbering2);
                                    numbering2.getValue().addPlausiblePredecessor(numbering1);
                                }));
    }
}
