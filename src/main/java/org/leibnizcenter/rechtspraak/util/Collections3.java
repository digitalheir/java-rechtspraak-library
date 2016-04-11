package org.leibnizcenter.rechtspraak.util;

import org.w3c.dom.Node;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Maarten on 2016-04-03.
 */
public class Collections3 {

    public static <A, B> Stream<Pair<A, B>> zip(Stream<A> as, Stream<B> bs) {
        Iterator<A> i = as.iterator();
        return bs.filter(x -> i.hasNext()).map(b -> new Pair<>(i.next(), b));
    }

    public static <T> boolean isNullOrEmpty(Collection<T> coll) {
        return coll == null || coll.size() <= 0;
    }

    public static <K> Deque<K> upToAndIncluding(Deque<K> deque, K stopAfter) {
        ArrayDeque<K> returnObj = new ArrayDeque<>(deque.size());
        for (K el : deque) {
            returnObj.add(el);
            if (Objects.equals(el, stopAfter)) break;
        }
        return returnObj;
    }
}
