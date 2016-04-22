package org.leibnizcenter.rechtspraak.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;
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

    public static <T> List<T> thisOrEmpty(List<T> list) {
        return (list == null) ? ImmutableList.of() : list;
    }

    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    public static <R> Set<R> thisOrEmpty(Set<R> set) {
        return set == null ? ImmutableSet.of() : set;
    }

    public static <R> R last(List<R> list) {
        return (list == null || list.size() <= 0) ? null : list.get(list.size() - 1);
    }

    public static <T> List<T> subList(List<T> l, int start) {
        List<T> newList = new ArrayList<>(l.size() - start);
        for (int i = start; i < l.size(); i++) newList.add(l.get(i));
        return newList;
    }
}
