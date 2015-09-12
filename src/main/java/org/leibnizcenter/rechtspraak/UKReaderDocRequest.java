package org.leibnizcenter.rechtspraak;

import com.squareup.okhttp.HttpUrl;

public class DocRequestBuilder {
    private final UkUriObject url;
    final HttpUrl.Builder mBuilder;

    public UKReaderDocRequest() {
        mBuilder = new HttpUrl.Builder();
    }

            return Response.success(ld, HttpHeaderParser.parseCacheHeaders(response));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return Response.error(new VolleyError("Could not process feed: " + url.feedURL));
        }
    }

    private ReaderDoc getDocFromEntry(LegislationAtomFeedParser.Entry entry) throws IOException, ParserConfigurationException, SAXException {
        DocReference doc = new DocReference(
                entry.id,
                entry.title,
                null,
                entry.summary,
                entry.updated
        );
        if (entry.alternatives.get("application/xhtml+xml") != null) {
            return getDocWithHtml(url.focusedUri, entry, doc);
        } else if (entry.alternatives.get("application/pdf") != null) {
            return new PDFDoc(doc, entry.alternatives.get("application/pdf").url);
        } else {
            throw new IOException("Found no manifestations for document " + url.feedURL);
        }
    }


    @Override
    protected void deliverResponse(ReaderDoc response) {
        if (response == null) {
            error.onErrorResponse(new VolleyError("Could not retrieve document information"));
        } else {
            success.onResponse(response);
        }
    }

    private LegislationAtomFeedParser.Entry getEntryFromFeed(NetworkResponse response) throws ParserConfigurationException, SAXException, IOException {
        LegislationAtomFeedParser.AtomFeed feed = LegislationAtomFeedParser.getAtomFeedFromInputStream(new ByteArrayInputStream(response.data));
        if (feed.size() < 1) {
            throw new IOException("Feed contained " + feed.size() + " items.");
        }
        if (feed.size() > 1) {
            Log.w(TAG, "Found " + feed.size() + " items in feed " + url.feedURL);
        }
        return feed.get(0);
    }

    private static ReaderDoc getDocWithHtml(String focusedUri, LegislationAtomFeedParser.Entry entry, DocReference doc) throws ParserConfigurationException, SAXException, IOException {
        String tocUrl = entry.tableOfContents;
//        TODO //Set HTML segments
//        UKLegislationTocXmlParser p = parseToc(tocUrl);
//        HTMLLinkedSegment prev = null;
//        HTMLLinkedSegment focusedSegment = null;
//        for (UKLegislationTocXmlParser.ContentsSection s : p.sectionList) {
//            String contentUrl = UkUriObject.getHtmlSnippetUrl(UkUriObject.getRelativeIdFromRepresentation(s.documentURI));
//            HTMLLinkedSegment segment = new HTMLLinkedSegment(doc, contentUrl, null, prev);
//            if (prev != null) {
//                prev.setNext(segment);
//            }
//            prev = segment;
//            if (s.idUri.equals(focusedUri)) {
//                focusedSegment = segment;
//            }
//        }

        //Create final value
        return new HTMLDoc(
                doc,
                UkUriObject.getTocSnippetUrlFromTocRepresentation(tocUrl),
                entry.alternatives.get("application/xhtml+xml").url
//                ,                focusedSegment
        );
    }

    private static UKLegislationTocXmlParser parseToc(String tocUrl) throws ParserConfigurationException, SAXException, IOException {
        URL tocXmlUrl = new URL(tocUrl + "/data.xml");
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        UKLegislationTocXmlParser p = new UKLegislationTocXmlParser();
        xr.setContentHandler(p);
        InputStream stream = tocXmlUrl.openStream();
        xr.parse(new InputSource(stream));
        return p;
    }
}
