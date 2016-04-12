package org.leibnizcenter.rechtspraak.enricher;

import org.junit.Test;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.leibnizcenter.rechtspraak.util.XmlTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static org.junit.Assert.*;

/**
 * Created by maarten on 7-4-16.
 */
public class EnrichTest {
    @Test
    public void enrich() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = XmlTest.class.getResourceAsStream("/docs/ECLI.NL.RVS.2015.3394.xml");
        Document doc = builder.parse(new InputSource(new InputStreamReader(is)));

        new Enrich().enrich("someecli", doc);

        Xml.writeToStream(doc, new OutputStreamWriter(System.out));
    }

    @Test
    public void cleanUp() throws Exception {

    }
}