package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.util.Identifier;

public class AbstractConnectedModBlock extends AbstractConnectorFunction implements IConnectedBlock {

    protected final Identifier id;
    protected final BlockGenerator generator;
    protected final boolean autoRegisterAllStatesWithConnector;

    public AbstractConnectedModBlock(Identifier id, TextureModes modes, boolean autoRegisterAllStatesWithConnector) {
        super(id, modes);
        this.id = id;
        this.generator = new BlockGenerator(id);
        this.autoRegisterAllStatesWithConnector = autoRegisterAllStatesWithConnector;
    }

    @Override
    public void onRegistered(Block block) {
        if (!autoRegisterAllStatesWithConnector) return;

        ISidedBlockConnector connector = ISidedBlockConnector.getInstance();
        connector.registerAsConnectedBlock(block, this);
    }

    @Override
    public BlockGenerator getGenerator() {
        for (State value : generator.getStateMap().values()) {
            if (value.modelId == null)
                value.modelId = defaultModelName;
        }
        return generator;
    }

    @Override
    public Identifier getId() {
        return id;
    }

}
