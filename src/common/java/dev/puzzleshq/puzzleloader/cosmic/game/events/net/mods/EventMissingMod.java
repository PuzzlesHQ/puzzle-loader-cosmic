package dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods;

import net.neoforged.bus.api.Event;

public class EventMissingMod extends Event {

    private final String modId;
    private boolean kickPlayer;

    public EventMissingMod(String modId) {
        this.modId = modId;
        this.kickPlayer = true;
    }

    public String getModId() {
        return modId;
    }

    public void setKickPlayer(boolean kickPlayer) {
        this.kickPlayer = kickPlayer;
    }

    public boolean doesKickPlayer() {
        return this.kickPlayer;
    }

}
