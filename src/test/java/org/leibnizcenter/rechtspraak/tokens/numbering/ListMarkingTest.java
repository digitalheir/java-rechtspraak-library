package org.leibnizcenter.rechtspraak.tokens.numbering;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by maarten on 1-4-16.
 */
public class ListMarkingTest {

    @Test
    public void startsWithListMarking() throws Exception {
        assertTrue(ListMarking.startsWithListMarking("− Hello"));
        assertTrue(ListMarking.startsWithListMarking("  \t\n - Hello"));
        assertFalse(ListMarking.startsWithListMarking("  \t\n g -"));
    }
}