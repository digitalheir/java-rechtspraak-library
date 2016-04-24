package org.leibnizcenter.rechtspraak;

import generated.OpenRechtspraak;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.HttpStatusException;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

/**
 * For quering a particular Rechtspraak XML document.
 * <p/>
 * Created by maarten on 31-7-15.
 */
@SuppressWarnings("WeakerAccess")
public class DocumentRequest {

    //    private static final HashMap<String, String> prefMap = new HashMap<String, String>() {{
//        put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
//    }};
//    private static final SimpleNamespaceContext namespaces = new SimpleNamespaceContext(prefMap);
    private final Request request;
    /**
     * Client for doing HTTP requests
     */
    private final OkHttpClient httpClient = new OkHttpClient.Builder().followRedirects(false).build();

    public DocumentRequest(HttpUrl url) {
        request = (new Request.Builder().url(url)).build();
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

    @SuppressWarnings("unused")
    public OpenRechtspraak executeAndParse() throws IOException, XPathExpressionException, JAXBException {
        return RechtspraakNlInterface.parseXml(execute().body().byteStream());
    }

    public Request getRequest() {
        return request;
    }
}
