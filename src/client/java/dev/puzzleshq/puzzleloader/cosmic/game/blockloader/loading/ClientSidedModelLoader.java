package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.client.BlockModelBuilder;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import dev.puzzleshq.puzzleloader.loader.util.ReflectionUtil;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ClientSidedModelLoader implements ISidedModelLoader {

    private static final Map<ModelKey, BlockModel> CLIENT_MODEL_CACHE = new HashMap<>();

    @Override
    public boolean hasModel(String name, float[] rotation) {
        ModelKey key = new ModelKey(name, (int) rotation[0], (int) rotation[1], (int) rotation[2]);
        return CLIENT_MODEL_CACHE.containsKey(key);
    }

    @Override
    public void loadModel(BlockModelGenerator modelGenerator, boolean coverAllRotations) {
        if (!coverAllRotations) {
            setCulling(loadModel(modelGenerator.getName(), modelGenerator.toString(), State.DEFAULT_ROTATION), modelGenerator);
            return;
        }

        float[] floats = new float[3];

        for (int x = 360; x > -1; x -= 90) {
            for (int y = 360; y > -1; y -= 90) {
                for (int z = 360; z > -1; z -= 90) {
                    floats[0] = x;
                    floats[1] = y;
                    floats[2] = z;
                    setCulling(loadModel(modelGenerator.getName(), modelGenerator.toString(), floats), modelGenerator);
                }
            }
        }
    }

    @Override
    public void loadModel(BlockModelGenerator modelGenerator, boolean coverAllRotations, boolean override) {
        if (!coverAllRotations) {
            setCulling(loadModel(modelGenerator.getName(), modelGenerator.toString(), State.DEFAULT_ROTATION, override), modelGenerator);
            return;
        }

        float[] floats = new float[3];

        for (int x = 360; x > -1; x -= 90) {
            for (int y = 360; y > -1; y -= 90) {
                for (int z = 360; z > -1; z -= 90) {
                    floats[0] = x;
                    floats[1] = y;
                    floats[2] = z;
                    setCulling(loadModel(modelGenerator.getName(), modelGenerator.toString(), floats, override), modelGenerator);
                }
            }
        }
    }

    private static BlockModel setCulling(BlockModel model, BlockModelGenerator modelGenerator) {
        // added to make sure the game does not crash if this gets removed, as we are unsure if this variable is used in the base game.
        try {
            Field field = ReflectionUtil.getField(model, "cullsSelf");
            field.set(model, modelGenerator.canCullSelf);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("remove \"cullsSelf\" attribute in BlockModelBuilder and BlockModelGenerator as it has been removed from the base game!");
        }
        return model;
    }

    @Override
    public BlockModel loadModel(BlockModelGenerator modelGenerator, float[] rotation) {
        return setCulling(loadModel(modelGenerator.getName(), modelGenerator.toString(), rotation, false), modelGenerator);
    }

    @Override
    public BlockModel loadModel(BlockModelGenerator modelGenerator, float[] rotation, boolean override) {
        return setCulling(loadModel(modelGenerator.getName(), modelGenerator.toString(), rotation, override), modelGenerator);
    }

    private BlockModel fromJson(String json, float[] rotation) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (BlockModel) ReflectionUtil.getMethod(BlockModelJson.class, "fromJson", String.class, int.class, int.class, int.class)
                .invoke(null, json, (int) rotation[0], (int) rotation[1], (int) rotation[2]);
    }

    @Override
    public BlockModel loadModel(String modelName, float[] rotation, boolean override) {
        ModelKey key = new ModelKey(modelName, (int) rotation[0], (int) rotation[1], (int) rotation[2]);
        if (CLIENT_MODEL_CACHE.containsKey(key) && !override) return CLIENT_MODEL_CACHE.get(key);

        try {
            String jsonStr = GameAssetLoader.loadAsset(Identifier.of(modelName)).readString();
            BlockModelJson model = (BlockModelJson) fromJson(jsonStr, rotation);

            CLIENT_MODEL_CACHE.put(key, model);
            return model;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BlockModel loadModel(String modelName, String modelJson, float[] rotation, boolean override) {
        ModelKey key = new ModelKey(modelName, (int) rotation[0], (int) rotation[1], (int) rotation[2]);
        if (CLIENT_MODEL_CACHE.containsKey(key) && !override) return CLIENT_MODEL_CACHE.get(key);

        try {
            BlockModelJson model = (BlockModelJson) ReflectionUtil.getMethod(BlockModelJson.class, "fromJson", String.class, int.class, int.class, int.class)
                    .invoke(null, modelJson, (int) rotation[0], (int) rotation[1], (int) rotation[2]);

            CLIENT_MODEL_CACHE.put(key, model);
            return model;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
