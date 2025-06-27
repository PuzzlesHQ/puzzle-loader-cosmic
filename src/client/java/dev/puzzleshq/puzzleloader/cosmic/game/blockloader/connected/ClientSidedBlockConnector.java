package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected;

import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class ClientSidedBlockConnector implements ISidedBlockConnector {

    static final Map<BlockState, ConnectorFunction> CONNECTION_FUNCTIONS = new HashMap<>();

    @Override
    public void registerAsConnectedBlock(Block block, ConnectorFunction function) {
        for (BlockState state : block.blockStates.values()) {
            registerStateAsConnectedBlock(state, function);
        }
    }

    @Override
    public void registerStateAsConnectedBlock(BlockState state, ConnectorFunction function) {
        CONNECTION_FUNCTIONS.put(state, function);
    }

    @Override
    public void connect(Zone zone, Chunk chunk, BlockState state, int bx, int by, int bz, IMeshData meshData, int opaqueBitmask, short[] blockLightLevels, int[] skyLightLevels) {
        if (CONNECTION_FUNCTIONS.containsKey(state)) {
            CONNECTION_FUNCTIONS.get(state).connect(zone, chunk, state, bx, by, bz, meshData, opaqueBitmask, blockLightLevels, skyLightLevels);
            return;
        }
        state.addVertices(meshData, bx, by, bz, opaqueBitmask, blockLightLevels, skyLightLevels);
    }

    @Override
    public boolean isConnectedBlock(BlockState state) {
        return CONNECTION_FUNCTIONS.containsKey(state);
    }

}
