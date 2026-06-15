package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventPacketSendIntercept;
import finalforeach.cosmicreach.networking.GamePacket;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GamePacket.class)
public class MixinGamePacket {

    @Inject(method = "flushToContext", at = @At("TAIL"))
    private void receivePacket(ChannelHandlerContext ctx, CallbackInfo ci) {
        EventPacketSendIntercept sendIntercept = new EventPacketSendIntercept();
        sendIntercept.setPacket((GamePacket)(Object)this);
        GameRegistries.NETWORK_EVENT_BUS.post(sendIntercept);
    }

}
