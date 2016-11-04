import generated.OpenRechtspraak;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.CouchDoc;
import org.leibnizcenter.rechtspraak.CouchInterface;
import org.leibnizcenter.rechtspraak.enricher.Enrich;
import org.leibnizcenter.xml.NotImplemented;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * Test the JSON-LD output of Rechtspraak.nl XML documents
 * <p>
 * Created by maarten on 31-7-15.
 */
public class ToJsonTest {
    private ArrayList<String> testDocs = new ArrayList<>();
    private ArrayList<String> testDocsToEnrich = new ArrayList<>();

    {
        testDocs.add("ECLI:NL:GHSHE:2014:1641");
//        testDocs.add("ECLI:NL:GHAMS:2013:2283");
//        testDocs.add("ECLI:NL:CRVB:2013:1886");
//        testDocs.add("ECLI:NL:RBDHA:2013:15631");
    }

    {
//        testDocsToEnrich.add("ECLI:NL:CBB:1996:ZG0749");
//        testDocsToEnrich.add("ECLI:NL:CBB:1997:ZG2071");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AA3411");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AA3430");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AA3434");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AA3436");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AA3437");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AA3581");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AU1293");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AU1323");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:AU1332");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:ZG1037");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:ZG1086");
//        testDocsToEnrich.add("ECLI:NL:CBB:1998:ZG1801");
//        testDocsToEnrich.add("ECLI:NL:CBB:1999:AA3582");
//        testDocsToEnrich.add("ECLI:NL:CBB:1999:AA3720");
//        testDocsToEnrich.add("ECLI:NL:CBB:1999:AA4117");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA4856");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA6439");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA6441");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA7087");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA7124");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA7920");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA8268");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9088");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9101");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9102");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9117");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9133");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9187");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9276");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9277");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AA9280");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AG3671");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AN6510");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1244");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1247");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1249");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1251");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1256");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1259");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1261");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1262");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1263");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:AU1266");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:ZG1820");
//        testDocsToEnrich.add("ECLI:NL:CBB:2000:ZG2018");
//        testDocsToEnrich.add("ECLI:NL:CBB:2001:AA9413");
//        testDocsToEnrich.add("ECLI:NL:CBB:2001:AA9497");
//        testDocsToEnrich.add("ECLI:NL:CBB:2001:AA9499");
//        testDocsToEnrich.add("ECLI:NL:CBB:2001:AA9501");
    }

    @Test
    public void testDocsToEnrich() throws NotImplemented, ClassNotFoundException {
        List<CouchDoc> parsedDocs = new ArrayList<>(testDocsToEnrich.size());
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Enrich enricher = new Enrich();
            for (String ecli : testDocsToEnrich) {
                String strXml;
                URL resource = getClass().getResource("/(e)x(a)m(p)l(e)/" + ecli.replaceAll(":", ".") + ".xml");
                byte[] encoded = Files.readAllBytes(Paths.get(resource.toURI()));
                strXml = new String(encoded, "UTF-8");

                OpenRechtspraak doc = CouchInterface.parseXml(strXml);
                CouchDoc couchDoc = new CouchDoc(doc, strXml, enricher, factory);
                parsedDocs.add(couchDoc);
            }

            for (CouchDoc doc : parsedDocs) {
                String json = CouchInterface.toJson(doc);

                assertNotNull(json);
                assertEquals("Rechtspraak.nl", doc.corpus);
                assertEquals("frbr:LegalWork", doc._type);
                assertEquals(doc.sameAs, "http://deeplink.rechtspraak.nl/uitspraak?id=" + doc.ecli);
                assertNotNull(doc.context);
//                if (doc._id.equals("ECLI:NL:CBB:2000:AU1262"))
//                    System.out.println(json);
//                if (validateJson.get(doc._id) != null) {
//                }
            }
        } catch (IOException | JAXBException | URISyntaxException | TransformerException | SAXException | ParserConfigurationException | InstantiationException | IllegalAccessException e) {
            throw new Error(e);
        }
    }
    public void testDocsThatAreAlreadyRich() throws NotImplemented, ClassNotFoundException {
        List<CouchDoc> parsedDocs = new ArrayList<>(testDocs.size());
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Enrich enricher = new Enrich();
            for (String ecli : testDocs) {
                String strXml;
                URL resource = getClass().getResource("/(e)x(a)m(p)l(e)/" + ecli.replaceAll(":", ".") + ".xml");
                byte[] encoded = Files.readAllBytes(Paths.get(resource.toURI()));
                strXml = new String(encoded, "UTF-8");

                OpenRechtspraak doc = CouchInterface.parseXml(strXml);
                CouchDoc couchDoc = new CouchDoc(doc, strXml, enricher, factory);
                parsedDocs.add(couchDoc);
            }

            for (CouchDoc doc : parsedDocs) {
                String json = CouchInterface.toJson(doc);

                assertNotNull(json);
                assertEquals("Rechtspraak.nl", doc.corpus);
                assertEquals("frbr:LegalWork", doc._type);
                assertEquals(doc.sameAs, "http://deeplink.rechtspraak.nl/uitspraak?id=" + doc.ecli);
                assertNotNull(doc.context);
//                if (doc._id.equals("ECLI:NL:CBB:2000:AU1262"))
//                    System.out.println(json);
//                if (validateJson.get(doc._id) != null) {
//                }
            }
        } catch (IOException | JAXBException | URISyntaxException | TransformerException | SAXException | ParserConfigurationException | InstantiationException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

}
