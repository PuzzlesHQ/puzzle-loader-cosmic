package dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.AbstractConnectedModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import finalforeach.cosmicreach.util.Identifier;

public class ExampleConnectedBlock extends AbstractConnectedModBlock {

    public static final Identifier id = Identifier.of("puzzle-loader-cosmic", "zombiis-connected-block");

    public ExampleConnectedBlock() {
        super(id, TextureModes.TEXTURE_MODE_16, true);

        State state = generator.createState("default");
        state.canDrop.set(true);
        state.languageKey = "Zombii's Connected Block";
        state.modelId = defaultModelName;
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }

}
