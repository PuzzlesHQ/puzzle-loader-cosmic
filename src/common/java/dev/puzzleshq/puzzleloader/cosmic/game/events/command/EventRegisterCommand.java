package dev.puzzleshq.puzzleloader.cosmic.game.events.command;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.chat.commands.Command;
import net.neoforged.bus.api.Event;

import java.util.List;
import java.util.function.Supplier;

public class EventRegisterCommand extends Event {

    private final List<Pair<String[], Supplier<Command>>> commandFactories;

    public EventRegisterCommand(List<Pair<String[], Supplier<Command>>> commandFactories) {
        this.commandFactories = commandFactories;
    }

    public void register(Supplier<Command> commandSupplier, String... aliases) {
        this.commandFactories.add(new ImmutablePair<>(aliases, commandSupplier));
    }

}
