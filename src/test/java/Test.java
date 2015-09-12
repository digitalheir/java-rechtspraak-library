import org.leibnizcenter.rechtspraak.DocumentRequest;
import org.leibnizcenter.rechtspraak.SearchRequest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by maarten on 31-7-15.
 */
public class Test {

    public static void main(String[] argh) {
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
            e.printStackTrace();
        }
    }

}
