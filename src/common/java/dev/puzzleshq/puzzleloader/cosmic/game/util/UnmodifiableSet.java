package dev.puzzleshq.puzzleloader.cosmic.game.util;

import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class UnmodifiableSet<E> implements Set<E> {

    private final Set<E> innerSet;

    public UnmodifiableSet(Set<E> innerSet) {
        this.innerSet = innerSet;
    }

    @Override
    public int size() {
        return innerSet.size();
    }

    @Override
    public boolean isEmpty() {
        return innerSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return innerSet.contains(o);
    }

    @Override
    public @NonNull Iterator<E> iterator() {
        return innerSet.iterator();
    }

    @Override
    public @NonNull Object[] toArray() {
        return innerSet.toArray();
    }

    @Override
    public @NonNull <T> T[] toArray(@NonNull T[] a) {
        return innerSet.toArray(a);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return innerSet.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
