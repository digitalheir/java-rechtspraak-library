package org.leibnizcenter.rechtspraak;

import generated.OpenRechtspraak;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maarten on 28-9-15.
 */
public class RechtspraakNlInterface {

    public static OpenRechtspraak parseDocument(String ecli) throws IOException, JAXBException, XPathExpressionException {
        return new DocumentRequest(ecli).execute();
    }

    public static String getDocumentXmlUrl(String ecli) {
        return "http://data.rechtspraak.nl/uitspraken/content?id=" + ecli;
    }

    public static OpenRechtspraak parseDocument(InputStream isXml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(
                OpenRechtspraak.class,
                org.purl.dc.terms.ObjectFactory.class,
                nl.rechtspraak.psi.ObjectFactory.class,
                org.w3._1999._02._22_rdf_syntax_ns_.ObjectFactory.class,
                generated.ObjectFactory.class
        );
        Unmarshaller um = context.createUnmarshaller();
        return (OpenRechtspraak) um.unmarshal(isXml);
    }
}
