package org.leibnizcenter.rechtspraak.util;

import com.google.common.collect.Lists;
import com.sun.org.apache.xerces.internal.dom.DOMOutputImpl;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.transform.TransformerException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maarten on 22-12-15.
 */
public class Xml extends HashMap<Integer, Node> {
    public static Element wrapSubstringInElement(
            Text node,
            int startIndex,
            int length,
            String tagName) {
        Text middle = node.splitText(startIndex);
        middle.splitText(length);

        Element newElement = node.getOwnerDocument().createElement(tagName);

        Node parent = node.getParentNode();

        parent.replaceChild(newElement, middle);
        newElement.appendChild(middle);

        return newElement;
    }


    public static String toString(Document document) throws TransformerException,
            InstantiationException, IllegalAccessException, ClassNotFoundException {
        DOMImplementationLS domImplLS = (DOMImplementationLS) document
                .getImplementation();

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
}
