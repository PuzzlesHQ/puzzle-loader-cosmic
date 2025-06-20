package dev.puzzleshq.puzzleloader.cosmic.core.registries.exception;

import dev.puzzleshq.puzzleloader.cosmic.core.registries.IRegistry;
import finalforeach.cosmicreach.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class MissingEntryException extends RuntimeException {
    public MissingEntryException(@NotNull IRegistry<?> registry, Identifier name) {
        super("Registry \"" + registry.getID() + "\" does not contain \"" + name + "\"");
    }
}