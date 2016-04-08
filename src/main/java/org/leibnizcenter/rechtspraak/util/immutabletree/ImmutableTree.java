package org.leibnizcenter.rechtspraak.util.immutabletree;

import com.google.common.collect.ImmutableList;
import org.leibnizcenter.rechtspraak.enricher.Depth;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTreeVertex;
import org.leibnizcenter.rechtspraak.util.Collections3;

/**
 * Safeguard against state changes
 * <p>
 * Created by maarten on 8-4-16.
 */
public class ImmutableTree implements TokenTreeVertex {
    public final ImmutableList<ImmutableTree> children;
    public final TokenTreeLeaf token;

    public ImmutableTree(TokenTreeLeaf token, ImmutableList<ImmutableTree> children) {
        this.token = token;this.children=children;
    }

    public ImmutableTree addChild(ImmutableTree child) {
        ImmutableList<ImmutableTree> newChildren = new ImmutableList.Builder<ImmutableTree>()
                .addAll(children)
                .add(child)
                .build();

        ImmutableTree newTree = new ImmutableTree(token, newChildren);

        return newTree;
    }

    public int depth(Depth kind) {
        switch (kind) {
            case RightMostBranches:
                if (Collections3.isNullOrEmpty(children)) {
                    return 1;
                } else {
                    return 1 + children.get(children.size() - 1).depth(kind);
                }
            default:
                throw new IllegalStateException();
        }
    }

    public ImmutableTree replaceChild(int ix, ImmutableTree newChild) {
        ImmutableList.Builder<ImmutableTree> b = new ImmutableList.Builder<ImmutableTree>();
        for (int i = 0; i < children.size(); i++) {
            b.add(i==ix?newChild:children.get(i));
        }
        return new ImmutableTree(this.token, b.build());
    }
}
