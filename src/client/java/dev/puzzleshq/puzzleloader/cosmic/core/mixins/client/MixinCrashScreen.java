package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.puzzleshq.puzzleloader.cosmic.core.crash.BetterCrashScreen;
import finalforeach.cosmicreach.lwjgl3.CrashScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.awt.*;

@Mixin(CrashScreen.class)
public class MixinCrashScreen {

//    @Inject(method = "showCrash", at = @At(value = "INVOKE", target = "Ljavax/swing/JDialog;setVisible(Z)V", shift = At.Shift.BEFORE))
//    private static void test(long t, StringBuilder s, Throwable ex, CallbackInfo info) {
//    }

    @Redirect(method = "showCrash(JLjava/lang/StringBuilder;Ljava/lang/Throwable;)V", at = @At(value = "INVOKE", target = "Ljavax/swing/JDialog;setVisible(Z)V"))
    private static void setCrashScreenVisible(JDialog self, boolean b) {
//        System.err.println(self);
        BetterCrashScreen.fromDialog(self);
    }

}
