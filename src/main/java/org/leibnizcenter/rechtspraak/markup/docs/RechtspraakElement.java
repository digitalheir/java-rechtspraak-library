package org.leibnizcenter.rechtspraak.markup.docs;

import org.leibnizcenter.rechtspraak.markup.docs.features.IsAllCaps;
import org.leibnizcenter.rechtspraak.util.TextBlockInfo;
import org.leibnizcenter.rechtspraak.util.numbering.NumberingNumber;
import org.leibnizcenter.rechtspraak.util.numbering.SubSectionNumber;
import org.w3c.dom.*;

import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;

/**
 * XML element from Rechtspraak.nl with some pre-processing applied
 * Created by maarten on 29-2-16.
 */
public class RechtspraakElement implements Element {
    public final Element e;
    public final NumberingNumber numbering;
    public final String normalizedText;
    public final int wordCount;
    public final boolean isSpaced;
    public final boolean isAllCaps;
    private final String textContent;
    private boolean highConfidenceNumberedTitleFoundAndIsNumbered;

    public RechtspraakElement(Element e) {
        this.e = e;
        this.textContent = e.getTextContent().trim();

        this.numbering = startsWithNumber(textContent);

        String normalizedWithPotentialLeadingNumber = textContent
                .replaceAll("\\s\\s+", " ") // Replace all whitespace with a single space
                .replaceAll("[^\\p{L} 0-9]", ""); // Remove non-alphanumerics

        String normalizedText = normalizedWithPotentialLeadingNumber
                .replaceAll("^\\s*([0-9]|\\b(i{1,3})\\b|\\b((i?[vx])|([xv]i{0,3}))\\b)+\\s*([;:\\.]*)+\\s*", "") // Remove leading numbering
                .replaceAll("^\\s*(de|het|een)\\b\\s*", "") // Remove leading article (de/het/een)
                .trim();

        this.isAllCaps = (IsAllCaps.ALL_CAPS.matcher(normalizedText).matches());

        this.isSpaced = TextBlockInfo.Space.isSpaced(normalizedText);
        if (isSpaced) {
            normalizedText = TextBlockInfo.Space.unspace(normalizedText);
        }

        this.normalizedText = normalizedText.toLowerCase(Locale.ENGLISH);

        this.wordCount = normalizedText.split("\\s+").length;
    }


    public static NumberingNumber startsWithNumber(String textContent) {
        Matcher numberMatcher = TextBlockInfo.Regex.START_WITH_NUM.matcher(textContent);
        if (numberMatcher.matches()) {
            if (!textContent.matches("^[0-9]{4}-[01]?[0-9]-[0-3]?[0-9]\b.*")
                    && !textContent.matches(
                    "^[0-3]?[0-9]\\s+(jan|feb|maart|apr|mei|jun|jul|aug|sep|okt|nov|dec).*[0-9]{4}\\b.*")
                    ) {
                // If it's not a date...
                String num = numberMatcher.group(1);
                String terminal = numberMatcher.group(3);
//                System.out.println("Num: "+num);
//                System.out.println("Terminal: "+terminal);
                try {
                    return NumberingNumber.parse(num, terminal);
                } catch (NumberingNumber.TooBigForFormattingException e) {
                    return null;
                }
            }
        }
        return null;
    }


//    public static void setNumberFeature(List<NumberingNumber> precedingNumbers, String textContent, Token token,
//                                        Token prevToken, String ecli) {
//        NumberingNumber startsWithNumbering = startsWithNumber(textContent);
//
//        if (startsWithNumbering != null) {
//            token.setProperty(INT_NUM, startsWithNumbering.mainNum());
//            if (prevToken != null) {
//                if (new Integer(startsWithNumbering.mainNum() - 1).equals(prevToken.getProperty(INT_NUM))) {
//                    prevToken.setFeatureValue(PART_OF_LIST, 1.0);
//                    token.setFeatureValue(PART_OF_LIST, 1.0);
//                }
//            }
//
//            boolean incrementingNumber;
//            token.setFeatureValue("START_WITH_NUM_WITH_TERMINAL", dblMatches(textContent, Regex.START_WITH_NUM_WITH_TERMINAL));
//
//            token.setFeatureValue(SUBSECTION_NUMBER, startsWithNumbering instanceof SubSectionNumber ? 1.0 : 0.0);
//            token.setFeatureValue(SECTION_NUMBER, startsWithNumbering instanceof FullSectionNumber ? 1.0 : 0.0);
//
//            Collection<NumberingNumber> predecessors = Collections2.filter(precedingNumbers,
//                    startsWithNumbering::isSuccedentOf);
//
//            if (predecessors.size() > 0) {
////                if (predecessors.size() > 1) {
////                    System.err.println(ecli
////                            + ": Warning: Found "
////                            + predecessors.size() + " predecessors for "
////                            + startsWithNumbering + ": " + predecessors
////                    );
////                }
//                int ix = Collections3.lastIndexWhere(precedingNumbers, startsWithNumbering::isSuccedentOf);
//                //System.out.println(ecli + ": " + precedingNumbers.get(ix) + "+1 = " + textContent);
//
//                // Make sure that this numbering is at the tail of the list, because it's touched last
//                precedingNumbers.add(startsWithNumbering);
//                precedingNumbers.remove(ix);
//
//                incrementingNumber = true;
//            } else if (Collections2.filter(precedingNumbers, isPlausiblySuccedent(startsWithNumbering)::apply).size() > 0) {
//                int ix = Collections3.lastIndexWhere(precedingNumbers, isPlausiblySuccedent(startsWithNumbering));
//                System.out.println(ecli + ": " + precedingNumbers.get(ix) + "+1 = " + textContent);
//
//                // Make sure that this numbering is at the tail of the list, because it's touched last
//                precedingNumbers.add(startsWithNumbering);
//                precedingNumbers.remove(ix);
//
//                incrementingNumber = true;
//            } else {
//                //System.out.println(ecli + ": Found numbering " + textContent);
//                precedingNumbers.add(startsWithNumbering);
//                incrementingNumber = startsWithNumbering.isFirstNumbering();
//            }
//            token.setFeatureValue(INCREMENTING_NUMBER, (incrementingNumber ? 1.0 : 0.0));
//        }
//    }

