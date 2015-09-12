package org.leibnizcenter.rechtspraak;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A search request to rechtspraak.nl. Retrieves a list of judgments and their metadata.
 * <p/>
 * See https://www.rechtspraak.nl/Uitspraken-en-Registers/Uitspraken/Open-Data/Documents/Technische-documentatie-Open-Data-van-de-Rechtspraak.pdf for documentation on the Rechtspraak.nl web service
 * <p/>
 * Created by maarten on 31-7-15.
 */
public class SearchRequest {
    private final Request mRequest;
    SAXParserFactory factory = SAXParserFactory.newInstance();
    /**
     * Client for doing HTTP requests
     */
    private OkHttpClient sHttpClient = new OkHttpClient();

    private SearchRequest(HttpUrl url) {
        mRequest = new Request.Builder()
                .url(url)
                .build();
    }

    public enum ReturnType {META, DOC}

    public enum Sort {ASC, DESC}

    public enum Type {Conclusie, Uitspraak}

    public Request getRequest() {
        return mRequest;
    }

    public Response getResponse() throws IOException {
        return sHttpClient.newCall(mRequest).execute();
    }

    public List<JudgmentMetadata> execute() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println(getResponse().body().string().toString());
        InputStream is = getResponse().body().byteStream();
        SAXParser saxParser = factory.newSAXParser();
        ResultHandler handler = new ResultHandler();
        saxParser.parse(is, handler);
        return handler.judgments;
    }

    /**
     * See https://www.rechtspraak.nl/Uitspraken-en-Registers/Uitspraken/Open-Data/Documents/Technische-documentatie-Open-Data-van-de-Rechtspraak.pdf for documentation on the Rechtspraak.nl web service
     */
    public static class Builder {
        private final HttpUrl.Builder mBuilder;
        static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        static final DateFormat MODIFIED_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        public Builder() {
            mBuilder = new HttpUrl.Builder()
                    .scheme("http")
                    .host("data.rechtspraak.nl")
                    .addPathSegment("uitspraken")
                    .addPathSegment("zoeken");
        }

        /**
         * @param max Maximum number of results to return. Cannot be larger than 1000.
         */
        public Builder max(int max) {
            if (max > 1000 | max < 1) {
                throw new InvalidParameterException("Return limit needs to be a number between 1 and 1000 inclusive");
            }
            mBuilder.addQueryParameter("max", max + "");
            return this;
        }


        /**
         * @param type The type of content to return (only metadata or metadata+document)
         */
        public Builder returnType(ReturnType type) {
            mBuilder.addQueryParameter("return", type.toString());
            return this;
        }

        /**
         * @param from Offset in results, used for pagination.
         */
        public Builder from(int from) {
            if (from < 0) {
                from = 0;
            }
            mBuilder.addQueryParameter("from", from + "");
            return this;
        }

        /**
         * @param sort Sort results on modification date ascending or descending. Default is ascending (oldest first).
         */
        public Builder sort(Sort sort) {
            mBuilder.addQueryParameter("sort", sort.toString());
            return this;
        }

        /**
         * @param replaces Returns ECLI judgments that replace given LJN number
         */
        public Builder replaces(String replaces) {
            mBuilder.addQueryParameter("replaces", replaces);
            return this;
        }

        /**
         * @param date the particular date to return judgments taken on
         * @see #date(Date, Date)
         */
        public Builder date(Date date) {
            mBuilder.addQueryParameter("date", DATE_FORMAT.format(date));
            return this;
        }

        /**
         * Returns judgments taken between given two dates
         *
         * @param date1 search window start date, inclusive
         * @param date2 search window end date, inclusive
         */
        public Builder date(Date date1, Date date2) {
            mBuilder.addQueryParameter("date", DATE_FORMAT.format(date1));
            mBuilder.addQueryParameter("date", DATE_FORMAT.format(date2));
            return this;
        }

        /**
         * @param modified return judgment documents that where modified on this date or after
         * @see #modified(Date, Date)
         */
        public Builder modified(Date modified) {
            mBuilder.addQueryParameter("modified", MODIFIED_FORMAT.format(modified));
            return this;
        }

        /**
         * return judgment documents that where modified between given dates
         *
         * @param modified1 search window start date
         * @param modified2 search window end date
         * @see #modified(Date, Date)
         */
        public Builder modified(Date modified1, Date modified2) {
            mBuilder.addQueryParameter("modified", MODIFIED_FORMAT.format(modified1));
            mBuilder.addQueryParameter("modified", MODIFIED_FORMAT.format(modified2));
            return this;
        }

        /**
         * @param type The type of document to return ('uitspraak' or 'conclusie')
         */
        public Builder type(Type type) {
            mBuilder.addQueryParameter("type", type.toString());
            return this;
        }

        /**
         * @param subject Subject URI for a legal field
         */
        public Builder subject(String subject) {
            mBuilder.addQueryParameter("subject", subject);
            return this;
        }

        @Override
        public String toString() {
            return mBuilder.toString();
        }

        public SearchRequest build() {
            return new SearchRequest(mBuilder.build());
        }

        public HttpUrl.Builder getBuilder() {
            return mBuilder;
        }
    }

    public static class ResultHandler extends DefaultHandler {
        private static final Pattern SUBTITLE_PATTERN = Pattern.compile("([0-9]*)\\s*\\.?\\s*$");
        public int results = -1;
        public List<JudgmentMetadata> judgments = new ArrayList<JudgmentMetadata>();
        private String id;
        private String title;
        private String summary;
        private String updated;
        private String linkRel;
        private String linkType;
        private String linkHref;
        private Stack<String> elementStack = new Stack<String>();
        private String subtitle;


        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes) {
            this.elementStack.push(qName);

            switch (qName) {
                case "entry":
                case "id":
                case "title":
                case "summary":
                case "updated":
                    break;
                case "link":
                    linkRel = attributes.getValue("rel");
                    linkType = attributes.getValue("type");
                    linkHref = attributes.getValue("href");
                    break;
                default:
                    break;
            }
        }

        @SuppressWarnings("RedundantStringToString") // Because we want to ensure they're not null
        public void endElement(String uri, String localName,
                               String qName) {
            this.elementStack.pop();

            if ("entry".equals(qName)) {
                JudgmentMetadata judgment = new JudgmentMetadata(
                        id.toString(),
                        title.toString(),
                        summary.toString(),
                        Date.from(
                                javax.xml.bind.DatatypeConverter.parseDateTime(updated.toString())
                                        .toInstant()),
                        new JudgmentMetadata.Link(
                                linkRel.toString(), linkType.toString(), linkHref.toString()
                        )
                );
                id = null;
                title = null;
                summary = null;
                updated = null;
                linkRel = null;
                linkType = null;
                linkHref = null;
                judgments.add(judgment);
            }
            if ("subtitle".equals(qName)) {
                Matcher match = SUBTITLE_PATTERN.matcher(subtitle.toString());
                if (match.find()) {
                    String number = match.group(1);
                    results = Integer.parseInt(number);
                }
                subtitle = null;
            }
        }

        public void characters(char ch[], int start, int length) {
            String value = new String(ch, start, length).trim();
            if (value.length() == 0) return; // ignore white space

            if (currentElementParent() == "entry") {
                switch (currentElement()) {
                    case "id":
                        id = (id != null ? id : "") + value;
                        break;
                    case "title":
                        title = (title != null ? title : "") + value;
                        break;
                    case "summary":
                        summary = (summary != null ? summary : "") + value;
                        break;
                    case "updated":
                        updated = (updated != null ? updated : "") + value;
                        break;
                    default:
                        break;
                }
            }

            switch (currentElement()) {
                case "subtitle":
                    subtitle = (subtitle != null ? subtitle : "") + value;
                    break;
            }
        }

        private String currentElement() {
            return this.elementStack.peek();
        }

        private String currentElementParent() {
            if (this.elementStack.size() < 2) return null;
            return this.elementStack.get(this.elementStack.size() - 2);
        }

    }

    public static class JudgmentMetadata {
        public final String id;
        public final String title;
        public final String summary;
        public final Date updated;
        public final Link link;

        public JudgmentMetadata(String id, String title, String summary, Date updated, Link link) {
            this.id = id;
            this.title = title;
            this.summary = summary;
            this.updated = updated;
            this.link = link;
        }

        public static class Link {
            public final String rel;
            public final String type;
            public final String href;

            public Link(String rel, String type, String href) {
                this.rel = rel;
                this.type = type;
                this.href = href;
            }
        }
    }
}
