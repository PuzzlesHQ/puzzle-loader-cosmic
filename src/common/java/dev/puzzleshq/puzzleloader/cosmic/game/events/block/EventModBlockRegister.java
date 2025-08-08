package dev.puzzleshq.puzzleloader.cosmic.game.events.block;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

public class EventModBlockRegister extends Event {

    List<IModBlock> blocks;

    public EventModBlockRegister() {
        blocks = new ArrayList<>();
    }

    public void register(IModBlock modBlockSupplier) {
        blocks.add(modBlockSupplier);
    }

    public List<IModBlock> getBlocks() {
        return blocks;
    }
}
