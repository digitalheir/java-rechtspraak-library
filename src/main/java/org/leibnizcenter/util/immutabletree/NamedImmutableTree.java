package org.leibnizcenter.util.immutabletree;

import com.google.common.collect.ImmutableList;

import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        return name + "{" +
                String.join(", ", children.stream().map(Object::toString).collect(Collectors.toList())) +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedImmutableTree that = (NamedImmutableTree) o;

        return children != null ? children.equals(that.children) : that.children == null && (name != null ? name.equals(that.name) : that.name == null);

    }

    @Override
    public int hashCode() {
        int result = children != null ? children.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }
}
