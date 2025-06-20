package dev.puzzleshq.puzzleloader.cosmic.game;

import net.neoforged.bus.BusBuilderImpl;
import net.neoforged.bus.api.IEventBus;

public class GameRegistries {

    public static final IEventBus COSMIC_EVENT_BUS = new BusBuilderImpl().build();

}
