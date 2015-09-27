import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import generated.ObjectFactory;
import generated.OpenRechtspraak;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.DocumentRequest;
import org.leibnizcenter.rechtspraak.SearchRequest;
import org.w3._1999._02._22_rdf_syntax_ns_.Description;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the JAXB parsing of Rechtspraak.nl
 * <p>
 * Created by maarten on 31-7-15.
 */
public class DocParseTest {

    @Test
    public void testDoc() {
        String url = "http://rechtspraak.cloudant.com/ecli/ECLI:NL:GHSHE:2014:1641/data.xml";

        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(HttpUrl.get(URI.create(url))).build();
        try {
            Response res = client.newCall(req).execute();
            JAXBContext context = JAXBContext.newInstance(OpenRechtspraak.class, ObjectFactory.class, org.purl.dc.terms.ObjectFactory.class);
            Unmarshaller um = context.createUnmarshaller();
            OpenRechtspraak doc = (OpenRechtspraak) um.unmarshal(res.body().byteStream());


            List<Description> descs = doc.getRDF().getDescription();
            assertEquals(descs.size(), 2);

            Description desc1 = descs.get(0);
            Description desc2 = descs.get(1);

            // Test some basic facts
            assertEquals(desc1.getAccessRights(), "public");
            assertEquals(desc1.getCoverage(), "NL");
            assertEquals(desc1.getCreator().getValue(), "Gerechtshof 's-Hertogenbosch");
            assertEquals(desc2.getIdentifier(), "http://deeplink.rechtspraak.nl/uitspraak?id=ECLI:NL:GHSHE:2014:1641");
            assertEquals(desc2.getIssued().getLabel(), "Publicatiedatum");

            assertNull(doc.getConclusie());
            assertNotNull(doc.getUitspraak());

            assertEquals(doc.getUitspraak().getId(), "ECLI:NL:GHSHE:2014:1641:DOC");
            assertEquals(doc.getUitspraak().getSpace(), "preserve");
        } catch (JAXBException | IOException e) {
            throw new Error(e);
        }
    }

    public void testList() {
        try {
            List<SearchRequest.JudgmentMetadata> list = (new SearchRequest.Builder())
                    .modified(
                            SearchRequest.Builder.DATE_FORMAT.parse("2014-02-03"),
                            SearchRequest.Builder.DATE_FORMAT.parse("2014-02-04")
                    )
                    .date(
                            SearchRequest.Builder.DATE_FORMAT.parse("2014-02-03"),
                            SearchRequest.Builder.DATE_FORMAT.parse("2014-02-04")
                    ).build().execute();
            System.out.println(list);

            new DocumentRequest(list.get(0).id).execute();
        } catch (IOException | ParserConfigurationException | SAXException | ParseException | XPathExpressionException e) {
            throw new Error(e);
        }
    }

}
