package dev.puzzleshq.puzzleloader.cosmic.core.registries;

import dev.puzzleshq.puzzleloader.cosmic.core.registries.exception.AlreadyFrozenException;
import dev.puzzleshq.puzzleloader.cosmic.core.registries.exception.MissingEntryException;
import dev.puzzleshq.puzzleloader.cosmic.core.registries.exception.NotReadableException;
import dev.puzzleshq.puzzleloader.cosmic.core.registries.exception.NotWritableException;
import finalforeach.cosmicreach.util.Identifier;

import java.util.Set;

public interface IRegistry<T> extends Iterable<T> {
    Identifier getID();

    boolean contains(Identifier name) throws NotReadableException;
    T get(Identifier name) throws NotReadableException, MissingEntryException;
    T store(Identifier id, T value) throws NotWritableException;
    Set<Identifier> names() throws NotReadableException;

    void freeze() throws AlreadyFrozenException;

    boolean readable();
    boolean writable();
}