package dev.puzzleshq.puzzleloader.cosmic.game.events.registry;

import dev.puzzleshq.puzzleloader.cosmic.core.registries.IRegistry;
import finalforeach.cosmicreach.util.Identifier;
import net.neoforged.bus.api.Event;

public class EventRegisterObject extends Event {

    public final IRegistry registry;
    public final Identifier id;
    public final Object obj;

    public EventRegisterObject(IRegistry registry, Identifier id, Object obj) {
        this.registry = registry;
        this.obj = obj;
        this.id = id;
    }

}