package org.leibnizcenter.rechtspraak.util;

import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.markup.docs.Label;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by maarten on 4-3-16.
 */
public class Labels {
    public static final Set<Label> set = EnumSet.allOf(Label.class);
    public static final Set<Label> withoutNull = Sets.immutableEnumSet(set.stream()
            .filter(l -> !l.equals(Label.NULL))
            .collect(Collectors.toSet()));
}
