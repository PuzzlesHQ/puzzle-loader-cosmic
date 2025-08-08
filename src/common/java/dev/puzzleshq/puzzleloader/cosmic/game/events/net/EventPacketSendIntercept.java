package dev.puzzleshq.puzzleloader.cosmic.game.events.net;

import finalforeach.cosmicreach.networking.GamePacket;
import net.neoforged.bus.api.Event;

public class EventPacketSendIntercept extends Event {

    private GamePacket packet;

    public void setPacket(GamePacket packet) {
        this.packet = packet;
    }

    public GamePacket getPacket() {
        return packet;
    }

}