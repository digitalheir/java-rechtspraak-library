package org.leibnizcenter.util;

import org.junit.Test;
import org.leibnizcenter.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;

/**
 * Created by maarten on 15-3-16.
 */
public class XmlTest {

    @Test
    public void testSibling() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(
                "<?xml version=\"1.0\"?>" +
                        "<xml>" +
                        "hello" +
                        "</xml>")));

        Node n = (Text) doc.getDocumentElement()
                .getChildNodes()
                .item(0);

        while(n != null){
            n=n.getPreviousSibling(); // Will never finish
        }
    }

    @Test
    public void main() throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = XmlTest.class.getResourceAsStream("/docs/ECLI.NL.CBB.2013.345.xml");
        Document doc = builder.parse(new InputSource(new InputStreamReader(is)));

        Element root = Xml.getContentRoot(doc);
        Node firstPara = root.getChildNodes().item(1);
        Text txt = (Text) firstPara.getChildNodes().item(0);
        Element newElement = Xml.wrapSubstringInElement(txt, 1, 1, "letter");
        assertTrue(Xml.toString(doc).contains("<para>u<letter>i</letter>tspraak </para>"));

        is = XmlTest.class.getResourceAsStream("/docs/ECLI.NL.CBB.2013.345.xml");
        doc = builder.parse(new InputSource(new InputStreamReader(is)));
        root = Xml.getContentRoot(doc);
        firstPara = root.getChildNodes().item(1);
        txt = (Text) firstPara.getChildNodes().item(0);
        newElement = Xml.wrapSubstringInElement(txt, 1, "letter");
        assertTrue(Xml.toString(doc).contains("<para><letter>u</letter>itspraak </para>"));
    }
}