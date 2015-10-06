import generated.OpenRechtspraak;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.CouchDoc;
import org.leibnizcenter.rechtspraak.CouchInterface;

import javax.xml.bind.JAXBException;
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
 * Test the JSON-LD output of Rechtspraak.nl XML documents
 * <p/>
 * Created by maarten on 31-7-15.
 */
public class ToJsonTest {

    ArrayList<String> testDocs = new ArrayList<>();


    {
        testDocs.add("ECLI:NL:GHSHE:2014:1641");
        testDocs.add("ECLI:NL:CBB:1996:ZG0749");
        testDocs.add("ECLI:NL:GHAMS:2013:2283");
        testDocs.add("ECLI:NL:CBB:1997:ZG2071");
        testDocs.add("ECLI:NL:CBB:1997:ZG2071");
        testDocs.add("ECLI:NL:CBB:1998:AA3411");
        testDocs.add("ECLI:NL:CBB:1998:AA3430");
        testDocs.add("ECLI:NL:CBB:1998:AA3434");
        testDocs.add("ECLI:NL:CBB:1998:AA3436");
        testDocs.add("ECLI:NL:CBB:1998:AA3437");
        testDocs.add("ECLI:NL:CBB:1998:AA3581");
        testDocs.add("ECLI:NL:CBB:1998:AU1293");
        testDocs.add("ECLI:NL:CBB:1998:AU1323");
        testDocs.add("ECLI:NL:CBB:1998:AU1332");
        testDocs.add("ECLI:NL:CBB:1998:ZG1037");
        testDocs.add("ECLI:NL:CBB:1998:ZG1086");
        testDocs.add("ECLI:NL:CBB:1998:ZG1801");
        testDocs.add("ECLI:NL:CBB:1999:AA3582");
        testDocs.add("ECLI:NL:CBB:1999:AA3720");
        testDocs.add("ECLI:NL:CBB:1999:AA4117");
        testDocs.add("ECLI:NL:CBB:2000:AA4856");
        testDocs.add("ECLI:NL:CBB:2000:AA6439");
        testDocs.add("ECLI:NL:CBB:2000:AA6441");
        testDocs.add("ECLI:NL:CBB:2000:AA7087");
        testDocs.add("ECLI:NL:CBB:2000:AA7124");
        testDocs.add("ECLI:NL:CBB:2000:AA7920");
        testDocs.add("ECLI:NL:CBB:2000:AA8268");
        testDocs.add("ECLI:NL:CBB:2000:AA9088");
        testDocs.add("ECLI:NL:CBB:2000:AA9101");
        testDocs.add("ECLI:NL:CBB:2000:AA9102");
        testDocs.add("ECLI:NL:CBB:2000:AA9117");
        testDocs.add("ECLI:NL:CBB:2000:AA9133");
        testDocs.add("ECLI:NL:CBB:2000:AA9187");
        testDocs.add("ECLI:NL:CBB:2000:AA9276");
        testDocs.add("ECLI:NL:CBB:2000:AA9277");
        testDocs.add("ECLI:NL:CBB:2000:AA9280");
        testDocs.add("ECLI:NL:CBB:2000:AG3671");
        testDocs.add("ECLI:NL:CBB:2000:AN6510");
        testDocs.add("ECLI:NL:CBB:2000:AU1244");
        testDocs.add("ECLI:NL:CBB:2000:AU1247");
        testDocs.add("ECLI:NL:CBB:2000:AU1249");
        testDocs.add("ECLI:NL:CBB:2000:AU1251");
        testDocs.add("ECLI:NL:CBB:2000:AU1256");
        testDocs.add("ECLI:NL:CBB:2000:AU1259");
        testDocs.add("ECLI:NL:CBB:2000:AU1261");
        testDocs.add("ECLI:NL:CBB:2000:AU1262");
        testDocs.add("ECLI:NL:CBB:2000:AU1263");
        testDocs.add("ECLI:NL:CBB:2000:AU1266");
        testDocs.add("ECLI:NL:CBB:2000:ZG1820");
        testDocs.add("ECLI:NL:CBB:2000:ZG2018");
        testDocs.add("ECLI:NL:CBB:2001:AA9413");
        testDocs.add("ECLI:NL:CBB:2001:AA9497");
        testDocs.add("ECLI:NL:CBB:2001:AA9499");
        testDocs.add("ECLI:NL:CBB:2001:AA9501");
        testDocs.add("ECLI:NL:CRVB:2013:1886");
    }

    @Test
    public void testDoc() {
        List<CouchDoc> parsedDocs = new ArrayList<>(testDocs.size());

        try {
            for (String ecli : testDocs) {
                String strXml;
                URL resource = getClass().getResource("/(e)x(a)m(p)l(e)/" + ecli.replaceAll(":", ".") + ".xml");
                byte[] encoded = Files.readAllBytes(Paths.get(resource.toURI()));
                strXml = new String(encoded, "UTF-8");

                OpenRechtspraak doc = CouchInterface.parseXml(strXml);

                CouchDoc couchDoc = new CouchDoc(doc, strXml);
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
        } catch (IOException | JAXBException | URISyntaxException | TransformerException e) {
            throw new Error(e);
        }
    }

}
