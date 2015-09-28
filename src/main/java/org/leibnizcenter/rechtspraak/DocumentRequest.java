package org.leibnizcenter.rechtspraak;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import generated.OpenRechtspraak;
import org.leibnizcenter.helpers.SimpleNamespaceContext;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.HashMap;

/**
 * For quering a particular Rechtspraak XML document.
 * <p/>
 * Created by maarten on 31-7-15.
 */
public class DocumentRequest {

    private static final HashMap<String, String> prefMap = new HashMap<String, String>() {{
        put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    }};
    private static final SimpleNamespaceContext namespaces = new SimpleNamespaceContext(prefMap);
    private final Request request;
    /**
     * Client for doing HTTP requests
     */
    private final OkHttpClient httpClient = new OkHttpClient();
    {
        httpClient.setFollowRedirects(false);
    }

    public DocumentRequest(String ecli, SearchRequest.ReturnType type) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("data.rechtspraak.nl")
                .addPathSegment("uitspraken")
                .addPathSegment("content")
                .addQueryParameter("id", ecli)
                .addQueryParameter("return", type.toString())
                .build();

        request = (new Request.Builder().url(url)).build();
    }

    public DocumentRequest(String ecli) {
        this(ecli, SearchRequest.ReturnType.DOC);
    }

    /**
     * @return Response
     */
    public Response getResponse() throws IOException {
        return httpClient.newCall(request).execute();
    }

    public OpenRechtspraak execute() throws IOException, XPathExpressionException, JAXBException {
        Response response = getResponse();
        if (response.code() != 200)
            throw new IllegalStateException("URL " + request.url() + " returned code " + response.code());
        return RechtspraakNlInterface.parseDocument(response.body().byteStream());
    }

    public Request getRequest() {
        return request;
    }
}
