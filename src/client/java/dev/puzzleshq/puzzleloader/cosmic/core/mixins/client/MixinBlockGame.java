package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client;

import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.*;
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
    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/rendering/shaders/GameShader;initShaders()V", shift = At.Shift.BEFORE))
    private void initPre(CallbackInfo ci) {
        PreModInit.invoke();
        ClientPreModInit.invoke();
    }

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
