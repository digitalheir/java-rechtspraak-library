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
    private SearchResult next;

    public SearchResult(SearchRequest request, List<SearchRequest.JudgmentMetadata> judgments) {
        this.request = request;
        this.judgments = judgments;
    }

    public List<SearchRequest.JudgmentMetadata> getJudgments() {
        return judgments;
    }

    public boolean hasNext() throws ParserConfigurationException, SAXException, IOException {
        SearchRequest.Builder builder = request.getBuilder();

        // We know we've reached (past) the final page when it contains <= 0 results
        if (judgments.size() > 0) {
            next = getNextResult(builder);
            return next.judgments.size() > 0;
        }
        return false;
    }

    private SearchResult getNextResult(SearchRequest.Builder builder) throws IOException, ParserConfigurationException, SAXException {
        SearchRequest nextRequest = builder.from(builder.getOffset() + this.judgments.size()).build();
        return nextRequest.execute();
    }

    public SearchResult next() throws ParserConfigurationException, SAXException, IOException {
        SearchResult old = this;
        SearchResult returnNext = next;
        if (returnNext == null) returnNext = getNextResult(old.request.getBuilder());
        return returnNext;
    }
}
