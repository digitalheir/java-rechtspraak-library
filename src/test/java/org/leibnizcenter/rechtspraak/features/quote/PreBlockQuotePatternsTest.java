package org.leibnizcenter.rechtspraak.features.quote;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by maarten on 24-3-16.
 */
public class PreBlockQuotePatternsTest {
    @Test
    public void main() {
        assertFalse(PreBlockQuotePattrns.Normalized.matchesAny(
                "volgens dit artikellid dienen voor een eenpersoonshuishouden in een seniorenwoningappartement voor de aandachtsgebieden waarin eiseres gecompenseerd dient te worden de volgende bedragen te gelden"
        ));
    }
}