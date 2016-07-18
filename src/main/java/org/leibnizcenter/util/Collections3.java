package org.leibnizcenter.util;

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

    public static <T> List<T> orEmptyDefault(List<T> list) {
        return (list == null) ? Collections.EMPTY_LIST : list;
    }

    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    public static <R> Set<R> orEmptyDefault(Set<R> set) {
        return set == null ? Collections.EMPTY_SET : set;
    }

    public static <R> R last(List<R> list) {
        return (list == null || list.size() <= 0) ? null : list.get(list.size() - 1);
    }

    public static <T> List<T> subList(List<T> l, int start) {
        List<T> newList = new ArrayList<>(l.size() - start);
        for (int i = start; i < l.size(); i++) newList.add(l.get(i));
        return newList;
    }

    public static <T> Set<T> add(Set<T> set, T toAdd) {
        if (set == null) set = new HashSet<>();
        set.add(toAdd);
        return set;
    }
}
