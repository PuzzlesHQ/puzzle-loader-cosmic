package dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IConnectedBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import finalforeach.cosmicreach.util.Identifier;

public class ConnectedBlock16 extends AbstractConnectedBlock implements IModBlock, IConnectedBlock{

    BlockGenerator generator;

    public static final Identifier id = Identifier.of("connected-textures", "connected-block-16");

    public ConnectedBlock16() {
        super(id, true);
        generator = new BlockGenerator(id);
        State state = generator.createState("default");
        state.modelId = defaultModelName;
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }

}
