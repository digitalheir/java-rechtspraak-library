package org.leibnizcenter.rechtspraak;

import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.Response;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URI;

/**
 * Created by maarten on 28-9-15.
 */
public class CouchInterface extends RechtspraakNlInterface {
    @Deprecated
    public static Response request(String ecli) throws IOException, JAXBException, XPathExpressionException {
        URI uri = URI.create(getXmlUrl(ecli));
        HttpUrl url = HttpUrl.get(uri);
        return new DocumentRequest(url).execute();
    }

    @Deprecated
    public static String getXmlUrl(String ecli) {
        return "http://rechtspraak.cloudant.com/ecli/" + ecli + "/data.xml";
    }

    public static String toJson(Object desc) {
        return
                new GsonBuilder().create()
                        .toJson(desc);
    }

}
