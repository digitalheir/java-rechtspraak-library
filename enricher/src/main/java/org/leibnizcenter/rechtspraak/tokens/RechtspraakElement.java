package org.leibnizcenter.rechtspraak.tokens;

import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTreeVertex;
import org.leibnizcenter.rechtspraak.util.Regex;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.w3c.dom.*;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * XML element from Rechtspraak.nl with some pre-processing applied
 * Created by maarten on 29-2-16.
 */
public abstract class RechtspraakElement extends org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf implements Element {
    private final Element e;


    public RechtspraakElement(Element e) {
        super(e);
        this.e = e;
        if (e == null) throw new NullPointerException();
    }

    public boolean precedesNonEmptyText() {
        return Xml.isNonEmptyText(e.getNextSibling());
    }

    public boolean followsNonEmptyText() {
        return Xml.isNonEmptyText(e.getPreviousSibling());
    }

    @Override
    public String getTagName() {
        return e.getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return ((Element)getNode()).getAttribute(name);
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
        return e.setAttributeNode(newAttr);
    }

    @Override
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        return e.removeAttributeNode(oldAttr);
    }

    @Override
    public NodeList getElementsByTagName(String name) {
        return e.getElementsByTagName(name);
    }

    @Override
    public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
        return e.getAttributeNS(namespaceURI, localName);
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
        return e.hasAttributeNS(namespaceURI, localName);
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

    public Element getElement() {
        return e;
    }
}
