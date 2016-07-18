import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.SearchRequest;
import org.leibnizcenter.rechtspraak.SearchResult;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Tests a simple search request
 *
 * Created by maarten on 9-3-16.
 */
public class SearchRequestTest {

    @Test
    public void testIteration() throws ParserConfigurationException, SAXException, IOException {
        SearchResult iterator = new SearchRequest.Builder().build()
                .execute();

        Assert.assertTrue(iterator.hasNext());
        iterator = iterator.next();
        Assert.assertTrue(iterator.hasNext());
        iterator = iterator.next();
        Assert.assertTrue(iterator.hasNext());
    }
}
