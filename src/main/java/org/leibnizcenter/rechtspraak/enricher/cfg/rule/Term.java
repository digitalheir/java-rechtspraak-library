package org.leibnizcenter.rechtspraak.enricher.cfg.rule;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.UnmodifiableListIterator;
import org.jetbrains.annotations.NotNull;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.Type;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represent an immutable sequence of types
 * Created by maarten on 21-4-16.
 */
@SuppressWarnings({"unused", "deprecation", "EqualsWhichDoesntCheckParameterClass"})
public class Term implements List<Type> {
    private ImmutableList<Type> types;

    public Term(List<Type> types) {
        this.types = ImmutableList.copyOf(types);
    }

    public Term(@NotNull Type... types) {
        this.types = ImmutableList.copyOf(types);
    }

    @NotNull
    @Override
    public UnmodifiableIterator<Type> iterator() {
        return types.iterator();
    }

    @NotNull
    @Override
    public UnmodifiableListIterator<Type> listIterator() {
        return types.listIterator();
    }

    @NotNull
    @Override
    public UnmodifiableListIterator<Type> listIterator(int index) {
        return types.listIterator(index);
    }

    @Override
    public int indexOf(Object object) {
        return types.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return types.lastIndexOf(object);
    }

    @Override
    public boolean contains(Object object) {
        return types.contains(object);
    }

    @NotNull
    @Override
    public ImmutableList<Type> subList(int fromIndex, int toIndex) {
        return types.subList(fromIndex, toIndex);
    }

    @Override
    @Deprecated
    public boolean addAll(int index, @NotNull Collection<? extends Type> newElements) {
        return types.addAll(index, newElements);
    }

    @Override
    @Deprecated
    public Type set(int index, Type element) {
        return types.set(index, element);
    }

    @Override
    @Deprecated
    public void add(int index, Type element) {
        types.add(index, element);
    }

    public ImmutableList<Type> asList() {
        return types.asList();
    }

    public ImmutableList<Type> reverse() {
        return types.reverse();
    }

    @Override
    public boolean equals(Object obj) {
        return types.equals(obj);
    }

    @Override
    public int hashCode() {
        return types.hashCode();
    }


    @Override
    @Deprecated
    public boolean add(Type type) {
        return types.add(type);
    }

    @Override
    @Deprecated
    public boolean addAll(@NotNull Collection<? extends Type> newElements) {
        return types.addAll(newElements);
    }

    @Override
    public int size() {
        return types.size();
    }

    @Override
    public boolean isEmpty() {
        return types.isEmpty();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return types.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return types.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return types.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return types.containsAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return types.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return types.retainAll(c);
    }

    @Override
    public void clear() {
        types.clear();
    }

    @Override
    public String toString(){
        return String.join("", stream().map(Object::toString).collect(Collectors.toList()));
    }

    @Override
    public boolean removeIf(Predicate<? super Type> filter) {
        return types.removeIf(filter);
    }

    @Override
    public Spliterator<Type> spliterator() {
        return types.spliterator();
    }

    @Override
    public Stream<Type> stream() {
        return types.stream();
    }

    @Override
    public Stream<Type> parallelStream() {
        return types.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super Type> action) {
        types.forEach(action);
    }

    @Override
    public void replaceAll(UnaryOperator<Type> operator) {
        types.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super Type> c) {
        types.sort(c);
    }

    @Override
    public Type get(int index) {
        return types.get(index);
    }

    @Override
    public Type remove(int index) {
        return types.remove(index);
    }

    public boolean isTerminal() {
        return !stream().anyMatch(r -> !r.isTerminal());
    }
}
