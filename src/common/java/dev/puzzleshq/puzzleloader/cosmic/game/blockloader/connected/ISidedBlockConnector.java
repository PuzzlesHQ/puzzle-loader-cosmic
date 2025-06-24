package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

import java.util.concurrent.atomic.AtomicReference;

public interface ISidedBlockConnector {

    AtomicReference<ISidedBlockConnector> CONTEXTUAL_INSTANCE = new AtomicReference<>();

    static ISidedBlockConnector getInstance() {
        return CONTEXTUAL_INSTANCE.get();
    }

    void registerAsConnectedBlock(Block block, ConnectorFunction function);
    void registerStateAsConnectedBlock(BlockState state, ConnectorFunction function);

    void connect(
            Zone zone, Chunk chunk, BlockState state,
            int bx, int by, int bz, IMeshData meshData, int opaqueBitmask,
            short[] blockLightLevels, int[] skyLightLevels
    );

    interface ConnectorFunction {
        void connect(Zone zone, Chunk chunk, BlockState blockState, int x, int y, int z, IMeshData meshData, int opaqueBitmask, short[] blockLightLevels, int[] skyLightLevels);
    }

}
