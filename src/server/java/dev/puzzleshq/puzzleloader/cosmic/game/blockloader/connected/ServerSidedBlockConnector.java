package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class ServerSidedBlockConnector implements ISidedBlockConnector {

    @Override
    public void registerAsConnectedBlock(Block block, ConnectorFunction function) {

    }

    @Override
    public void registerStateAsConnectedBlock(BlockState state, ConnectorFunction function) {

    }

    @Override
    public void connect(Zone zone, Chunk chunk, BlockState state, int bx, int by, int bz, IMeshData meshData, int opaqueBitmask, short[] blockLightLevels, int[] skyLightLevels) {

    }

    @Override
    public boolean isConnectedBlock(BlockState state) {
        return false;
    }

}
