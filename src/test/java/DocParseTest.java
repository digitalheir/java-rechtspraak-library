import generated.OpenRechtspraak;
import nl.rechtspraak.schema.rechtspraak_1.TRechtspraakMarkup;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.RechtspraakNlInterface;
import org.leibnizcenter.rechtspraak.SearchRequest;
import org.w3._1999._02._22_rdf_syntax_ns_.Description;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 * Test JAXB parsing of Rechtspraak.nl XML
 * </p>
 * Created by maarten on 31-7-15.
 */
public class DocParseTest {

    @Test
    public void testDoc() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("ECLI:NL:GHSHE:2014:1641");

        try {
            OpenRechtspraak doc = RechtspraakNlInterface.parseDocument(list.get(0));

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

//            assertEquals(doc.getUitspraak().getId(), "ECLI:NL:GHSHE:2014:1641:DOC");
//            assertEquals(doc.getUitspraak().getSpace(), "preserve");

            int markups = assertAllRechtspraakMarkup(doc.getUitspraak().getContent(), 0);
            assertTrue(markups > 0);


            // Test innerText string
            String markupStr = doc.getUitspraak().toString().trim();
//            System.out.println(markupStr);
            assertTrue(markupStr.startsWith("GERECHTSHOF ’s-HERTOGENBOSCH"));
            assertTrue(markupStr.endsWith("3 juni 2014."));
        } catch (Exception e) {
            throw new Error(e);
        }
    }


    @Test
    public void impossibleEcliTest() {
        try {
            RechtspraakNlInterface.parseDocument("I should not exist");
            assertEquals("apples", "pears");
        } catch (IOException | JAXBException | XPathExpressionException e) {
            throw new Error(e);
        } catch (IllegalStateException ignored) {
        }
    }

    private int assertAllRechtspraakMarkup(List<Object> content, int i) {
        i++;
        for (Object o : content) {
            //System.out.println(o.getClass());
            if (o instanceof TRechtspraakMarkup) {
                TRechtspraakMarkup markup = (TRechtspraakMarkup) o;
                i += assertAllRechtspraakMarkup(markup.getContent(), 0);
//                if (markup instanceof Para) {
//                    Para p = (Para) markup;
                //System.out.println(p.getContent());
//                }
//            }else if(o instanceof String){
//                System.out.println(o);
            }
        }
        return i;
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


        } catch (IOException | ParserConfigurationException | SAXException | ParseException e) {
            throw new Error(e);
        }
    }

}
