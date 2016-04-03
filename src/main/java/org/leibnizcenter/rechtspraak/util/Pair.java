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
}
