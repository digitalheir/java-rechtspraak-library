package org.leibnizcenter.rechtspraak.markup.docs.tokentree;

import org.leibnizcenter.rechtspraak.tokens.features.quote.BlockQuotePatterns;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledToken;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.util.TextBlockInfo;
import org.leibnizcenter.util.Xml;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList.INFO_SUFFIX;

/**
 * Created by maarten on 27-3-16.
 */
public class TokenTree implements TokenTreeVertex {
    private static final String TAG_SECTION = "section";
    private static final String TAG_TITLE = "title";
    private static final String TAG_NR = "nr";
    private static final String TAG_TEXT = "text";
    private static final String TAG_POTENTIAL_NR = "potentialnr";
    private static final String TAG_TEXTGROUP = "textgroup";
    private static final String TAG_PARA = "para";
    private static final String TAG_QUOTE = "quote";
    private static final Pattern KNOWN_ELEMENTS = Pattern.compile("(itemized|ordered)list" +
            "|footnote" +
            "|listitem" +
            "|link" +
            "|potentialnr" +
            "|" + TAG_NR +
            "|" + TAG_TEXT +
            "|" + TAG_TEXTGROUP +
            "|" + TAG_QUOTE +
            "|emphasis" +
            "|section" +
            "|bridgehead" +
            "|(uitspraak|conclusie)\\.info" +
            "|(rs:)?para(block|group)?" +
            "|title" +
            "|(informal)?table" +
            "|mediaobject");
    private final List<TokenTreeVertex> children;
    Pattern START_W_QUOTE = Pattern.compile("^\\s*(" + BlockQuotePatterns.Constants.CHAR_ANY_QUOTE + ")");
    private Element element;

