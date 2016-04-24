package org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags;

import com.google.common.collect.Sets;
import org.w3c.dom.Node;

import java.util.Set;

public class LeibnizTags {
    public static final String LEIBNIZ_NAMESPACE = "http://www.leibnizcenter.org/";

    public static final String TAG_TEXT = "text";
    public static final String TAG_POTENTIAL_NR = "potentialnr";
    public static final String TAG_TEXTGROUP = "textgroup";
    public static final String TAG_LIST_MARKING = "listmarking";
    public static final String TAG_QUOTE = "quote";

    public static final Set<String> all = Sets.newHashSet(
            TAG_TEXT,
            TAG_POTENTIAL_NR,
            TAG_TEXTGROUP,
            TAG_LIST_MARKING,
            TAG_QUOTE
    );

    public static final boolean hasLeibnizNameSpace(Node n) {
        String ns = n.getNamespaceURI();
        if (ns == null) return false;
        boolean y = ns.equals(LEIBNIZ_NAMESPACE);

        if (y && !all.contains(n.getNodeName()))
            throw new IllegalStateException("Unknown tag " + n.getNodeName() + " with Leibniz.org namespace");

        return y;
    }

    public static class Attr {
        public static final String manualAnnotation = "manualAnnotation";
    }
}