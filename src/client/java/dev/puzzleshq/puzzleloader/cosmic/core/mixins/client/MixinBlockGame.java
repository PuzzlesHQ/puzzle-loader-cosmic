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
//        PreModInit.invoke();
//        ClientPreModInit.invoke();
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

//        try {
//            Pixmap m = (Pixmap) ReflectionUtil.getField(ChunkShader.class, "allBlocksPix").get(null);
//            byte[] bytes = new byte[m.getWidth() * m.getHeight() * 4];
//            m.getPixels().get(bytes);
//            PixmapIO.writePNG(new FileHandle("F.png"), m);
//        } catch (IllegalAccessException | NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
    }

}