    public TokenTree(Element root, boolean includeWhiteSpace) {
        element = root;
        children = new ArrayList<>();
        //textElementsAsLinearList(root, null, null, false);
        //List<LabeledToken> tokens = new ArrayList<>(100);

        // Make sure we have a reference to all children *as they are now*; the XML tree might change.
        Node[] originalChildren = getChildren(root);

        for (Node child : originalChildren) {
            String parentName = root.getNodeName();

            if (includeWhiteSpace || !TextBlockInfo.isAllWhitespace(child.getTextContent())) {
                switch (child.getNodeType()) {
                    case Node.TEXT_NODE:
                        children.add(fromTextNode((Text) child, includeWhiteSpace));
                        break;
                    case Node.ELEMENT_NODE:
                        children.add(fromElement((Element) child, includeWhiteSpace));
                        break;
                    default:
                        throw new IllegalStateException("Unknown node type found");
                }
            }
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

    public static List<LabeledToken> labelFromAnnotation(List<RechtspraakElement> l) {
        return l.stream()
                .map((el) -> new LabeledToken(
                        el,
                        Label.get(el.getAttribute("manualAnnotation"))
                ))
                .collect(Collectors.toList());
    }

    public static List<LabeledToken> labelFromXmlTags(List<RechtspraakElement> l) {
        return l.stream()
                .map((el) -> new LabeledToken(
                        el,
                        inferLabelFromXmlStructure(el)//todo use map
                ))
                .collect(Collectors.toList());
    }

    private static Label inferLabelFromXmlStructure(RechtspraakElement el) {
        Label label = Label.TEXT_BLOCK; // Default: out
        String tagName = el.getTagName();
        if (tagName.endsWith(INFO_SUFFIX) || Xml.hasParentWithTagSuffix(el, INFO_SUFFIX)) {
            // Within info tag
            label = Label.TEXT_BLOCK;//INFO;
        } else if (isTitleInSection(el)) {
            // i.e. <section> <title /> </section>
            if (TAG_NR.equals(el.getNodeName())) {
                label = Label.NR_FULL_INLINE;//todo
            } else {
                label = Label.SECTION_TITLE;
            }
        }
        return label;
    }

    private static boolean isTitleInSection(RechtspraakElement el) {
        Element title = Xml.getParentWithTagName(el, TAG_TITLE);
        if (title != null) {
            Element section = Xml.getParentWithTagName(title, TAG_SECTION);
            return section != null;
        } else {
            return false;
        }
    }

    private TokenTreeVertex fromElement(Element child, boolean includeWhiteSpace) {
        if (!KNOWN_ELEMENTS.matcher(child.getNodeName()).matches())
            System.err.println("? " + child.getNodeName() + " ?");

        //child = textBlockWithLabel.getToken(); // XML tree may have changed


        switch (child.getTagName()) {
            case "para":
            case "text":
            case "emphasis":
                // If this is para node with a numbering, create a <potentialnumber/> in front
                Text txt = getFirstTextChildOrBreakOnNr(child);
                if (txt != null) {
                    String textContent = txt.getTextContent();

                    Matcher quoteMatcher = START_W_QUOTE.matcher(textContent);
                    if (quoteMatcher.find()) {
                        Xml.wrapSubstringInElement(txt,
                                quoteMatcher.start(1),
                                quoteMatcher.end(1) - quoteMatcher.start(1),
                                null, TAG_QUOTE, null);
                        return new TokenTree(child, includeWhiteSpace);
                    }

                    Matcher numberMatcher = TextBlockInfo.Regex.START_WITH_NUM.matcher(textContent);
                    if (numberMatcher.find() && !TextBlockInfo.Regex.YYYY_MM_DD.matcher(textContent).find()
                            && !TextBlockInfo.Regex.DD_MON_YYYY.matcher(textContent).find()) {
                        Xml.wrapSubstringInElement(txt, numberMatcher.start(1),
                                numberMatcher.end(1) - numberMatcher.start(1), TAG_POTENTIAL_NR);
                        return new TokenTree(child, includeWhiteSpace);
                    }
                }
                if (TextBlockInfo.hasElementsOrLinebreaksAsChildren(child)) {
                    return new TokenTree(child, includeWhiteSpace);
                } else {
                    return new RechtspraakElement(child);
                }
            case "nr":
            case "potentialnr":
            case "quote":
            case "informaltable":
            case "table":
            case "bridgehead":
            case "mediaobject":
            case "itemizedlist":
            case "listitem":
            case "footnote":
                return new RechtspraakElement(child);
            default:
                return new TokenTree(child, includeWhiteSpace);
        }
    }

    private Text getFirstTextChildOrBreakOnNr(Element root) {
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
                    if (!TextBlockInfo.isAllWhitespace(c.getTextContent())) {
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

    private TokenTreeVertex fromTextNode(Text child, boolean includeWhiteSpace) {
        String textContent = child.getTextContent();

        Matcher quoteMatcher = START_W_QUOTE.matcher(textContent);
        if (quoteMatcher.find()) {
            Element wrappedText = Xml.wrapNodeInElement(child, TAG_TEXTGROUP);
            Xml.wrapSubstringInElement(child,
                    quoteMatcher.start(1),
                    quoteMatcher.end(1) - quoteMatcher.start(1),
                    null, TAG_QUOTE, null);
            return new TokenTree(wrappedText, includeWhiteSpace);
        }

        Matcher numberMatcher = TextBlockInfo.Regex.START_WITH_NUM.matcher(textContent);
        if (numberMatcher.find() && !TextBlockInfo.Regex.YYYY_MM_DD.matcher(textContent).find()
                && !TextBlockInfo.Regex.DD_MON_YYYY.matcher(textContent).find()) {
            // Wrap entire textnode in text group
            Element wrappedText = Xml.wrapNodeInElement(child, TAG_TEXTGROUP);

            child = (Text) wrappedText.getChildNodes().item(0); // Must be a text node
            // Wrap number in <nr>, remaining text in <para>
            Xml.wrapSubstringInElement(child,
                    numberMatcher.start(1),
                    numberMatcher.start(2) - numberMatcher.start(1),
                    null, TAG_POTENTIAL_NR, TAG_TEXT);
            //Element nr = wrappeds[1];
            //Element para = wrappeds[2];
            return new TokenTree(wrappedText, includeWhiteSpace);
        } else {
            // Wrap textnode in <text>
            Element wrappedText = Xml.wrapNodeInElement(child, TAG_TEXT);
            return new RechtspraakElement(wrappedText);
        }
    }

    /**
     * Return the pre-processed list of elements.
     */
    public List<RechtspraakElement> terminalsInPreOrder() {
        List<RechtspraakElement> list = new ArrayList<>();
        for (TokenTreeVertex child : this.children) {
            if (child instanceof RechtspraakElement) {
                list.add((RechtspraakElement) child);
            } else if (child instanceof TokenTree) {
                list.addAll(((TokenTree) child).terminalsInPreOrder());
            } else {
                throw new IllegalStateException();
            }
        }
        return list;
    }
}
