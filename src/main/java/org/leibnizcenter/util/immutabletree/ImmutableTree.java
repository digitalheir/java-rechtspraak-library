package org.leibnizcenter.util.immutabletree;

import com.google.common.collect.ImmutableList;
import org.leibnizcenter.util.Collections3;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by Maarten on 2016-04-11.
 */
public interface ImmutableTree {
    default int depth(Depth kind) {
        List<ImmutableTree> children = getChildren();
        switch (kind) {
            case RightMostBranches:
                if (Collections3.isNullOrEmpty(children)) {
                    return 1;
                } else {
                    return 1 + children.get(children.size() - 1).depth(kind);
                }
            default:
                throw new InvalidParameterException();
        }
    }

    ImmutableTree replaceChild(int ix, ImmutableTree newChild);
    ImmutableList<ImmutableTree> getChildren();

    ImmutableTree addChild(ImmutableTree nodeToAdd);

    /**
     * NOTE: we should add more for completeness, but not currently needed
     */
    enum Depth {
        RightMostBranches
    }
}
