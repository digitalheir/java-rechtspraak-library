package org.leibnizcenter.rechtspraak;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Response;
import generated.OpenRechtspraak;
import nl.rechtspraak.psi.Procedure;
import org.joda.time.DateTime;
import org.purl.dc.terms.*;
import org.w3._1999._02._22_rdf_syntax_ns_.Description;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by maarten on 28-9-15.
 */
public class CouchInterface extends RechtspraakNlInterface {
    static final Gson gson = new GsonBuilder()
            .create();
    public static Response request(String ecli) throws IOException, JAXBException, XPathExpressionException {
        URI uri = URI.create(getXmlUrl(ecli));
        HttpUrl url = HttpUrl.get(uri);
        return new DocumentRequest(url).execute();
    }

    public static String getXmlUrl(String ecli) {
        return "http://rechtspraak.cloudant.com/ecli/" + ecli + "/data.xml";
    }

    public static String toJson(Object desc) {
        return gson.toJson(desc);
    }

}
