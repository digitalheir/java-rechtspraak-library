package org.leibnizcenter.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.org.apache.xerces.internal.dom.DOMOutputImpl;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Pair;
import org.leibnizcenter.util.Regex;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by maarten on 22-12-15.
 */
public class Xml {
    public static String OUT_FOLDER = "/media/maarten/Media/rechtspraak-rich-docs-20160221-annotated/";
    public static String IN_FOLDER = "/media/maarten/Media/rechtspraak-rich-docs-20160221/";

    public static Element wrapRemainderInElement(Element e, int startFromChildIx, String tagName) {
        Element newElement = e.getOwnerDocument().createElement(tagName);

        Node[] children = getChildren(e); // Tree will change, so get a ref to children as they are now
        for (int i = startFromChildIx; i < children.length; i++) {
            Node child = children[i];
            e.removeChild(child);
            newElement.appendChild(child);
        }
        if (newElement.getChildNodes().getLength() > 0) e.appendChild(newElement);

        return newElement;
    }

    public static Element wrapSubstringInElement(
            Text node,
            int startIndex,
            int length,
            String tagName) {
        return wrapSubstringInElement(node, startIndex, length, null, tagName, null)[1];
    }

    public static Element wrapSubstringInElement(
            Text node,
            int length,
            String tagName) {
        node.splitText(length);

        Node parent = node.getParentNode();

        Element newElement = node.getOwnerDocument().createElement(tagName);
        parent.replaceChild(newElement, node);
        newElement.appendChild(node);

        return newElement;
    }

    public static Element[] wrapSubstringInElement(
            Text startNode,
            int startIndex,
            int length,
            String tagNameStart, String tagNameMiddle, String tagNameLast) {
        Element[] wrappedesies = new Element[3];

        Text middle = startNode.splitText(startIndex);
        Text last = middle.splitText(length);

        Node parent = startNode.getParentNode();
        if (tagNameStart != null) {
            Element firstTextWrapped = startNode.getOwnerDocument().createElement(tagNameStart);
            parent.replaceChild(firstTextWrapped, startNode);
            firstTextWrapped.appendChild(startNode);
            wrappedesies[0] = firstTextWrapped;
        }

        Element newElement = startNode.getOwnerDocument().createElement(tagNameMiddle);
        parent.replaceChild(newElement, middle);
        newElement.appendChild(middle);
        wrappedesies[1] = newElement;

        if (tagNameLast != null) {
            Element lastTextWrapped = startNode.getOwnerDocument().createElement(tagNameLast);
            parent.replaceChild(lastTextWrapped, last);
            lastTextWrapped.appendChild(last);
            wrappedesies[2] = lastTextWrapped;
        }

        return wrappedesies;
    }


    public static String toString(Document document) throws TransformerException,
            InstantiationException, IllegalAccessException, ClassNotFoundException {
//        DOMImplementationLS domImplLS = (DOMImplementationLS) document
//                .getImplementation();
        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("XML 3.0 LS 3.0");
        LSSerializer serializer = impl.createLSSerializer();
        LSOutput out = new DOMOutputImpl();
        out.setEncoding("UTF-8");
        Writer sw = new StringWriter();
        out.setCharacterStream(sw);
        serializer.write(document, out);

        return sw.toString();
    }

    public static void writeToStream(Document document, Writer w) throws TransformerException,
            InstantiationException, IllegalAccessException, ClassNotFoundException {
//        DOMImplementationLS domImplLS = (DOMImplementationLS) document
//                .getImplementation();
        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("XML 3.0 LS 3.0");
        LSSerializer serializer = impl.createLSSerializer();
        LSOutput out = new DOMOutputImpl();
        out.setEncoding("UTF-8");
        out.setCharacterStream(w);
        serializer.write(document, out);
    }

    public static Element getContentRoot(Document doc) {
        NodeList uitspraken = doc.getElementsByTagName("uitspraak");
        NodeList conclusies = doc.getElementsByTagName("conclusie");

        assert uitspraken.getLength() + conclusies.getLength() == 1;

        Element e = null;
        if (uitspraken.getLength() == 1) {
            e = (Element) uitspraken.item(0);
        } else if (conclusies.getLength() == 1) {
            e = (Element) conclusies.item(0);
        }
        if (e == null) {
            throw new NullPointerException("Content node could not be found");
        }
        return e;
    }

    public static List<Text> textInPreorder(Node root) {
        if (root.getNodeType() == Node.TEXT_NODE) {
            return Lists.newArrayList((Text) root);
        } else {
            NodeList children = root.getChildNodes();
            List<Text> texts = new ArrayList<>(children.getLength());
            for (int i = 0; i < children.getLength(); i++) {
                texts.addAll(textInPreorder(children.item(i)));
            }
            return texts;
        }
    }


