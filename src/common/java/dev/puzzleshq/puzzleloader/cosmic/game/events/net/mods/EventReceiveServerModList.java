package dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods;

import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.packets.PacketModListExchange;
import dev.puzzleshq.puzzleloader.cosmic.game.util.UnmodifiableSet;
import net.neoforged.bus.api.Event;

import java.util.Set;

public class EventReceiveServerModList extends Event {

    private final Set<PacketModListExchange.MiniModInfo> modList;

    public EventReceiveServerModList(Set<PacketModListExchange.MiniModInfo> modList) {
        this.modList = new UnmodifiableSet<>(modList);
    }

    public Set<PacketModListExchange.MiniModInfo> getModList() {
        return modList;
    }

}
