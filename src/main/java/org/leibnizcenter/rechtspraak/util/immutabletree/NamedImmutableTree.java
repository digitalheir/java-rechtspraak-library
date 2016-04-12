package org.leibnizcenter.rechtspraak.util.immutabletree;

import com.google.common.collect.ImmutableList;

/**
 * Safeguard against state changes
 * Created by maarten on 8-4-16.
 */
public class NamedImmutableTree implements ImmutableTree {
    public final ImmutableList<ImmutableTree> children;
    private final String name;

    public NamedImmutableTree(String name, ImmutableList<ImmutableTree> children) {
        this.children = children;
        this.name = name;
    }

    @Override
    public ImmutableTree addChild(ImmutableTree child) {
        ImmutableList<ImmutableTree> newChildren = new ImmutableList.Builder<ImmutableTree>()
                .addAll(children == null ? ImmutableList.of() : children)
                .add(child)
                .build();
        return new NamedImmutableTree(name, newChildren);
    }

    @Override
    public ImmutableList<ImmutableTree> getChildren() {
        return children;
    }

    @Override
    public ImmutableTree replaceChild(int ix, ImmutableTree newChild) {
        ImmutableList.Builder<ImmutableTree> b = new ImmutableList.Builder<>();
        for (int i = 0; i < children.size(); i++) b.add(i == ix ? newChild : children.get(i));
        return new NamedImmutableTree(name, b.build());
    }

    public String getName() {
        return name;
    }
}
