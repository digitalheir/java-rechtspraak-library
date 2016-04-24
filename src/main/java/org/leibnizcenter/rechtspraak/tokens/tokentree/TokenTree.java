package org.leibnizcenter.rechtspraak.tokens.tokentree;

import com.google.common.collect.Maps;
import org.leibnizcenter.rechtspraak.leibnizannotations.DeterministicTagger;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.LabeledToken;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.numbering.ListMarking;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.quote.Quote;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TextElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.util.Collections3;
import org.leibnizcenter.util.Regex;
import org.leibnizcenter.util.Xml;
import org.leibnizcenter.rechtspraak.tokens.text.IgnoreElement;
import org.w3c.dom.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags.LeibnizTags.*;

/**
 * Created by maarten on 27-3-16.
 */
public class TokenTree implements TokenTreeVertex {
    private final List<TokenTreeVertex> children;
    private final Node element;

    public static final String TAG_SECTION = "section";
    public static final String TAG_TITLE = "title";
    public static final String TAG_NR = "nr";
    public static final String TAG_PARA = "para";
    

    private static final Pattern KNOWN_ELEMENTS = Pattern.compile("(itemized|ordered)list" +
            "|footnote" +
            "|footnote-ref" +
            "|listmarking" +
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
        Node[] originalChildren = Xml.getChildren(root);

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

    public static List<Label> labelFromAnnotation(List<TokenTreeLeaf> l) {
        return l.stream()
                .map(TokenTree::getFromAnnotation)
                .collect(Collectors.toList());
    }

    private static Label getFromAnnotation(TokenTreeLeaf el) {
        // TODO inline label based on whether a text node follows element
        if (el instanceof RechtspraakElement) {
            Label l = Label.fromString.get(((RechtspraakElement) el).getAttribute("manualAnnotation"));
            if (l == null)
                if (el instanceof ListMarking ||
                        ((RechtspraakElement) el).getTagName().matches("quote|footnote\\-ref")) return Label.TEXT_BLOCK;
                else
            throw new NullPointerException();
            return l;
        } else if (el instanceof Newline) {
            return Label.NEWLINE;
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
//        else if (el instanceof Quote) label = Label.QUOTE;
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
                return fromPotentiallyMixed(e);
            case "nr":
            case "potentialnr":
                return new Numbering(e);
            case "listmarking":
                return new TextElement(e);
            case "quote":
                return new Quote(e);
            default:
                if (IgnoreElement.dontTokenize(e.getTagName())) return new IgnoreElement(e);
                return new TokenTree(e);
        }
    }

    private static TokenTreeVertex fromPotentiallyMixed(Element e) {
        Node[] children = Xml.getChildren(e);

        ArrayList<TokenTreeVertex> tokenSiblings = new ArrayList<>(children.length);
        for (int i = 0; i < children.length; i++) {
            Node c = children[i];

            ///////////////////////////
            switch (c.getNodeType()) {
                case Element.ELEMENT_NODE:
                    Element childE = (Element) c;
                    tokenSiblings.add(fromElement(childE));
                    break;
                case Element.TEXT_NODE:
                    String text = c.getTextContent();

                    // If we're not trailing or leading whitespace
                    if (!((i == 0 || i == children.length - 1) && Regex.CONSECUTIVE_WHITESPACE.matcher(text).matches())) {
                        List<TokenTreeVertex> numberings = findNumberings((Text) c);
                        if (numberings.size() > 0) {
                            tokenSiblings.addAll(numberings);
                        } else {
                            if (children.length == 1) return new TextElement(e);
                            else {
                                Element wrapped = Xml.wrapNodeInElement(c, TAG_TEXT);
                                tokenSiblings.add(new TextElement(wrapped));
                            }
                        }
                    }
                    break;
                case Element.PROCESSING_INSTRUCTION_NODE:
                    tokenSiblings.add(fromProcessingInstruction((ProcessingInstruction) c));
                    break;
                default:
                    throw new IllegalStateException();
            }
            //////////////////////////////////////////////////////
        }

        if (tokenSiblings.size() > 0) {
            return new TokenTree(e, tokenSiblings);
        } else {
            return new Newline(e);
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
            Element element = Xml.wrapSubstringInElement(txt, 0, listMarking+1, TAG_LIST_MARKING);
            txt = (Text) element.getNextSibling();
            children.add(new ListMarking(element));
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

        // Get all sequences of adjacent numberings
        setAdjacentNumbers(list);

        setSameProfileNumberingsPreAndSuccessors(allNumberings);
        setPlausiblePreAndSuccessors(allNumberings);

        // Set whether numbers are plausible
        for (int i = 0; i < list.size(); i++) {
            org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf element = list.get(i);
            if (element instanceof Numbering) {
                boolean isImplausible = DeterministicTagger
                        .looksLikeNumberingButProbablyIsnt(list, i);
                if (isImplausible) {
                    for (SameKindOfNumbering p : SameKindOfNumbering.values()) {
                        SameKindOfNumbering.List sequence = ((Numbering) element).getSequence(p);
                        if (!Collections3.isNullOrEmpty(sequence)) sequence.taintByImplausibleNumbering();
                    }
                }
                ((Numbering) element).isPlausibleNumbering = !isImplausible;
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

    private void setAdjacentNumbers(List<TokenTreeLeaf> list) {
        for (SameKindOfNumbering p : SameKindOfNumbering.values()) {
            SameKindOfNumbering.List l = new SameKindOfNumbering.List(p);

            for (int i = 1; i <= list.size(); i++) {
                TokenTreeLeaf prev = list.get(i - 1);
                if (i == list.size()) {
                    if (l.size() > 0) {
                        // Add last match
                        l.add((Numbering) prev);
                        ((Numbering) prev).setSequence(p, l);
                    }
                } else {
                    TokenTreeLeaf token = list.get(i);

                    if (p.test(prev, token)) {
                        l.add((Numbering) prev);// We add 'token' in the next iteration
                        ((Numbering) prev).setSequence(p, l);
                    } else if (l.size() > 0) {
                        // Add last match
                        l.add((Numbering) prev);
                        ((Numbering) prev).setSequence(p, l);
                        l = new SameKindOfNumbering.List(p); // Start with fresh list
                    }
                }
            }
        }
    }

    public void setSameProfileNumberingsPreAndSuccessors(List<Map.Entry<Integer, Numbering>> allNumberings) {
        allNumberings.stream().forEach((numbering1) ->
                allNumberings.stream()
                        .filter(numbering2 -> SameKindOfNumbering.isSameProfileSuccession(numbering1, numbering2))
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