    public static Function<NumberingNumber, Boolean> isPlausiblySuccedent(NumberingNumber startsWithNumbering) {
        return (precedingNumber) -> startsWithNumbering.isSuccedentOf(precedingNumber) || (
                startsWithNumbering instanceof SubSectionNumber
                        && startsWithNumbering.isFirstNumbering()
                        && ((SubSectionNumber) startsWithNumbering).firstSubsectionOf().isSuccedentOf(precedingNumber));
    }


    @Override
    public String getTagName() {
        return e.getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return e.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, String value) throws DOMException {
        e.setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) throws DOMException {
        e.removeAttribute(name);

    }

    @Override
    public Attr getAttributeNode(String name) {
        return e.getAttributeNode(name);
    }

    @Override
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        return null;
    }

    @Override
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        return null;
    }

    @Override
    public NodeList getElementsByTagName(String name) {
        return e.getElementsByTagName(name);
    }

    @Override
    public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
        return null;
    }

    @Override
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        e.setAttributeNS(namespaceURI, qualifiedName, value);

    }

    @Override
    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        e.removeAttributeNS(namespaceURI, localName);

    }

    @Override
    public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        return e.getAttributeNodeNS(namespaceURI, localName);
    }

    @Override
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        return e.setAttributeNodeNS(newAttr);
    }

    @Override
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
        return e.getElementsByTagNameNS(namespaceURI, localName);
    }

    @Override
    public boolean hasAttribute(String name) {
        return e.hasAttribute(name);
    }

    @Override
    public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        return false;
    }

    @Override
    public TypeInfo getSchemaTypeInfo() {
        return e.getSchemaTypeInfo();
    }

    @Override
    public void setIdAttribute(String name, boolean isId) throws DOMException {
        e.setIdAttribute(name, isId);

    }

    @Override
    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        e.setIdAttributeNS(namespaceURI, localName, isId);

    }

    @Override
    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        e.setIdAttributeNode(idAttr, isId);

    }

    @Override
    public String getNodeName() {
        return e.getNodeName();
    }

    @Override
    public String getNodeValue() throws DOMException {
        return e.getNodeValue();
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {
        e.setNodeValue(nodeValue);

    }

    @Override
    public short getNodeType() {
        return e.getNodeType();
    }

    @Override
    public Node getParentNode() {
        return e.getParentNode();
    }

    @Override
    public NodeList getChildNodes() {
        return e.getChildNodes();
    }

    @Override
    public Node getFirstChild() {
        return e.getFirstChild();
    }

    @Override
    public Node getLastChild() {
        return e.getLastChild();
    }

    @Override
    public Node getPreviousSibling() {
        return e.getPreviousSibling();
    }

    @Override
    public Node getNextSibling() {
        return e.getNextSibling();
    }

    @Override
    public NamedNodeMap getAttributes() {
        return e.getAttributes();
    }

    @Override
    public Document getOwnerDocument() {
        return e.getOwnerDocument();
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return e.insertBefore(newChild, refChild);
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return e.replaceChild(newChild, oldChild);
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        return e.removeChild(oldChild);
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        return e.appendChild(newChild);
    }

    @Override
    public boolean hasChildNodes() {
        return e.hasChildNodes();
    }

    @Override
    public Node cloneNode(boolean deep) {
        return e.cloneNode(deep);
    }

    @Override
    public void normalize() {
        e.normalize();

    }

    @Override
    public boolean isSupported(String feature, String version) {
        return e.isSupported(feature, version);
    }

    @Override
    public String getNamespaceURI() {
        return e.getNamespaceURI();
    }

    @Override
    public String getPrefix() {
        return e.getPrefix();
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {
        e.setPrefix(prefix);

    }

    @Override
    public String getLocalName() {
        return e.getLocalName();
    }

    @Override
    public boolean hasAttributes() {
        return e.hasAttributes();
    }

    @Override
    public String getBaseURI() {
        return e.getBaseURI();
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return e.compareDocumentPosition(other);
    }

    @Override
    public String getTextContent() throws DOMException {
        return textContent;
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {
        e.setTextContent(textContent);

    }

    @Override
    public boolean isSameNode(Node other) {
        return e.isSameNode(other);
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return e.lookupPrefix(namespaceURI);
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return e.isDefaultNamespace(namespaceURI);
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return e.lookupNamespaceURI(prefix);
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return e.isEqualNode(arg);
    }

    @Override
    public Object getFeature(String feature, String version) {
        return e.getFeature(feature, version);
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return e.setUserData(key, data, handler);
    }

    @Override
    public Object getUserData(String key) {
        return e.getUserData(key);
    }

    public String getNormalizedText() {
        return normalizedText;
    }

    @Override
    public String toString() {
        return e.getTagName() + ": " + textContent;
    }

    public void setHighConfidenceNumberedTitleFoundAndIsNumbered(boolean highConfidenceNumberedTitleFoundAndIsNumbered) {
        if (this.numbering == null) throw new IllegalStateException();
        this.highConfidenceNumberedTitleFoundAndIsNumbered = highConfidenceNumberedTitleFoundAndIsNumbered;
    }

    public boolean highConfidenceNumberedTitleFoundAndIsNumbered() {
        return highConfidenceNumberedTitleFoundAndIsNumbered;
    }
}
