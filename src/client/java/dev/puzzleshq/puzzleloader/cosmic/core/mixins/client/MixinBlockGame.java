package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client;

import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.client.ClientModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.client.ClientPostModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.client.ClientPreModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.ModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PostModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PreModInit;
import finalforeach.cosmicreach.BlockGame;
import finalforeach.cosmicreach.gamestates.GameState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGame.class)
public class MixinBlockGame {

    /**
     * {@link BlockGame#create()}
     * {@link finalforeach.cosmicreach.ClientSingletons#}
     */
    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/ClientSingletons;create()V", shift = At.Shift.AFTER))
    private static void init(CallbackInfo ci) {
        ModInit.invoke();
        ClientModInit.invoke();
    }

    /**
     * {@link BlockGame#create()}
     * {@link finalforeach.cosmicreach.gamestates.GameState#switchToGameState(GameState)}
     */
    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/gamestates/GameState;switchToGameState(Lfinalforeach/cosmicreach/gamestates/GameState;)V", shift = At.Shift.AFTER))
    private static void initPost(CallbackInfo ci) {
        PostModInit.invoke();
        ClientPostModInit.invoke();
    }

}
