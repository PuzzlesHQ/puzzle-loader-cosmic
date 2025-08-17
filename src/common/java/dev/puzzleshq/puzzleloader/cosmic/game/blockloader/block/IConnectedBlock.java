package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import finalforeach.cosmicreach.blocks.Block;

public interface IConnectedBlock extends ISidedBlockConnector.ConnectorFunction, IModBlock {

    @Override
    default void onRegistered(Block block) {
        ISidedBlockConnector connector = ISidedBlockConnector.getInstance();
        connector.registerAsConnectedBlock(block, this);
    }
}
