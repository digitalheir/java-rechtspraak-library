package org.leibnizcenter.rechtspraak.tokens.text;

import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.w3c.dom.Element;

import java.util.Set;

/**
 * Created by maarten on 1-4-16.
 */
public class IgnoreElement extends RechtspraakElement {
    private static final Set<String> dontTokenize = Sets.newHashSet(
            "footnote",
            "footnote-ref",
            "listitem",
            "link",
            "bridgehead",
            "list",
            "informaltable",
            "table",
            "mediaobject"
    );
    private static final Set<String> passThrough = Sets.newHashSet(
            "section",
            "uitspraak\\.info",
            "conclusie\\.info",
            "title"
    );
    private static final Set<String> set = Sets.union(dontTokenize, passThrough);


    public IgnoreElement(Element e) {
        super(e);
    }

    public static boolean passThrough(String nodeName) {
        return set.contains(nodeName)
                || nodeName.endsWith("list")
                || nodeName.endsWith("table")
                || nodeName.endsWith("object")
                || nodeName.endsWith(".info");
    }

    public static boolean dontTokenize(String nodeName) {
        return dontTokenize.contains(nodeName)
                || nodeName.endsWith("list")
                || nodeName.endsWith("table")
                || nodeName.endsWith("object");
    }

    public static boolean dontTryToLabel(String nodeName) {
        return nodeName.endsWith("list")
                || nodeName.endsWith("table")
                || nodeName.endsWith("table")
                || nodeName.startsWith("footnote")
                || nodeName.endsWith("listitem")
                || nodeName.endsWith("link")
                || nodeName.endsWith("list")
                || nodeName.endsWith("informaltable")
                || nodeName.endsWith("table")
                || nodeName.endsWith("mediaobject")
                || nodeName.endsWith("object");
    }
}
