package org.leibnizcenter.rechtspraak;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by maarten on 9-3-16.
 */
public class SearchResult {
    private final List<SearchRequest.JudgmentMetadata> judgments;
    private final SearchRequest request;

    public SearchResult(SearchRequest request, List<SearchRequest.JudgmentMetadata> judgments) {
        this.request = request;
        this.judgments = judgments;
    }

    public SearchResultIterator iterator() {
        return new SearchResultIterator(this);
    }

    public List<SearchRequest.JudgmentMetadata> getJudgments() {
        return judgments;
    }

    public static class SearchResultIterator {
        private SearchResult current;
        private SearchResult next;

        public SearchResultIterator(SearchResult startingFrom) {
            current = startingFrom;
        }

        public boolean hasNext() throws ParserConfigurationException, SAXException, IOException {
            SearchRequest.Builder builder = current.request.getBuilder();

            // We know we've reached (past) the final page when it contains <= 0 results
            if (current.judgments.size() > 0) {
                next = getNextResult(builder);
                return next.judgments.size() > 0;
            }
            return false;
        }

        private SearchResult getNextResult(SearchRequest.Builder builder) throws IOException, ParserConfigurationException, SAXException {
            SearchRequest nextRequest = builder.from(builder.getOffset() + current.judgments.size()).build();
            return nextRequest.execute();
        }

        public SearchResult next() throws ParserConfigurationException, SAXException, IOException {
            SearchResult old = this.current;
            this.current = next;
            this.next = null;
            if (this.current == null) current = getNextResult(old.request.getBuilder());
            return current;
        }
    }
}
