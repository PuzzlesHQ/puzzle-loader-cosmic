package dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods;

import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.packets.PacketModListExchange;
import dev.puzzleshq.puzzleloader.cosmic.game.util.UnmodifiableSet;
import finalforeach.cosmicreach.accounts.Account;
import net.neoforged.bus.api.Event;

import java.util.Set;

public class EventReceiveClientModList extends Event {

    private final Set<PacketModListExchange.MiniModInfo> modList;
    private final Account playerAccount;

    public EventReceiveClientModList(
            Account playerAccount,
            Set<PacketModListExchange.MiniModInfo> modList
    ) {
        this.modList = new UnmodifiableSet<>(modList);
        this.playerAccount = playerAccount;
    }

    public Account getPlayerAccount() {
        return playerAccount;
    }

    public Set<PacketModListExchange.MiniModInfo> getModList() {
        return modList;
    }

}
