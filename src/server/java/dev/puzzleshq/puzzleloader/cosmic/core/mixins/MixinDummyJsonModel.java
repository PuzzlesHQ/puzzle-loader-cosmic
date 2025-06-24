package dev.puzzleshq.puzzleloader.cosmic.core.mixins;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import finalforeach.cosmicreach.rendering.blockmodels.DummyBlockModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DummyBlockModel.class)
public class MixinDummyJsonModel {

    /**
     * @author Mr_Zombii
     * @reason Re-route Block Model Loading
     */
    @Overwrite
    public static DummyBlockModel getInstance(String modelName, float[] rotation) {
        return (DummyBlockModel) ISidedModelLoader.getInstance().loadModel(modelName, rotation);
    }

    /**
     * @author Mr_Zombii
     * @reason Re-route Block Model Loading
     */
    @Overwrite
    public static DummyBlockModel getInstanceFromJsonStr(String modelName, String modelJson, float[] rotation) {
        return (DummyBlockModel) ISidedModelLoader.getInstance().loadModel(modelName, modelJson, rotation);
    }


}
