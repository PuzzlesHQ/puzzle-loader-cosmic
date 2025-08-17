package dev.puzzleshq.puzzleloader.cosmic.core.registries;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DeferredRegister<T> extends Register<T> {

    public DeferredRegister(IRegistry<T> registry, String namespace) {
        super(registry, namespace);
    }

    final List<Pair<Identifier, T>> itemsToRegister = new ArrayList<>();

    public Identifier register(String name, T obj) {
        Identifier id = Identifier.of(namespace, name);
        itemsToRegister.add(new ImmutablePair<>(id, obj));
        return id;
    }

    public void registerAll() {
        for (Pair<Identifier, T> identifierTPair : itemsToRegister) {
            registry.store(identifierTPair.getKey(), identifierTPair.getValue());
        }
    }



}
