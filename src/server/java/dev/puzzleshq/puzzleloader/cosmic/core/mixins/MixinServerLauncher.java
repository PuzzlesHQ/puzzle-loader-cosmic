package dev.puzzleshq.puzzleloader.cosmic.core.mixins;

import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.server.ServerPreModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PreModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.server.ServerModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.ModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.server.ServerPostModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PostModInit;
import finalforeach.cosmicreach.server.ServerLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;

@Mixin(ServerLauncher.class)
public class MixinServerLauncher {

    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Ljava/security/CodeSource;getLocation()Ljava/net/URL;"))
    private static URL getLocation(CodeSource instance) {
        try {
            /* added the non-existant /e/ to the end because it automatically gets the parent file*/
            return new File("./e/").toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/server/ServerSingletons;create()V", shift = At.Shift.BEFORE))
    private static void initPre(String[] args, CallbackInfo ci) {
        PreModInit.invoke();
        ServerPreModInit.invoke();
    }

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/server/ServerSingletons;create()V", shift = At.Shift.AFTER))
    private static void init(String[] args, CallbackInfo ci) {
        ModInit.invoke();
        ServerModInit.invoke();
    }

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/netty/NettyServer;run()V", shift = At.Shift.BEFORE))
    private static void initPost(String[] args, CallbackInfo ci) {
        PostModInit.invoke();
        ServerPostModInit.invoke();
    }

}
