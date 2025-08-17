package dev.puzzleshq.puzzleloader.cosmic.core.registries;

import finalforeach.cosmicreach.util.Identifier;

public class Register<T> {

    final IRegistry<T> registry;
    final String namespace;

    public Register(IRegistry<T> registry, String namespace) {
        this.registry = registry;
        this.namespace = namespace;
    }

    public Identifier register(String name, T obj) {
        Identifier id = Identifier.of(namespace, name);
        registry.store(id, obj);
        return id;
    }

    public IRegistry<T> getRegistry() {
        return registry;
    }

    public String getNamespace() {
        return namespace;
    }
}
