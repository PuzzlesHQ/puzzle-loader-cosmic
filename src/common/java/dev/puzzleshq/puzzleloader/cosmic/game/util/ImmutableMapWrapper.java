package dev.puzzleshq.puzzleloader.cosmic.game.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImmutableMapWrapper<K, V> implements Map<K, V> {

    private final Map<K, V> internalMap;

    public ImmutableMapWrapper(Map<K, V> internalMap) {
        this.internalMap = internalMap;
    }


    @Override
    public int size() {
        return this.internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.internalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.internalMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.internalMap.get(key);
    }

    @Override
    public @Nullable V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(@NonNull Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Set<K> keySet() {
        return this.internalMap.keySet();
    }

    @Override
    public @NonNull Collection<V> values() {
        return this.internalMap.values();
    }

    @Override
    public @NonNull Set<Entry<K, V>> entrySet() {
        return this.internalMap.entrySet();
    }
}
