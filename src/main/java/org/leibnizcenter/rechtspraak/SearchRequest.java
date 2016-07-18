package org.leibnizcenter.rechtspraak;

import com.google.common.base.Preconditions;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.HttpStatusException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
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
    final SAXParserFactory factory = SAXParserFactory.newInstance();
    private final Request mRequest;
    /**
     * Client for doing HTTP requests
     */
    private final OkHttpClient sHttpClient = new OkHttpClient();
    private final Builder builder;

    private SearchRequest(Builder b) {
        this.builder = b;
        mRequest = new Request.Builder()
                .url(b.urlBuilder.build())
                .build();
    }

    public Builder getBuilder() {
        return builder;
    }

    public Request getRequest() {
        return mRequest;
    }

    public Response getResponse() throws IOException {
        return sHttpClient.newCall(mRequest).execute();
    }

    public SearchResult execute() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println(getResponse().body().string().toString());
        Response response = getResponse();

        try {
            if (response.code() == 200) {
                //String str = response.body().string();

                SAXParser saxParser = factory.newSAXParser();
                ResultHandler handler = new ResultHandler();
                try {
                    saxParser.parse(new InputSource(response.body().byteStream()), handler);

                } catch (SAXException e) {
                    //System.err.println(str);
                    throw e;
                }

                return new SearchResult(this, handler.judgments);
            } else {
                throw new HttpStatusException("Code was not 200 but " + response.code(), response.code(), getRequest().url().toString());
            }
        } finally {
            response.body().close(); // Make sure it's closed
        }
    }

    public enum ReturnType {META, DOC}

    public enum Sort {ASC, DESC}

    public enum Type {Conclusie, Uitspraak}

    /**
     * See https://www.rechtspraak.nl/Uitspraken-en-Registers/Uitspraken/Open-Data/Documents/Technische-documentatie-Open-Data-van-de-Rechtspraak.pdf for documentation on the Rechtspraak.nl web service
     */
    public static class Builder {
        public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        public static final DateFormat MODIFIED_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        private final HttpUrl.Builder urlBuilder;
        private int offset = 0;

        public Builder() {
            urlBuilder = initBuilder();
        }

        /**
         * @param returnType Whether to return only documents with content,
         *                   or documents that have at least metadata. The later is the default,
         *                   but it is more likely that you are looking for the former.
         */
        public Builder(ReturnType returnType) {
            urlBuilder = initBuilder();
            urlBuilder.addQueryParameter("return", returnType.toString());
        }

        private HttpUrl.Builder initBuilder() {
            HttpUrl.Builder mBuilder;
            mBuilder = new HttpUrl.Builder()
                    .scheme("http")
                    .host("data.rechtspraak.nl")
                    .addPathSegment("uitspraken")
                    .addPathSegment("zoeken");
            return mBuilder;
        }

        /**
         * @param max Maximum number of results to return. Cannot be larger than 1000.
         */
        public Builder max(int max) {
            if (max > 1000 | max < 1) {
                throw new InvalidParameterException("Return limit needs to be a number between 1 and 1000 inclusive");
            }
            urlBuilder.addQueryParameter("max", max + "");
            return this;
        }


        /**
         * @param type The type of content to return (only metadata or metadata+document)
         */
        public Builder returnType(ReturnType type) {
            urlBuilder.addQueryParameter("return", type.toString());
            return this;
        }

        /**
         * @param from Offset in results, used for pagination.
         */
        public Builder from(int from) {
            if (from < 0) {
                from = 0;
            }
            this.offset = from;
            urlBuilder.addQueryParameter("from", from + "");
            return this;
        }

        /**
         * @param sort Sort results on modification date ascending or descending. Default is ascending (oldest first).
         */
        public Builder sort(Sort sort) {
            urlBuilder.addQueryParameter("sort", sort.toString());
            return this;
        }

        /**
         * @param replaces Returns ECLI judgments that replace given LJN number
         */
        public Builder replaces(String replaces) {
            urlBuilder.addQueryParameter("replaces", replaces);
            return this;
        }

        /**
         * @param date the particular date to return judgments taken on
         * @see #date(Date, Date)
         */
        public Builder date(Date date) {
            urlBuilder.addQueryParameter("date", DATE_FORMAT.format(date));
            return this;
        }

        /**
         * Returns judgments taken between given two dates
         *
         * @param date1 search window start date, inclusive
         * @param date2 search window end date, inclusive
         */
        public Builder date(Date date1, Date date2) {
            urlBuilder.addQueryParameter("date", DATE_FORMAT.format(date1));
            urlBuilder.addQueryParameter("date", DATE_FORMAT.format(date2));
            return this;
        }

        /**
         * @param modified return judgment documents that where modified on this date or after
         * @see #modified(Date, Date)
         */
        public Builder modified(Date modified) {
            urlBuilder.addQueryParameter("modified", MODIFIED_FORMAT.format(modified));
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
            urlBuilder.addQueryParameter("modified", MODIFIED_FORMAT.format(modified1));
            urlBuilder.addQueryParameter("modified", MODIFIED_FORMAT.format(modified2));
            return this;
        }

        /**
         * @param type The type of document to return ('uitspraak' or 'conclusie')
         */
        public Builder type(Type type) {
            urlBuilder.addQueryParameter("type", type.toString());
            return this;
        }

        /**
         * @param subject Subject URI for a legal field
         */
        public Builder subject(String subject) {
            urlBuilder.addQueryParameter("subject", subject);
            return this;
        }

        @Override
        public String toString() {
            return urlBuilder.toString();
        }

        public SearchRequest build() {
            return new SearchRequest(this);
        }

        public HttpUrl.Builder getBuilder() {
            return urlBuilder;
        }

        public int getFrom() {
            return offset;
        }

        public int getOffset() {
            return offset;
        }
    }

    public static class ResultHandler extends DefaultHandler {
        private static final Pattern SUBTITLE_PATTERN = Pattern.compile("([0-9]*)\\s*\\.?\\s*$");
        public final List<JudgmentMetadata> judgments = new ArrayList<>();
        private final Stack<String> elementStack = new Stack<>();
        public int results = -1;
        private String id;
        private String title;
        private String summary;
        private String updated;
        private String linkRel;
        private String linkType;
        private String linkHref;
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
                        id,
                        title,
                        summary,
                        Date.from(
                                javax.xml.bind.DatatypeConverter.parseDateTime(updated.toString())
                                        .toInstant()),
                        new JudgmentMetadata.Link(
                                linkRel, linkType, linkHref
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

            if (Objects.equals(currentElementParent(), "entry")) {
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
            Preconditions.checkArgument(id != null && id.length() > 0);
            Preconditions.checkNotNull(title, id + ": null pointer for title");
//            if (summary == null) {
//                System.out.println("Summary was null: " + summary);
//            }
//            if (updated == null) {
//                System.out.println("Updated was null: " + updated);
//            }
            Preconditions.checkNotNull(link, id + ": null pointer for link");

            this.id = id;
            this.title = title;
            this.summary = summary;
            this.updated = updated;
            this.link = link;
        }

        @Override
        public String toString() {
            return "JudgmentMetadata{" +
                    "\n  id='" + id + '\'' +
                    ", \n  title='" + title + '\'' +
                    ", \n  summary='" + summary + '\'' +
                    ", \n  updated=" + updated +
                    ", \n  link=" + link +
                    "\n}";
        }

        public static class Link {
            public final String rel;
            public final String type;
            public final String href;

            public Link(String rel, String type, String href) {
                Preconditions.checkNotNull(rel);
                Preconditions.checkNotNull(type);
                Preconditions.checkNotNull(href);

                this.rel = rel;
                this.type = type;
                this.href = href;
            }

            @Override
            public String toString() {
                return "Link{" +
                        "\n  rel='" + rel + '\'' +
                        ", \n  type='" + type + '\'' +
                        ", \n  href='" + href + '\'' +
                        "\n}";
            }
        }
    }
}
