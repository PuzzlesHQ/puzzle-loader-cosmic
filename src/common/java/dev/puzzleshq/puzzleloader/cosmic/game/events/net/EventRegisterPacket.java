package dev.puzzleshq.puzzleloader.cosmic.game.events.net;

import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.PacketInterceptor;
import finalforeach.cosmicreach.networking.GamePacket;
import net.neoforged.bus.api.Event;

public class EventRegisterPacket extends Event {

    public EventRegisterPacket() {}

    public <T extends GamePacket> void registerPacketLazy(Class<T> packetClass) {
        PacketInterceptor.registerPacketLazy(packetClass);
    }

    public <T extends GamePacket> void registerPacketLazy(String strId, Class<T> packetClass) {
        PacketInterceptor.registerPacketLazy(strId, packetClass);
    }

    public <T extends GamePacket> void registerPacket(String strId, int numId, Class<T> packetClass) {
        PacketInterceptor.registerPacket(strId, numId, packetClass);
    }

    public <T extends GamePacket> void registerReservedPacket(String strId, int numId, Class<T> packetClass) {
        PacketInterceptor.registerReservedPacket(strId, numId, packetClass);
    }
}
