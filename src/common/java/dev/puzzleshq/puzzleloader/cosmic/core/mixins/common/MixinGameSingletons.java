package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.BlockLoader;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.ModInit;
import finalforeach.cosmicreach.singletons.GameSingletons;
import finalforeach.cosmicreach.util.logging.Logger;
import finalforeach.cosmicreach.util.logging.LoggerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(GameSingletons.class)
public class MixinGameSingletons {

    @Unique
    private static AtomicBoolean puzzle_loader_cosmic$firstCall = new AtomicBoolean(true);

    @Inject(method = "loadNextGameObject", at = @At("HEAD"))
    private static void loadNextGameObject(CallbackInfo ci) {
        if (puzzle_loader_cosmic$firstCall.get()) {
            puzzle_loader_cosmic$firstCall.set(false);

            BlockLoader.injectIntoQueue(GameSingletons.loadingQueue);
        }
    }

}
