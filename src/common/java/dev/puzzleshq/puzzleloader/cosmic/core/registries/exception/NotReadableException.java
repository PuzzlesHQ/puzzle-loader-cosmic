package dev.puzzleshq.puzzleloader.cosmic.core.registries.exception;

import dev.puzzleshq.puzzleloader.cosmic.core.registries.IRegistry;
import org.jetbrains.annotations.NotNull;

public class NotReadableException extends RuntimeException {
    public NotReadableException(@NotNull IRegistry<?> registry) {
        super("Registry \"" + registry.getID() + "\" is not readable");
    }
}