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
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by maarten on 28-9-15.
 */
public class RechtspraakNlInterface {
    public static Response requestXmlForEcli(String ecli) throws IOException, JAXBException, XPathExpressionException {
        URI uri = URI.create(getXmlUrl(ecli));
        HttpUrl url = HttpUrl.get(uri);
        return new DocumentRequest(url).execute();
    }

    public static String getXmlUrl(String ecli) {
        return "http://data.rechtspraak.nl/uitspraken/content?id=" + ecli;
    }

    public static OpenRechtspraak parseXml(InputStream isXml) throws JAXBException {
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

    public static String xmlToHtml(String xmlStr) throws URISyntaxException, TransformerException {
//            ByteArrayInputStream is
        return xmlToHtml(new ByteArrayInputStream(xmlStr.getBytes()));
    }

    public static String xmlToHtml(ByteArrayInputStream is) throws URISyntaxException, TransformerException {
        File stylesheet = new File(
                CouchDoc.class.getResource("/xslt/rechtspraak_to_html.xslt").toURI()
        );
        StreamSource stylesource = new StreamSource(stylesheet);
        Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);


        StringWriter sw = new StringWriter();
        transformer.transform(new StreamSource(is), new StreamResult(sw));
        return sw.toString();
    }

    public static OpenRechtspraak parseXml(String str) throws JAXBException, UnsupportedEncodingException {
        InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
        return parseXml(is);
    }

//    public static org.jsoup.nodes.Element getUitspraakOrConclusieXmlElement(String strXml) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
//        org.jsoup.nodes.Document doc = Jsoup.parse(strXml, "http://data.rechtspraak.nl/uitspraken/", Parser.xmlParser());
//        Elements els = doc.select("open-rechtspraak > uitspraak,conclusie");
//        if (els.size() != 1)
//            throw new IllegalStateException("Document should have exactly one uitspraak or conclusie");
//        return els.get(0);
//    }

    public static RechtspraakContent getUitspraakOrConclusie(OpenRechtspraak doc) {
        Uitspraak u = doc.getUitspraak();
        Conclusie c = doc.getConclusie();
        if ((u == null && c == null) || (u != null && c != null)) {
            throw new IllegalStateException("Document should have exactly one uitspraak or conclusie");
        }
        return u == null ? c : u;
    }
}
