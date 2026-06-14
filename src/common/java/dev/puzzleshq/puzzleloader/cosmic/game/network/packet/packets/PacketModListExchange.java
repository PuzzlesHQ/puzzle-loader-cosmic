package dev.puzzleshq.puzzleloader.cosmic.game.network.packet.packets;

import dev.puzzleshq.mod.api.IModContainer;
import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods.EventReceiveClientModList;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods.EventReceiveServerModList;
import dev.puzzleshq.puzzleloader.loader.util.ModFinder;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PacketModListExchange extends GamePacket {

    private final Set<MiniModInfo> mods = new HashSet<MiniModInfo>();

    public PacketModListExchange() {}

    @Override
    public void receive(ByteBuf in) {
        int count = readInt(in);
        for (int i = 0; i < count; i++) {
            String modName = readString(in);
            String modVersion = readString(in);
            boolean worksClientSide = readBoolean(in);
            boolean worksServerSide = readBoolean(in);

            mods.add(new MiniModInfo(modName, modVersion, worksClientSide, worksServerSide));
        }
    }

    @Override
    public void write() {
        AtomicBoolean clientSided = new AtomicBoolean(false);
        AtomicBoolean serverSided = new AtomicBoolean(false);

        List<IModContainer> containers = ModFinder.getModsArray();
        writeInt(containers.size());

        for (IModContainer container : containers) {
            clientSided.set(false);
            serverSided.set(false);

            writeString(container.getID());
            writeString(container.getVersionStr());

            Map<String, Boolean> sides = container.getInfo().getLoadableSides();
            sides.forEach((sid, isLoadable) -> {
                if (sid.equalsIgnoreCase("client")) clientSided.set(true);
                if (sid.equalsIgnoreCase("server")) serverSided.set(true);
            });

            writeBoolean(clientSided.get());
            writeBoolean(serverSided.get());
        }
    }

    @Override
    public void handle(NetworkIdentity networkIdentity, ChannelHandlerContext channelHandlerContext) {
        switch (networkIdentity.getSide()) {
            case CLIENT -> {
                EventReceiveServerModList event = new EventReceiveServerModList(mods);
                GameRegistries.COSMIC_EVENT_BUS.post(event);
                networkIdentity.send(new PacketModListExchange());
            }
            case SERVER -> {
                EventReceiveClientModList event = new EventReceiveClientModList(
                        networkIdentity.getAccount(),
                        mods
                );
                GameRegistries.COSMIC_EVENT_BUS.post(event);
            }
        }
    }

    @Override
    public boolean shouldCompress() {
        return true;
    }

    public record MiniModInfo(
           String modId,
           String modVersion,
           boolean worksClientSide,
           boolean worksServerSide
    ) {}
}
