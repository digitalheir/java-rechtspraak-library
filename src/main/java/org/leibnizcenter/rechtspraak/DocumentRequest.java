package org.leibnizcenter.rechtspraak;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.leibnizcenter.helpers.SimpleNamespaceContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.HashMap;

/**
 * For quering a particular Rechtspraak XML document.
 *
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

    public DocumentRequest(String id) {
        this(id, SearchRequest.ReturnType.DOC);
    }

    /**
     * @return Response
     */
    public Response getResponse() throws IOException {
        return httpClient.newCall(request).execute();
    }

    public void execute() throws IOException, XPathExpressionException {
        Response response = getResponse();

        final XPathFactory xpathFactory = XPathFactory.newInstance();
        final XPath xpath = xpathFactory.newXPath();
        xpath.setNamespaceContext(namespaces);
        final InputSource xml = new InputSource(response.body().byteStream());

        //TODO
        // Node rdfTag = getRdfTag(xpath, xml);
        // MetadataParser metadataParser = new MetadataParser(rdfTag,xpath);

    }


    private Node getRdfTag(XPath xpath, InputSource xml) throws XPathExpressionException, UnexpectedException {
        final NodeList list = (NodeList) xpath.evaluate("/open-rechtspraak/rdf:RDF", xml, XPathConstants.NODESET);
        if (list.getLength() != 1) {
            throw new UnexpectedException("Expected just one RDF tag");
        }
        return list.item(0);
    }

    public Request getRequest() {
        return request;
    }
}
