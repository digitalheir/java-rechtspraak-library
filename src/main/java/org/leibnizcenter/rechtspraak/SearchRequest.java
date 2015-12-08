package org.leibnizcenter.rechtspraak;

import com.google.common.base.Preconditions;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
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

    private SearchRequest(HttpUrl url) {
        mRequest = new Request.Builder()
                .url(url)
                .build();
    }

    public Request getRequest() {
        return mRequest;
    }

    public Response getResponse() throws IOException {
        return sHttpClient.newCall(mRequest).execute();
    }

    public List<JudgmentMetadata> execute() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println(getResponse().body().string().toString());
        Response resp = getResponse();

        if (resp.code() == 200) {

            String str = getResponse().body().string();

            SAXParser saxParser = factory.newSAXParser();
            ResultHandler handler = new ResultHandler();
            try {
                saxParser.parse(new InputSource(new StringReader(str)), handler);
            } catch (SAXException e) {
                System.err.println(str);
                throw e;
            }
            return handler.judgments;
        } else {
            throw new HttpStatusException("Code was not 200 but " + resp.code(), resp.code(), getRequest().url().toString());
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
        private final HttpUrl.Builder mBuilder;

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
