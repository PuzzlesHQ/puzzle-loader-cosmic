package dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods;

import net.neoforged.bus.api.Event;

public class EventModVersionMismatch extends Event {

    private final String modId;
    private boolean kickPlayer;
    private final String serverVersion;
    private final String clientVersion;

    public EventModVersionMismatch(
            String modId,
            String serverVersion,
            String clientVersion
    ) {
        this.modId = modId;
        this.kickPlayer = true;
        this.serverVersion = serverVersion;
        this.clientVersion = clientVersion;
    }

    public String getModId() {
        return modId;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setKickPlayer(boolean kickPlayer) {
        this.kickPlayer = kickPlayer;
    }

    public boolean doesKickPlayer() {
        return this.kickPlayer;
    }

}
