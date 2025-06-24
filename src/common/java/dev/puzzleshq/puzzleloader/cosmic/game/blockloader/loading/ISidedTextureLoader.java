package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import java.util.concurrent.atomic.AtomicReference;

public interface ISidedTextureLoader {

    AtomicReference<ISidedTextureLoader> CONTEXTUAL_INSTANCE = new AtomicReference<>();

    static ISidedTextureLoader getInstance() {
        return CONTEXTUAL_INSTANCE.get();
    }

}
