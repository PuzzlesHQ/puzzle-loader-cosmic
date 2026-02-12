package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockModelJson.class)
public class MixinBlockJsonModel {

    @Unique
    private static final float[] puzzle_loader_cosmic$xyz = new float[3];

    /**
     * @author Mr_Zombii
     * @reason Re-route Block Model Loading
     */
    @Overwrite
    public static BlockModelJson getInstance(String modelName, int rotX, int rotY, int rotZ) {
        puzzle_loader_cosmic$xyz[0] = rotX;
        puzzle_loader_cosmic$xyz[1] = rotY;
        puzzle_loader_cosmic$xyz[2] = rotZ;
        return (BlockModelJson) ISidedModelLoader.getInstance().loadModel(modelName, puzzle_loader_cosmic$xyz);
    }

    /**
     * @author Mr_Zombii
     * @reason Re-route Block Model Loading
     */
    @Overwrite
    public static BlockModelJson getInstance(String modelName, float[] rotation) {
        return (BlockModelJson) ISidedModelLoader.getInstance().loadModel(modelName, rotation);
    }

    /**
     * @author Mr_Zombii
     * @reason Re-route Block Model Loading
     */
    @Overwrite
    public static BlockModelJson getInstanceFromJsonStr(String modelName, String modelJson, float[] rotation) {
        return (BlockModelJson) ISidedModelLoader.getInstance().loadModel(modelName, modelJson, rotation);
    }


}
