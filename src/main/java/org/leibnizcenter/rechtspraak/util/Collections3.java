package org.leibnizcenter.rechtspraak.util;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Created by Maarten on 2016-04-03.
 */
public class Collections3 {

    public static <A, B> Stream<Pair<A, B>> zip(Stream<A> as, Stream<B> bs) {
        Iterator<A> i = as.iterator();
        return bs.filter(x -> i.hasNext()).map(b -> new Pair<>(i.next(), b));
    }
}
