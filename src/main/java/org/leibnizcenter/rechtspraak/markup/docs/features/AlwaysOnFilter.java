package org.leibnizcenter.rechtspraak.markup.docs.features;

import org.crf.crf.filters.Filter;

/**
 * Created by maarten on 5-3-16.
 */
public class AlwaysOnFilter extends Filter<String, String> {
    @Override
    public int hashCode() {
        return 69;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass();
    }
}
