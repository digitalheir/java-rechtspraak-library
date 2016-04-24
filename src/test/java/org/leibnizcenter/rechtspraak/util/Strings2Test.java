package org.leibnizcenter.rechtspraak.util;

import com.google.common.collect.Sets;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by maarten on 31-3-16.
 */
public class Strings2Test {

    @Test
    public void lastCharIs() throws Exception {
        assertTrue(Strings2.lastCharIs("  \t\nABÄ Ä", 'Ä'));
        assertTrue(Strings2.lastCharIs("  \t\nABÄ", 'Ä'));
        assertFalse(Strings2.lastCharIs("  \t\nABÄ", 'A'));
        assertFalse(Strings2.lastCharIs("  \t\nABÄ\n\t", 'A'));
    }

    @Test
    public void firstCharIs() throws Exception {
        assertTrue(Strings2.firstCharIs("ÄBC", 'Ä'));
        assertFalse(Strings2.firstCharIs("  \t\nÄBC\n\t", 'Ä'));
        assertFalse(Strings2.lastCharIs("  \t\nÄBA\n\t", 'A'));
        assertFalse(Strings2.firstCharIs("ÄBC", 'A'));

        assertEquals(4, Strings2.firstNonWhitespaceCharIsAny("  \t\nÄBC\n\t", Sets.newHashSet('Ë', 'Ä')));
        assertEquals(-1, Strings2.firstNonWhitespaceCharIsAny("  \t\nÄBC\n\t", Sets.newHashSet('Ë', 'A')));
    }
}