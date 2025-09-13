package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client;

import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.client.ClientPreModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PreModInit;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Lwjgl3Launcher.class)
public class MixinLwjgl3Launcher {

    @Inject(method = "main", at = @At(value = "FIELD", target = "Lcom/badlogic/gdx/Gdx;files:Lcom/badlogic/gdx/Files;", opcode = Opcodes.PUTSTATIC))
    private static void afterArgParsing(String[] args, CallbackInfo ci) {
        PreModInit.invoke();
        ClientPreModInit.invoke();
    }

}
