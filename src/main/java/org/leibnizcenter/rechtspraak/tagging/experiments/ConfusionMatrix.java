package org.leibnizcenter.rechtspraak.tagging.experiments;

import org.leibnizcenter.rechtspraak.tagging.Label;

import java.io.Serializable;
import java.util.EnumMap;

/**
 * Created by maarten on 15-5-16.
 */
public class ConfusionMatrix extends EnumMap<Label, Label2Int> implements Serializable {
    public ConfusionMatrix() {
        super(Label.class);
    }

    public ConfusionMatrix addObservation(Label actual, Label predicted) {
        Label2Int m = get(actual);
        m.increment(predicted);
        put(actual, m);
        return this;
    }

    public int getCounts(Label actual, Label predicted) {
        return get(actual).get(predicted);
    }

    public int getTruePositives(Label label) {
        return get(label).get(label);
    }

    @Override
    public Label2Int get(Object l) {
        if (containsKey(l)) {
            return super.get(l);
        } else {
            Label2Int res = new Label2Int();
            put((Label) l, res);
            return res;
        }
    }

    public int getTrueNegatives(Label label) {
        int total = 0;
        for (Label l1 : Label.values())
            for (Label l2 : Label.values())
                if (!l1.equals(label) && !l2.equals(label)) total += get(l1).get(l2);
        return total;
    }

    public int getFalseNegatives(Label label) {
        int total = 0;
        for (Label l1 : Label.values())
            if (!l1.equals(label)) total += get(label).get(l1);
        return total;
    }

    public int getFalsePositives(Label label) {
        int total = 0;
        for (Label l : Label.values()) {
            if (!l.equals(label)) total += get(l).get(label);
        }
        return total;
    }

}
