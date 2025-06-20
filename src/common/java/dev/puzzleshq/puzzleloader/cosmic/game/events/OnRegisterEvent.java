package dev.puzzleshq.puzzleloader.cosmic.game.events;

import dev.puzzleshq.puzzleloader.cosmic.core.registries.IRegistry;
import finalforeach.cosmicreach.util.Identifier;
import net.neoforged.bus.api.Event;

public class OnRegisterEvent extends Event {

    public final IRegistry registry;
    public final Identifier id;
    public final Object obj;

    public OnRegisterEvent(IRegistry registry, Identifier id, Object obj) {
        this.registry = registry;
        this.obj = obj;
        this.id = id;
    }

}