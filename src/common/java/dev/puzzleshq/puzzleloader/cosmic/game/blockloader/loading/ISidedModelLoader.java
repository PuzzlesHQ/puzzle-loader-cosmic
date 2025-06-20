package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;

import java.util.concurrent.atomic.AtomicReference;

public interface ISidedModelLoader {

    AtomicReference<ISidedModelLoader> CONTEXTUAL_INSTANCE = new AtomicReference<>();

    static ISidedModelLoader getInstance() {
        return CONTEXTUAL_INSTANCE.get();
    }

    void loadModel(BlockModelGenerator modelGenerator);
}
