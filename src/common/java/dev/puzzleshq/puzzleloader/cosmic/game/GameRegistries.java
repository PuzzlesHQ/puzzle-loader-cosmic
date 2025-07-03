package dev.puzzleshq.puzzleloader.cosmic.game;

import net.neoforged.bus.api.BusBuilder;
import net.neoforged.bus.api.IEventBus;

public class GameRegistries {

    public static final IEventBus COSMIC_EVENT_BUS = BusBuilder.builder().build();

}
