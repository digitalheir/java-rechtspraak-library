package org.leibnizcenter.rechtspraak.tokens.text;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTreeVertex;
import org.leibnizcenter.rechtspraak.util.Regex;
import org.leibnizcenter.rechtspraak.util.Strings2;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.w3c.dom.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by maarten on 3-4-16.
 */
public abstract class TokenTreeLeaf implements TokenTreeVertex, Node {
    private static final String[] ZERO_STRINGS = new String[]{};
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";

    private final Node node;
    private final String textContent;
    private final static Pattern LEADING_ARTICLE = Pattern.compile("^\\s*(de|het|een)\\b\\s*");
    private final String normalizedText;
    public final String[] words;

    public String[] wordsBeforeOpeningBracket;
    public final Element emphasis;

    public TokenTreeLeaf(Node n) {
        this.node = n;
        String textContent = n.getTextContent();
        this.textContent = textContent == null ? null : textContent.trim();

        words = this.textContent == null ? ZERO_STRINGS : getWords(textContent);
        wordsBeforeOpeningBracket = getWordsBeforeOpeningBracket(words);

        emphasis = findEmphasis();

        if (Strings.isNullOrEmpty(textContent)) {
            this.normalizedText = EMPTY_STRING;
        } else {
            String normalizedText = Regex.CONSECUTIVE_WHITESPACE.matcher(textContent).replaceAll(SPACE);// Replace all whitespace with a single space;
            // Remove non-alphanumerics
            normalizedText = Regex.NON_ALPHANUMERIC.matcher(normalizedText).replaceAll(EMPTY_STRING);
            normalizedText = LEADING_ARTICLE.matcher(normalizedText) // Remove leading article (de/het/een)
                    .replaceAll(EMPTY_STRING)
                    .trim().toLowerCase(Locale.ENGLISH);
            this.normalizedText = normalizedText;
        }
    }

    private static String[] getWords(String textContent) {
        List<String> words = Arrays.stream(Regex.CONSECUTIVE_WHITESPACE.split(textContent))
                .filter(s -> !(Strings.isNullOrEmpty(s)) && Strings2.hasAtLeastOneLetter(s))
                .collect(Collectors.toList());
        if (words.size() == 0) return ZERO_STRINGS;
        return words.toArray(new String[words.size()]);
    }

    private static String[] getWordsBeforeOpeningBracket(String[] words) {
        int bracketOpen = -1;
        for (int i = 0; i < words.length; i++) {
            if (Regex.BRACKET_OPEN.matcher(words[i]).find()) {
                bracketOpen = i;
            }
        }
        if (bracketOpen < 0) return words;
        else {
            String[] wordsBeforeOpeningBracket = new String[bracketOpen];
            System.arraycopy(words, 0, wordsBeforeOpeningBracket, 0, wordsBeforeOpeningBracket.length);
            return wordsBeforeOpeningBracket;
        }
    }

    public String getNormalizedText() {
        return normalizedText;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String getNodeName() {
        return node.getNodeName();
    }


    @Override
    public String getNodeValue() throws DOMException {
        return node.getNodeValue();
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        node.setNodeValue(nodeValue);

    }

    @Override
    public short getNodeType() {
        return node.getNodeType();
    }

    @Override
    public Node getParentNode() {
        return node.getParentNode();
    }

    @Override
    public NodeList getChildNodes() {
        return node.getChildNodes();
    }

    @Override
    public Node getFirstChild() {
        return node.getFirstChild();
    }

    @Override
    public Node getLastChild() {
        return node.getLastChild();
    }

    @Override
    public Node getPreviousSibling() {
        return node.getPreviousSibling();
    }

    @Override
    public Node getNextSibling() {
        return node.getNextSibling();
    }

    @Override
    public NamedNodeMap getAttributes() {
        return node.getAttributes();
    }

    @Override
    public Document getOwnerDocument() {
        return node.getOwnerDocument();
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return node.insertBefore(newChild, refChild);
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return node.replaceChild(newChild, oldChild);
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        return node.removeChild(oldChild);
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        return node.appendChild(newChild);
    }

    @Override
    public boolean hasChildNodes() {
        return node.hasChildNodes();
    }

    @Override
    public Node cloneNode(boolean deep) {
        return node.cloneNode(deep);
    }

    @Override
    public void normalize() {
        node.normalize();

    }

    @Override
    public boolean isSupported(String feature, String version) {
        return node.isSupported(feature, version);
    }

    @Override
    public String getNamespaceURI() {
        return node.getNamespaceURI();
    }

    @Override
    public String getPrefix() {
        return node.getPrefix();
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        node.setPrefix(prefix);

    }

    @Override
    public String getLocalName() {
        return node.getLocalName();
    }

    @Override
    public boolean hasAttributes() {
        return node.hasAttributes();
    }

    @Override
    public String getBaseURI() {
        return node.getBaseURI();
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return node.compareDocumentPosition(other);
    }

    @Override
    public String getTextContent() throws DOMException {
        return textContent;
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        node.setTextContent(textContent);

    }

    @Override
    public boolean isSameNode(Node other) {
        return node.isSameNode(other);
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return node.lookupPrefix(namespaceURI);
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return node.isDefaultNamespace(namespaceURI);
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return node.lookupNamespaceURI(prefix);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return node.isEqualNode(arg);
    }

    @Override
    public Object getFeature(String feature, String version) {
        return node.getFeature(feature, version);
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return node.setUserData(key, data, handler);
    }

    private Element findEmphasis() {
        TokenTreeLeaf e = this;
        if (e.getNodeType() == Element.ELEMENT_NODE
                && e.getNodeName().equals("emphasis")) return (Element) e;
        NodeList children = e.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            Node child = children.item(j);
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    if (!"emphasis".equals(((Element) child).getTagName())) return (Element) child;
                    break;
                case Node.TEXT_NODE:
                    if (Xml.hasSubstantialText((Text) child)) return null;
            }
        }
        return null;
    }

    @Override
    public Object getUserData(String key) {
        return node.getUserData(key);
    }


    @Override
    public String toString() {
        return node.getNodeName() + ": " + textContent;
    }

    public Set<String> getEmphasis() {
        // todo split at initialization
        if (emphasis == null) return null;
        else return Sets.newHashSet(Regex.CONSECUTIVE_WHITESPACE.split(emphasis.getAttribute("role")));
    }
}
