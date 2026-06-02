package dev.puzzleshq.puzzleloader.cosmic.core.mixins;

import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.ModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.server.ServerModInit;
import finalforeach.cosmicreach.singletons.GameSingletons;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSingletons.class)
public class MixinGameSingletons {

    @Inject(method = "postCreate", at = @At(value = "HEAD"))
    private static void postCreate(CallbackInfo ci) {
        ModInit.invoke();
        ServerModInit.invoke();
    }

}
