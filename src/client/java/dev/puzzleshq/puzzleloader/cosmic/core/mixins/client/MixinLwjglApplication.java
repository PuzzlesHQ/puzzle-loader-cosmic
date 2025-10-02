package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.llamalad7.mixinextras.sugar.Local;
import dev.puzzleshq.puzzleloader.cosmic.game.PuzzleIcons;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Lwjgl3Application.class)
public class MixinLwjglApplication {

    @Redirect(require = 0, method = "createGlfwWindow", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Window;setIcon(J[Ljava/lang/String;Lcom/badlogic/gdx/Files$FileType;)V"))
    private static void nullifyGdxIcons(long i, String[] pixmap, Files.FileType windowHandle) {}

    @Inject(require = 0, method = "createGlfwWindow", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwMakeContextCurrent(J)V", shift = At.Shift.AFTER))
    private static void loadPuzzleIcons(Lwjgl3ApplicationConfiguration config, long sharedContextWindow, CallbackInfoReturnable<Long> cir, @Local(ordinal = 1) long windowHandle) {
        PuzzleIcons.window = windowHandle;
        PuzzleIcons.loadIcons();
        PuzzleIcons.setIcon();
    }



}
