package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import finalforeach.cosmicreach.util.Identifier;

public class AbstractModBlock implements IModBlock {

    protected final BlockGenerator generator;
    protected final Identifier id;

    public AbstractModBlock(Identifier id) {
        this.id = id;
        this.generator = new BlockGenerator(id);
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }

    public Identifier getId() {
        return id;
    }

}
