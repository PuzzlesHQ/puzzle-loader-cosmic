package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.PacketInterceptor;
import finalforeach.cosmicreach.networking.GamePacketRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GamePacketRegistry.class)
public class MixinGamePacketRegistry {

    @Inject(method = "registerPackets", at = @At("TAIL"))
    private static void registerPacketsMixin(CallbackInfo ci) {
        PacketInterceptor.callRegisterPacket();
        PacketInterceptor.init();
    }

}
