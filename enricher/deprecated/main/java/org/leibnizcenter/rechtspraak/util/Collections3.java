package org.leibnizcenter.rechtspraak.util;

import java.util.List;
import java.util.function.Function;

/**
 * Created by maarten on 16-2-16.
 */
public class Collections3 {
    public static <F> int lastIndexWhere(List<F> list, Function<F, Boolean> f) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (f.apply(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static <F> int indexWhere(List<F> list, Function<F, Boolean> f) {
        for (int i = 0; i < list.size(); i++) {
            if (f.apply(list.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
