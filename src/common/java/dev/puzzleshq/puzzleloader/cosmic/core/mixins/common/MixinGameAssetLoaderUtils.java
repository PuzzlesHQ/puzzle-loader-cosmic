package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import dev.puzzleshq.mod.api.IModContainer;
import dev.puzzleshq.puzzleloader.loader.util.ModFinder;
import finalforeach.cosmicreach.util.GameAssetLoaderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Set;

@Mixin(GameAssetLoaderUtils.class)
public class MixinGameAssetLoaderUtils {

    @Unique
    private static final File[] puzzle_loader_cosmic$EMPTY_FILE_LIST = {};

    @Redirect(method = "getAllNamespaces", at = @At(value = "INVOKE", target = "Ljava/io/File;listFiles()[Ljava/io/File;"))
    private static File[] listModFiles(File instance) {
        File[] oldFiles = instance.listFiles();
        if (oldFiles == null)
            return puzzle_loader_cosmic$EMPTY_FILE_LIST;
        return oldFiles;
    }

    /**
     * Intercepts the return on getAllNamespaces to add Java-mod namespaces
     */
    @Inject(method = "getAllNamespaces", at = @At(value = "RETURN"), cancellable = true)
    private static void interceptReturn(CallbackInfoReturnable<Set<String>> cir) {
        Set<String> namespaces = cir.getReturnValue();
        for (IModContainer modContainer : ModFinder.getModsArray()) {
            namespaces.add(modContainer.getID());
        }
        cir.setReturnValue(namespaces);
    }

}
