package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import com.badlogic.gdx.utils.ObjectSet;
import dev.puzzleshq.mod.api.IModContainer;
import dev.puzzleshq.puzzleloader.loader.mod.ModContainer;
import dev.puzzleshq.puzzleloader.loader.util.ModFinder;
import finalforeach.cosmicreach.GameAssetLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameAssetLoader.class)
public class MixinGameAssetLoader {

    /**
     * Intercepts the return on getAllNamespaces to add Java-mod namespaces
     */
    @Inject(method = "getAllNamespaces", at = @At("RETURN"), cancellable = true)
    private static void interceptReturn(CallbackInfoReturnable<ObjectSet<String>> cir) {
        ObjectSet<String> namespaces = cir.getReturnValue();
        for (IModContainer modContainer : ModFinder.getModsArray()) {
            namespaces.add(modContainer.getID());
        }
        cir.setReturnValue(namespaces);
    }

}
