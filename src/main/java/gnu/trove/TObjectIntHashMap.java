package gnu.trove;

import gnu.trove.map.TObjectIntMap;

/**
 * Created by Maarten on 30/08/2016.
 */
public class TObjectIntHashMap<T> extends gnu.trove.map.hash.TObjectIntHashMap<T> {
    public TObjectIntHashMap() {
    }

    public TObjectIntHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TObjectIntHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TObjectIntHashMap(int initialCapacity, float loadFactor, int noEntryValue) {
        super(initialCapacity, loadFactor, noEntryValue);
    }

    public TObjectIntHashMap(TObjectIntMap<? extends T> map) {
        super(map);
    }
}
