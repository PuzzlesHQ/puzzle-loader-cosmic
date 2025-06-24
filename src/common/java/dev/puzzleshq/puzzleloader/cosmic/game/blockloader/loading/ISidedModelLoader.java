package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;

import java.util.concurrent.atomic.AtomicReference;

public interface ISidedModelLoader {

    AtomicReference<ISidedModelLoader> CONTEXTUAL_INSTANCE = new AtomicReference<>();
    float[] DEFAULT_ROTATION = new float[]{0, 0, 0};

    static ISidedModelLoader getInstance() {
        return CONTEXTUAL_INSTANCE.get();
    }

    default boolean hasModel(String name) {
        return hasModel(name, State.DEFAULT_ROTATION);
    }

    boolean hasModel(String name, float[] rotation);

    void loadModel(BlockModelGenerator modelGenerator, boolean coverAllRotations);
    void loadModel(BlockModelGenerator modelGenerator, boolean coverAllRotations, boolean override);

    BlockModel loadModel(BlockModelGenerator modelGenerator, float[] rotation);
    BlockModel loadModel(BlockModelGenerator modelGenerator, float[] rotation, boolean override);

    default BlockModel loadModel(String modelName) {
        return loadModel(modelName, ISidedModelLoader.DEFAULT_ROTATION, false);
    }

    default BlockModel loadModel(String modelName, String modelJson) {
        return loadModel(modelName, modelJson, ISidedModelLoader.DEFAULT_ROTATION, false);
    }

    default BlockModel loadModel(String modelName, boolean override) {
        return loadModel(modelName, ISidedModelLoader.DEFAULT_ROTATION, override);
    }

    default BlockModel loadModel(String modelName, String modelJson, boolean override) {
        return loadModel(modelName, modelJson, ISidedModelLoader.DEFAULT_ROTATION, override);
    }

    default BlockModel loadModel(String modelName, float[] rotation) {
        return loadModel(modelName, rotation, false);
    }

    default BlockModel loadModel(String modelName, String modelJson, float[] rotation) {
        return loadModel(modelName, modelJson, rotation, false);
    }

    BlockModel loadModel(String modelName, float[] rotation, boolean override);
    BlockModel loadModel(String modelName, String modelJson, float[] rotation, boolean override);

    record ModelKey(String modelName, int x, int y, int z) {}

}
