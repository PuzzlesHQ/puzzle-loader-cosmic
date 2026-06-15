package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventPacketReceiveIntercept;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventPacketSendIntercept;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.NetworkHandler;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.netty.NettyPacketHandler;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NettyPacketHandler.class)
public class MixinPacketHandler {

    @Inject(
            method = "channelRead",
            at = @At(
                    value = "INVOKE",
                    target = "Lfinalforeach/cosmicreach/networking/GamePacket;handle(Lfinalforeach/cosmicreach/networking/NetworkIdentity;Lio/netty/channel/ChannelHandlerContext;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void receivePacket(ChannelHandlerContext ctx, Object msg, CallbackInfo ci) {
        EventPacketReceiveIntercept receiveIntercept = new EventPacketReceiveIntercept();
        receiveIntercept.setPacket((GamePacket)(Object)this);
        GameRegistries.NETWORK_EVENT_BUS.post(receiveIntercept);
    }

}
