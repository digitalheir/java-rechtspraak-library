package org.leibnizcenter.rechtspraak;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Response;
import generated.OpenRechtspraak;
import nl.rechtspraak.schema.rechtspraak_1.Conclusie;
import nl.rechtspraak.schema.rechtspraak_1.RechtspraakContent;
import nl.rechtspraak.schema.rechtspraak_1.Uitspraak;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.URI;

/**
 * Created by maarten on 28-9-15.
 */
public class RechtspraakNlInterface {
    public static Response request(String ecli) throws IOException, JAXBException, XPathExpressionException {
        URI uri = URI.create(getXmlUrl(ecli));
        HttpUrl url = HttpUrl.get(uri);
        return new DocumentRequest(url).execute();
    }

    public static String getXmlUrl(String ecli) {
        return "http://data.rechtspraak.nl/uitspraken/content?id=" + ecli;
    }

    public static OpenRechtspraak parseEcliXml(InputStream isXml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(
                OpenRechtspraak.class,
                org.purl.dc.terms.ObjectFactory.class,
                nl.rechtspraak.psi.ObjectFactory.class,
                org.w3._1999._02._22_rdf_syntax_ns_.ObjectFactory.class,
                generated.ObjectFactory.class
        );
        Unmarshaller um = context.createUnmarshaller();
        OpenRechtspraak doc = (OpenRechtspraak) um.unmarshal(isXml);

        String abstractXml = doc.getInhoudsindicatie().getXml();
        String simpleAbstract = doc.getInhoudsindicatie().toString().trim();
        if (simpleAbstract.length() > 0 && !simpleAbstract.equals("-")) {
            doc.getRDF().getDescription().get(1).getAbstract().setAbstractXml(abstractXml);
            doc.getRDF().getDescription().get(1).getAbstract().setAbstractSimple(simpleAbstract);
        }

        return doc;
    }

    public static OpenRechtspraak parseEcliXml(String str) throws JAXBException, UnsupportedEncodingException {
        InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
        return parseEcliXml(is);
    }

    public static org.jsoup.nodes.Element getUitspraakOrConclusieXmlElement(String strXml) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        org.jsoup.nodes.Document doc = Jsoup.parse(strXml, "http://data.rechtspraak.nl/uitspraken/", Parser.xmlParser());
        Elements els = doc.select("open-rechtspraak > uitspraak,conclusie");
        if (els.size() != 1)
            throw new IllegalStateException("Document should have exactly one uitspraak or conclusie");
        return els.get(0);
    }

    public static RechtspraakContent getUitspraakOrConclusie(OpenRechtspraak doc) {
        Uitspraak u = doc.getUitspraak();
        Conclusie c = doc.getConclusie();
        if ((u == null && c == null) || (u != null && c != null)) {
            throw new IllegalStateException("Document should have exactly one uitspraak or conclusie");
        }
        return u == null ? c : u;
    }
}
