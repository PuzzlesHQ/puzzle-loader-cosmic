package dev.puzzleshq.puzzleloader.cosmic.game.network.packet;

import dev.puzzleshq.puzzleloader.loader.LoaderConstants;
import dev.puzzleshq.puzzleloader.loader.util.EnvType;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class NetworkHandler {

    static final Logger LOGGER = LogManager.getLogger("Puzzle | " + LoaderConstants.SIDE.name().toUpperCase() + "NetworkHandler");

    static final Map<Class<? extends PuzzlePacket>, PacketHandler> clientHandlers = new HashMap<>();
    static final Map<Class<? extends PuzzlePacket>, PacketHandler> serverHandlers = new HashMap<>();

    public static <T extends PuzzlePacket> void registerPacketHandler(Class<T> packetClass, EnvType type, PacketHandler handler) {
        switch (type) {
            case CLIENT -> {
                if (LoaderConstants.SIDE == EnvType.SERVER) throw new RuntimeException("Cannot register a client packet handler on the server for the packet type \"" + packetClass.getName() + "\"");
                clientHandlers.put(packetClass, handler);
            }
            case SERVER -> {
                if (LoaderConstants.SIDE == EnvType.CLIENT) throw new RuntimeException("Cannot register a server packet handler on the client for the packet type \"" + packetClass.getName() + "\"");
                serverHandlers.put(packetClass, handler);
            }
            default -> throw new RuntimeException("Not able to use Environment Type " + type.name() + " for packet handler side");
        }
    }

    public interface PacketHandler {

        void onHandle(PuzzlePacket packet, NetworkIdentity identity, ChannelHandlerContext ctx);
    }

}