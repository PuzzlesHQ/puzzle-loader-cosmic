package dev.puzzleshq.puzzleloader.cosmic.core.registries.exception;

import dev.puzzleshq.puzzleloader.cosmic.core.registries.IRegistry;
import org.jetbrains.annotations.NotNull;

public class AlreadyFrozenException extends RuntimeException {
    public AlreadyFrozenException(@NotNull IRegistry<?> registry) {
        super("Registry \"" + registry.getID() + "\" is already frozen");
    }
}