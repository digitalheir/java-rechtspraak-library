package org.leibnizcenter.rechtspraak.markup.docs;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.leibnizcenter.rechtspraak.markup.docs.TestRechtspraakCorpus.getTokenList;

/**
 * Created by maarten on 23-3-16.
 */
public class RechtspraakElementTest {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();

    public RechtspraakElementTest() throws ParserConfigurationException {
    }

    @Test
    public void testEmphasisGet() throws IOException, SAXException {
        LabeledTokenList richDoc = getTokenList(builder, "ECLI:NL:CBB:2013:345");
        assertNull(richDoc.get(40).getToken().getEmphasisSingletonChild());
        assertNull(richDoc.get(0).getToken().getEmphasisSingletonChild());
        assertNull(richDoc.get(7).getToken().getEmphasisSingletonChild());
        assertNotNull(richDoc.get(41).getToken().getEmphasisSingletonChild());
        assertNotNull(richDoc.get(1).getToken().getEmphasisSingletonChild());
        assertNotNull(richDoc.get(4).getToken().getEmphasisSingletonChild());
        assertNotNull(richDoc.get(5).getToken().getEmphasisSingletonChild());
        assertNotNull(richDoc.get(8).getToken().getEmphasisSingletonChild());
    }
}