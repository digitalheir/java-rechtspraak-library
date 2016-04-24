package org.leibnizcenter.rechtspraak.util.immutabletree;

import com.google.common.collect.ImmutableList;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.TaggedToken;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

/**
 * Created by Maarten on 2016-04-11.
 */
public class LabeledTokenNode implements ImmutableTree {
    public final TaggedToken<TokenTreeLeaf, Label> token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabeledTokenNode that = (LabeledTokenNode) o;

        return token != null ? token.equals(that.token) : that.token == null;

    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }

    public LabeledTokenNode(TaggedToken<TokenTreeLeaf, Label> token) {
        this.token = token;
    }

    @Override
    public int depth(Depth kind) {
        return 1; // Just this node
    }

    @Override
    public ImmutableTree replaceChild(int ix, ImmutableTree newChild) {
        return this; // No children
    }

    @Override
    public ImmutableList<ImmutableTree> getChildren() {
        return null; // No children
    }

    @Override
    public ImmutableTree addChild(ImmutableTree nodeToAdd) {
        throw new IllegalStateException("Can't add children to " + LabeledTokenNode.class.getSimpleName()); //
    }

    @Override
    public String toString() {
        return "[" + token.toString() + "]";
    }
}
