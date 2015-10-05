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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test the JSON-LD output of Rechtspraak.nl XML documents
 * <p/>
 * Created by maarten on 31-7-15.
 */
public class ToJsonTest {

    ArrayList<String> testDocs = new ArrayList<>();
    private static Map<String, String> validateJson = new HashMap<>();

    static {
        validateJson.put("ECLI:NL:GHSHE:2014:1641", "{\"_id\":\"ECLI:NL:GHSHE:2014:1641\",\"owl:sameAs\":\"http://deeplink.rechtspraak.nl/uitspraak?id\\u003dECLI:NL:GHSHE:2014:1641\",\"@context\":[\"https://rechtspraak.cloudant.com/assets/assets/context.jsonld\",{\"Vervangt\":\"http://purl.org/dc/terms/replaces\",\"Procedure\":\"http://psi.rechtspraak.nl/procedure\",\"Instantie\":\"http://purl.org/dc/terms/creator\",\"Zaaknr\":\"http://psi.rechtspraak.nl/zaaknummer\"}],\"ecli\":\"ECLI:NL:GHSHE:2014:1641\",\"corpus\":\"Rechtspraak.nl\",\"@type\":\"frbr:LegalWork\",\"source\":\"http://data.rechtspraak.nl/uitspraken/content?id\\u003dECLI:NL:GHSHE:2014:1641\",\"abstract\":{\"resourceIdentifier\":\"../../rs:inhoudsindicatie\",\"abstractXml\":\"\\u003c?xml version\\u003d\\\"1.0\\\" encoding\\u003d\\\"UTF-8\\\" standalone\\u003d\\\"yes\\\"?\\u003e\\u003cns8:inhoudsindicatie xml:space\\u003d\\\"preserve\\\" id\\u003d\\\"ECLI:NL:GHSHE:2014:1641:INH\\\" lang\\u003d\\\"nl\\\" xmlns:ns6\\u003d\\\"bwb-dl\\\" xmlns:ns5\\u003d\\\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\\\" xmlns:ns8\\u003d\\\"http://www.rechtspraak.nl/schema/rechtspraak-1.0\\\" xmlns:ns7\\u003d\\\"https://e-justice.europa.eu/ecli\\\" xmlns:ns2\\u003d\\\"http://purl.org/dc/terms/\\\" xmlns:ns4\\u003d\\\"http://psi.rechtspraak.nl/\\\" xmlns:ns3\\u003d\\\"http://www.w3.org/2000/01/rdf-schema#\\\"\\u003e\\n      \\u003cns8:para\\u003edwaling over pensioenrechten bij beëindiging arbeidsovereenkomst\\u003c/ns8:para\\u003e\\n    \\u003c/ns8:inhoudsindicatie\\u003e\",\"@value\":\"dwaling over pensioenrechten bij beëindiging arbeidsovereenkomst\"},\"accessRights\":\"public\",\"coverage\":{\"@id\":\"nl\",\"rdfs:label\":[{\"@value\":\"Nederland\",\"@language\":\"nl\"}]},\"hasVersion\":[\"Rechtspraak.nl\",\"AR-Updates.nl 2014-0513\"],\"relation\":[{\"rdfs:label\":[{\"@value\":\"Einduitspraak: ECLI:NL:GHSHE:2015:1672\",\"@language\":\"nl\"}],\"@id\":\"ECLI:NL:GHSHE:2015:1672\",\"aanleg\":\"http://psi.rechtspraak.nl/latereAanleg\",\"type\":\"http://psi.rechtspraak.nl/tussenuitspraak\"}],\"zaaknummer\":[\"HD 200.132.399_01\"],\"subject\":[{\"rdfs:label\":[{\"@value\":\"Civiel recht; Arbeidsrecht\",\"@language\":\"nl\"}],\"@id\":\"http://psi.rechtspraak.nl/rechtsgebied#civielRecht_arbeidsrecht\"}],\"metadataModified\":\"2015-06-23T13:33:50.000+02:00\",\"contentModified\":\"2014-06-05T10:23:38.000+02:00\",\"issued\":\"2014-06-03T00:00:00.000+02:00\",\"htmlIssued\":\"2014-06-05T00:00:00.000+02:00\",\"page\":\"http://rechtspraak.lawreader.nl/ecli/ECLI:NL:GHSHE:2014:1641\",\"creator\":{\"scheme\":\"overheid.RechterlijkeMacht\",\"rdfs:label\":[{\"@value\":\"Gerechtshof \\u0027s-Hertogenbosch\",\"@language\":\"nl\"}],\"@id\":\"http://standaarden.overheid.nl/owms/terms/Gerechtshof_\\u0027s-Hertogenbosch\"},\"date\":\"2014-06-03T00:00:00.000+02:00\",\"language\":{\"rdfs:label\":[{\"@value\":\"Nederlands\",\"@language\":\"nl\"}],\"@id\":\"nl\"},\"publisher\":{\"rdfs:label\":[{\"@value\":\"Raad voor de Rechtspraak\",\"@language\":\"nl\"}],\"@id\":\"http://rechtspraak.nl/\"},\"spatial\":{\"rdfs:label\":[{\"@value\":\"\\u0027s-Hertogenbosch\",\"@language\":\"nl\"}],\"@id\":\"%27s-Hertogenbosch\"},\"couchDbUpdated\":\"2015-09-29T19:33:31.564+02:00\",\"about\":\"http://deeplink.rechtspraak.nl/uitspraak?id\\u003dECLI:NL:GHSHE:2014:1641\",\"title\":{\"@value\":\"ECLI:NL:GHSHE:2014:1641 Gerechtshof \\u0027s-Hertogenbosch , 03-06-2014 / HD 200.132.399_01\",\"language\":\"nl\"},\"procedure\":[],\"type\":{\"rdfs:label\":[{\"@value\":\"Uitspraak\",\"@language\":\"nl\"}],\"@id\":\"http://psi.rechtspraak.nl/uitspraak\"}}");
    }

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

//                if (validateJson.get(doc._id) != null) {
//                    assertEquals(validateJson.get(doc._id), CouchInterface.toJson(doc));
//                }
            }
        } catch (IOException | JAXBException | URISyntaxException | TransformerException e) {
            throw new Error(e);
        }
    }

    private void assertAllRechtspraakMarkup(List<Object> content, int i) {
    }
}
