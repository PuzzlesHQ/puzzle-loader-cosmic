package dev.puzzleshq.puzzleloader.cosmic.game.events.zone;

import finalforeach.cosmicreach.worldgen.ZoneGenerator;
import net.neoforged.bus.api.Event;

import java.util.List;
import java.util.function.Supplier;

public class EventRegisterZoneGenerator extends Event {

    private final List<Supplier<ZoneGenerator>> zoneFactories;

    public EventRegisterZoneGenerator(List<Supplier<ZoneGenerator>> zoneFactories) {
        this.zoneFactories = zoneFactories;
    }

    public void registerGenerator(Supplier<ZoneGenerator> zoneGenerator) {
        this.zoneFactories.add(zoneGenerator);
    }

}
