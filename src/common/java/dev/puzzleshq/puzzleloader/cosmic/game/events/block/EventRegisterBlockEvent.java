package dev.puzzleshq.puzzleloader.cosmic.game.events.block;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventReader;
import net.neoforged.bus.api.Event;

public class EventRegisterBlockEvent extends Event {

    BlockEventGenerator generator;

    public EventRegisterBlockEvent(String json) {
        this.generator = BlockEventReader.fromString(json);
    }

    public BlockEventGenerator getBlockEventsGenerator() {
        return generator;
    }

}
