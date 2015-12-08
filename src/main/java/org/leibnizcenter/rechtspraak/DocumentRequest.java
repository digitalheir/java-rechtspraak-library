package org.leibnizcenter.rechtspraak;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import generated.OpenRechtspraak;
import org.jsoup.HttpStatusException;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

/**
 * For quering a particular Rechtspraak XML document.
 * <p/>
 * Created by maarten on 31-7-15.
 */
public class DocumentRequest {

    //    private static final HashMap<String, String> prefMap = new HashMap<String, String>() {{
//        put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
//    }};
//    private static final SimpleNamespaceContext namespaces = new SimpleNamespaceContext(prefMap);
    private final Request request;
    /**
     * Client for doing HTTP requests
     */
    private final OkHttpClient httpClient = new OkHttpClient();

    {
        httpClient.setFollowRedirects(false);
    }

    public DocumentRequest(HttpUrl url, org.leibnizcenter.rechtspraak.SearchRequest.ReturnType type) {
        request = (new Request.Builder().url(url)).build();
    }

    public DocumentRequest(HttpUrl url) {
        this(url, org.leibnizcenter.rechtspraak.SearchRequest.ReturnType.DOC);
    }

    /**
     * @return Response
     */
    public Response getResponse() throws IOException {
        return httpClient.newCall(request).execute();
    }

    public Response execute() throws IOException {
        Response response = getResponse();
        if (response.code() != 200)
            throw new HttpStatusException("Did not get HTTP code 200", response.code(), request.url().toString());
        return response;
    }

    public OpenRechtspraak executeAndParse() throws IOException, XPathExpressionException, JAXBException {
        return RechtspraakNlInterface.parseXml(execute().body().byteStream());
    }

    public Request getRequest() {
        return request;
    }
}
