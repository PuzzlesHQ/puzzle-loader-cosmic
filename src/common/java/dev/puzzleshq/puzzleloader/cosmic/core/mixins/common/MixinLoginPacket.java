package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import dev.puzzleshq.puzzleloader.cosmic.game.network.api.IServerIdentity;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.packets.PacketModListExchange;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.NetworkSide;
import finalforeach.cosmicreach.networking.packets.meta.LoginPacket;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LoginPacket.class)
public abstract class MixinLoginPacket extends GamePacket {

    @Inject(method = "handle", at = @At("TAIL"))
    private void handlePacket(NetworkIdentity identity, ChannelHandlerContext ctx, CallbackInfo ci) {
        if (Objects.requireNonNull(identity.getSide()) == NetworkSide.SERVER) {
            IServerIdentity serverIdentity = (IServerIdentity) identity;
            boolean puzzleLoader = serverIdentity.getClientName().equals("puzzle-loader");
            if (puzzleLoader) {
                identity.send(new PacketModListExchange());
            }
        }
    }

}