package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client;

import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.client.ClientModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.ModInit;
import finalforeach.cosmicreach.singletons.GameSingletons;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSingletons.class)
public class MixinGameSingletons {

    @Inject(method = "postCreate", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blocks/Block;loadAllBlocks(Lcom/badlogic/gdx/utils/Queue;)V", shift = At.Shift.AFTER))
    private static void postCreate(CallbackInfo ci) {
        ModInit.invoke();
        ClientModInit.invoke();
    }

}
