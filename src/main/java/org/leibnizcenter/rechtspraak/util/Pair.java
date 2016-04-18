package org.leibnizcenter.rechtspraak.util;

import java.util.Map;

/**
 * Created by Maarten on 2016-04-03.
 */
public class Pair<K, V> implements Map.Entry<K, V> {
    private V v;
    private K k;

    public Pair(K key, V value) {
        v = value;
        k = key;
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    @Override
    public V setValue(V value) {
        V oldV = v;
        v = value;
        return oldV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        return v != null ? v.equals(pair.v) : pair.v == null && (k != null ? k.equals(pair.k) : pair.k == null);

    }

    @Override
    public int hashCode() {
        int result = v != null ? v.hashCode() : 0;
        result = 31 * result + (k != null ? k.hashCode() : 0);
        return result;
    }
}
