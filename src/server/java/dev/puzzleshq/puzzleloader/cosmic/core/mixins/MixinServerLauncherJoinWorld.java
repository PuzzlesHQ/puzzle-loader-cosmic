package dev.puzzleshq.puzzleloader.cosmic.core.mixins;

import dev.puzzleshq.puzzleloader.cosmic.game.CommonPuzzle;
import finalforeach.cosmicreach.server.ServerLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLauncher.class)
public class MixinServerLauncherJoinWorld {

    @ModifyConstant(
            method = "main",
            constant = @Constant(stringValue = "server-world-1")
//            at = @At(
//                    value = "INVOKE_ASSIGN",
//                    target = "Lfinalforeach/cosmicreach/server/ServerLauncher;serverWorldName()Ljava/lang/String;"
//            )
    )
    private static String joinWorld(String old) {
        return CommonPuzzle.autoJoinWorldName == null ? "server-world-1" : CommonPuzzle.autoJoinWorldName;
    }

}