    //    private final static Pattern relevantParents = Pattern.compile("(^title$)|(^\\p{L}*\\.info$)");


    public static boolean isElement(Node e) {
        return e != null && e.getNodeType() == Node.ELEMENT_NODE;
    }

    public static boolean whitespaceText(Node node) {
        switch (node.getNodeType()) {
            case Element.TEXT_NODE:
                return node.getTextContent().trim().length() == 0;
            case Element.ELEMENT_NODE:
            case Element.PROCESSING_INSTRUCTION_NODE:
                return false;
            default:
                throw new InvalidParameterException();
        }
    }

    public static boolean hasParentWithTagSuffix(Node node, String suffix) {
        Node parentNode = node.getParentNode();
        return parentNode != null
                && (parentNode.getNodeType() == Node.ELEMENT_NODE
                && ((Element) parentNode).getTagName().endsWith(suffix)
                || hasParentWithTagSuffix(parentNode, suffix));

    }

    public static Element getParentWithTagName(Node el, String tag) {
        Node parentNode = el.getParentNode();
        if (parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
            if (((Element) parentNode).getTagName().equals(tag)) {
                return (Element) parentNode;
            } else {
                return getParentWithTagName(parentNode, tag);
            }
        } else {
            return null;
        }
    }

    public static Element wrapNodeInElement(Node node, String tagText) {
        Node parent = node.getParentNode();
        Element newElement = node.getOwnerDocument().createElement(tagText);
        parent.replaceChild(newElement, node);
        newElement.appendChild(node);
        return newElement;
    }

