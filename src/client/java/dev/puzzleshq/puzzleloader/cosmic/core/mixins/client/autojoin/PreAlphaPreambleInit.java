package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client.autojoin;

import dev.puzzleshq.puzzleloader.cosmic.game.ClientPuzzle;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.gamestates.PrealphaPreamble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrealphaPreamble.class)
public class PreAlphaPreambleInit {

    @Inject(method = "create", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (ClientPuzzle.autoJoinWorldName != null) {
            GameState.switchToGameState(new MainMenu());
        }
    }

}
