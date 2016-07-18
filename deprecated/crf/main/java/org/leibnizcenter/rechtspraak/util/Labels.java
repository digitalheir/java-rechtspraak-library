package org.leibnizcenter.util;

import org.leibnizcenter.rechtspraak.markup.docs.Label;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by maarten on 4-3-16.
 */
public class Labels {
    public static final Set<Label> set = EnumSet.allOf(Label.class);
    public static final Set<Label> withoutNull = set;
}
