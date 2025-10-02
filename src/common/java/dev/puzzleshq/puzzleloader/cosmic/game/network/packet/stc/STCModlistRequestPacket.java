package dev.puzzleshq.puzzleloader.cosmic.game.network.packet.stc;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import dev.puzzleshq.mod.api.IModContainer;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.cts.CTSModlistPacket;
import dev.puzzleshq.puzzleloader.loader.util.ModFinder;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.NetworkSide;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class STCModlistRequestPacket extends GamePacket {

    public STCModlistRequestPacket() {}

    @Override
    public void receive(ByteBuf byteBuf) {}

    @Override
    public void write() {}

    @SuppressWarnings("unchecked")
    @Override
    public void handle(NetworkIdentity identity, ChannelHandlerContext channelHandlerContext) {
        if (identity.getSide() == NetworkSide.CLIENT) {
            List<Pair<String, String>> mods = new ArrayList<>();
            for (IModContainer container : ModFinder.getModsArray()) {
                mods.add(new ImmutablePair<>(container.getID(), container.getVersion().toString()));
            }

            identity.send(new CTSModlistPacket(mods.toArray(new Pair[0])));
        }
    }
}