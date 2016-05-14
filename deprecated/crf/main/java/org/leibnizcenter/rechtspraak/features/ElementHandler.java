package org.leibnizcenter.rechtspraak.tagging.crf.features;

import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;

import java.util.List;

/**
 * Created by Maarten on 2016-03-21.
 */
@FunctionalInterface
public interface ElementHandler {
    double value(List<RechtspraakElement> list, int position);
}
