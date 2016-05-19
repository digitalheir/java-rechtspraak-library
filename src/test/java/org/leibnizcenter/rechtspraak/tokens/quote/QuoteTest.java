package org.leibnizcenter.rechtspraak.tokens.quote;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by maarten on 1-4-16.
 */
public class QuoteTest {

    @Test
    public void endsWithDoubleQuote() throws Exception {
        assertTrue(Quote.endsWithDoubleQuote("End \" \n\t "));
        assertTrue(Quote.endsWithDoubleQuote("End \""));
        assertFalse(Quote.endsWithDoubleQuote("End '  "));
        assertFalse(Quote.endsWithDoubleQuote("End `  "));
    }

    @Test
    public void endsWithSingleQuote() throws Exception {
        assertFalse(Quote.endsWithSingleQuote("End \" \n\t "));
        assertFalse(Quote.endsWithSingleQuote("End \""));
        assertTrue(Quote.endsWithSingleQuote("End `  "));
        assertTrue(Quote.endsWithSingleQuote("End '  "));
        assertTrue(Quote.endsWithSingleQuote("End `"));
    }

    @Test
    public void endsWithQuote() throws Exception {
        assertTrue(Quote.endsWithQuote("End `"));
        assertTrue(Quote.endsWithQuote("End '  "));
        assertTrue(Quote.endsWithQuote("End \" \n\t "));
        assertTrue(Quote.endsWithQuote("End \""));
    }
}