    /**
     * Has more than one non-whitespace char
     *
     * @param text
     * @return
     */
    public static boolean hasSubstantialText(Text text) {
        String textContent = text.getTextContent();
        if (textContent.length() > 0) {
            Matcher matcher = Regex.NON_WHITESPACE.matcher(textContent);
            if (matcher.find() &&
                    matcher.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNonEmptyText(Node sibling) {
        return sibling != null
                && (sibling.getNodeType() == Element.TEXT_NODE
                || "text".equals(sibling.getNodeName())
                || "textgroup".equals(sibling.getNodeName())
                || "nr".equals(sibling.getNodeName())
                || "emphasis".equals(sibling.getNodeName())
        )
                && sibling.getTextContent().trim().length() > 0;
    }

    public static Node getPreviousNonWhitespaceNode(Element e) {
        Node prev = e;
        do {
            prev = prev.getPreviousSibling();
        } while (prev != null && whitespaceText(prev));
        return prev;
    }

    public static List<File> listXmlFiles(File folder) {
        return listXmlFiles(folder, -1);
    }

    public static List<File> listXmlFiles(File folder, int max) {
        return listXmlFiles(folder, max, true);
    }

    public static List<File> listXmlFiles(File folder, int max, boolean shuffle) {
        File[] files = folder.listFiles();
        if (files == null) throw new NullPointerException();

        List<File> xmls = new ArrayList<>(files.length);
        if (shuffle) {
            for (File file : files) {
                addToListIfXmlFile(xmls, file);
            }
            Collections.shuffle(xmls);
        } else {
            for (File file : files) {
                addToListIfXmlFile(xmls, file);
                if (max > 0 && xmls.size() >= max) break;
            }
        }
        if (max > 0 && max < xmls.size()) {
            return xmls.subList(0, max);
        }
        for (File f : xmls) if (f == null || f.getName() == null) throw new Error();
        return xmls;
    }

    private static void addToListIfXmlFile(List<File> xmls, File file) {
        if (file.getAbsolutePath().endsWith(".xml")) {
            xmls.add(file);
        }
    }

    public static List<File> listXmlFiles() {
        return listXmlFiles(new File(Const.PATH_TRAIN_TEST_XML_FILES_LINUX), -1, false);
    }

    public static File getFile(File folder, String ecli) {
        return new File(folder, ecli.replaceAll(":", "") + ".xml");
    }


    public static Node[] getChildren(Node root) {
        NodeList children = root.getChildNodes();
        Node[] originalChildren = new Node[children.getLength()];
        for (int i = 0; i < children.getLength(); i++) {
            originalChildren[i] = children.item(i);
        }
        return originalChildren;
    }

    public static void setFolders(String[] args) {
        if (args.length > 0 && !Strings.isNullOrEmpty(args[0])) IN_FOLDER = args[0];
        if (args.length > 1 && !Strings.isNullOrEmpty(args[1])) OUT_FOLDER = args[1];
    }

    public static Element setElementNameTo(Element element, String ns, String tagName) {
        Node parent = element.getParentNode();

        Element newElement;
        if (ns != null) newElement = element.getOwnerDocument().createElementNS(ns, tagName);
        else newElement = element.getOwnerDocument().createElement(tagName);


        copyElement(element, newElement);

        parent.insertBefore(newElement, element);
        parent.removeChild(element);
        return newElement;
    }

    private static void copyElement(Element element, Element newElement) {
        Node[] children = getChildren(element);
        for (Node child : children) {
            // element.removeChild(child);
            newElement.appendChild(child);
        }
        copyAttributes(element, newElement);
    }

    public static void copyAttributes(Element copyFrom, Element copyTo) {
        NamedNodeMap attrs = copyFrom.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            copyFrom.removeAttributeNode(attr);
            copyTo.setAttributeNode(attr);
        }
    }

    /**
     * Replaces the given element with its children.
     *
     * @param element
     */
    public static void dissolveTag(Element element) {
        Node[] children = getChildren(element);

        Node parent = element.getParentNode();

        for (Node child : children) parent.insertBefore(child, element);
        parent.removeChild(element);
    }

    public static boolean containsTag(Node root, String tagName) {
        if (root.getNodeName().equals(tagName)) return true;

        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (containsTag(children.item(i), tagName)) return true;
        }

        return false;
    }

    /**
     * If this would result in an overlapping tag, the algorithm cuts the container elements so that
     * we end up with a valid XML tree
     *
     * @param startAt   inclusive
     * @param stopAfter inclusive
     */
    public static void wrapSubTreeInElement(Node startAt, Node stopAfter, String tagName) {
        Node parent = startAt.getParentNode();
        Node[] children = getChildren(parent);

        Pair<Node, Node> p = makeSureThatNodesHaveSameParent(startAt, stopAfter);
        startAt = p.getKey();
        stopAfter = p.getKey();

        Element newElement = parent.getOwnerDocument().createElement(tagName);
        Node next = startAt;
        Node last;
        do {
            newElement.appendChild(next);
            last = next;
            next = next.getNextSibling();
        } while (next != null && last != stopAfter);
    }

    private static Pair<Node, Node> makeSureThatNodesHaveSameParent(Node first, Node second) {
        if (!Objects.equals(first.getParentNode(), second.getParentNode())) {
            Pair<Deque<Node>, Deque<Node>> pathUpToCommonAncestor = getPathUpToCommonAncestor(first, second);//todo just return common ancestor
            if (!Objects.equals(pathUpToCommonAncestor.getValue().getLast(), pathUpToCommonAncestor.getKey().getLast()))
                throw new IllegalStateException();

            // Close whatever the nodes are in, up to the common ancestor
            Deque<Node> firstPath = pathUpToCommonAncestor.getKey();
            Node commonAncestor = firstPath.getLast();
            Node node1 = first;
            while (!Objects.equals(node1.getParentNode(), commonAncestor)) node1 = cutParentAtNode(node1);
            Node node2 = second;
            while (!Objects.equals(node2.getParentNode(), commonAncestor)) node2 = cutParentAtNode(node2);

            return new Pair<>(node1, node2);
        } else return new Pair<>(first, second);
    }

    private static Element cutParentAtNode(Node n) {
        if (n.getParentNode().getNodeType() != Node.ELEMENT_NODE)
            throw new InvalidParameterException("Parent must be element.");
        Element parent = (Element) n.getParentNode();
        Element newElement = n.getOwnerDocument().createElement(parent.getTagName());
        getPrevSiblings(n).forEach(newElement::appendChild);
        parent.getParentNode().insertBefore(newElement, parent);
        copyAttributes(parent, newElement);
        return newElement;
    }


    private static List<Node> getNodeWithNextSiblings(Node node) {
        List<Node> nodes = Lists.newArrayList(node);
        while (node.getNextSibling() != null) {
            node = node.getNextSibling();
            nodes.add(node);
        }
        return nodes;
    }

    private static Deque<Node> getPrevSiblings(Node node) {
        Deque<Node> nodes = new ArrayDeque<>();
        while (node.getPreviousSibling() != null) {
            node = node.getPreviousSibling();
            nodes.addFirst(node);
        }
        return nodes;
    }

    private static Pair<Deque<Node>, Deque<Node>> getPathUpToCommonAncestor(Node first, Node second) {
        Deque<Node> firstAncestors = new ArrayDeque<>();
        Deque<Node> secondAncestors = new ArrayDeque<>();

        Node l1 = first;
        Node l2 = second;

        while (l1 != null || l2 != null) {
            firstAncestors.add(l1);
            secondAncestors.add(l2);
            if (firstAncestors.contains(l2))
                return new Pair<>(Collections3.upToAndIncluding(firstAncestors, l2), secondAncestors);
            if (secondAncestors.contains(l1))
                return new Pair<>(firstAncestors, Collections3.upToAndIncluding(secondAncestors, l1));
            if (l1 != null) l1 = l1.getParentNode();
            if (l2 != null) l2 = l2.getParentNode();
        }

        throw new InvalidParameterException("No common ancestors exists for given nodes.");
    }
}
