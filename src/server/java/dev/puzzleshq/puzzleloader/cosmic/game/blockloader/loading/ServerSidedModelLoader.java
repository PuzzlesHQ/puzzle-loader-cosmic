package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import dev.puzzleshq.puzzleloader.loader.util.ReflectionUtil;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.rendering.blockmodels.DummyBlockModel;
import finalforeach.cosmicreach.util.Identifier;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ServerSidedModelLoader implements ISidedModelLoader {

    private static Map<ModelKey, BlockModel> SERVER_MODEL_CACHE = new HashMap<>();

    @Override
    public boolean hasModel(String name, float[] rotation) {
        ModelKey key = new ModelKey(name, (int) rotation[0], (int) rotation[1], (int) rotation[2]);
        return SERVER_MODEL_CACHE.containsKey(key);
    }

    @Override
    public void loadModel(BlockModelGenerator modelGenerator, boolean coverAllRotations) {
        if (!coverAllRotations) {
            loadModel(modelGenerator.getName(), modelGenerator.toString(), State.DEFAULT_ROTATION);
            return;
        }

        float[] floats = new float[3];

        for (int x = 360; x > -1; x -= 90) {
            for (int y = 360; y > -1; y -= 90) {
                for (int z = 360; z > -1; z -= 90) {
                    floats[0] = x;
                    floats[1] = y;
                    floats[2] = z;
                    loadModel(modelGenerator.getName(), modelGenerator.toString(), floats);
                }
            }
        }
    }

    @Override
    public void loadModel(BlockModelGenerator modelGenerator, boolean coverAllRotations, boolean override) {
        if (!coverAllRotations) {
            loadModel(modelGenerator.getName(), modelGenerator.toString(), State.DEFAULT_ROTATION, override);
            return;
        }

        float[] floats = new float[3];

        for (int x = 360; x > -1; x -= 90) {
            for (int y = 360; y > -1; y -= 90) {
                for (int z = 360; z > -1; z -= 90) {
                    floats[0] = x;
                    floats[1] = y;
                    floats[2] = z;
                    loadModel(modelGenerator.getName(), modelGenerator.toString(), floats, override);
                }
            }
        }
    }

    @Override
    public BlockModel loadModel(BlockModelGenerator modelGenerator, float[] rotation) {
        return loadModel(modelGenerator.getName(), modelGenerator.toString(), rotation, false);
    }

    @Override
    public BlockModel loadModel(BlockModelGenerator modelGenerator, float[] rotation, boolean override) {
        return loadModel(modelGenerator.getName(), modelGenerator.toString(), rotation, override);
    }

    @Override
    public BlockModel loadModel(String modelName, float[] rotation, boolean override) {
        ModelKey key = new ModelKey(modelName, (int) rotation[0], (int) rotation[1], (int) rotation[2]);
        if (SERVER_MODEL_CACHE.containsKey(key) && !override) return SERVER_MODEL_CACHE.get(key);

        try {
            String jsonStr = GameAssetLoader.loadAsset(Identifier.of(modelName)).readString();
            DummyBlockModel model = (DummyBlockModel) ReflectionUtil.getMethod(DummyBlockModel.class, "fromJson", String.class, float[].class)
                    .invoke(null, jsonStr, rotation);

            SERVER_MODEL_CACHE.put(key, model);
            return model;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BlockModel loadModel(String modelName, String modelJson, float[] rotation, boolean override) {
        ModelKey key = new ModelKey(modelName, (int) rotation[0], (int) rotation[1], (int) rotation[2]);
        if (SERVER_MODEL_CACHE.containsKey(key) && !override) return SERVER_MODEL_CACHE.get(key);

        try {
            DummyBlockModel model = (DummyBlockModel) ReflectionUtil.getMethod(DummyBlockModel.class, "fromJson", String.class, float[].class)
                    .invoke(null, modelJson, rotation);

            SERVER_MODEL_CACHE.put(key, model);
            return model;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


}
