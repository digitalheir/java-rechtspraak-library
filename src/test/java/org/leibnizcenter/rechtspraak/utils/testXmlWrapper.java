package org.leibnizcenter.rechtspraak.utils;

import org.junit.Test;
import org.leibnizcenter.rechtspraak.util.Xml;
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

import static org.junit.Assert.assertTrue;

/**
 * Created by maarten on 15-3-16.
 */
public class testXmlWrapper {

    @Test
    public void main() throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = testXmlWrapper.class.getResourceAsStream("/docs/ECLI.NL.CBB.2013.345.xml");
        Document doc = builder.parse(new InputSource(new InputStreamReader(is)));

        Element root = Xml.getContentRoot(doc);
        Node firstPara = root.getChildNodes().item(1);
        Text txt = (Text) firstPara.getChildNodes().item(0);
        Xml.wrapSubstringInElement(txt, 1, 1, "letter");
        assertTrue(Xml.toString(doc).contains("<para>u<letter>i</letter>tspraak </para>"));
    }
}
