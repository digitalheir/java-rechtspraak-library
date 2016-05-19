package org.leibnizcenter.rechtspraak.tagging.experiments;

import org.leibnizcenter.rechtspraak.tagging.Label;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by maarten on 16-5-16.
 */
class Label2Int {
    private Map<Label, Integer> m = new EnumMap<>(Label.class);

    public int get(Label l) {
        return m.getOrDefault(l, 0);
    }

    public void increment(Label label) {
        m.put(label, get(label) + 1);
    }
}
