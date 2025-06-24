package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.ChunkMeshGroup;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkMeshGroup.class)
public class MixinChunkMeshGroup {

    @Redirect(method = "getMeshData", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blocks/BlockState;addVertices(Lfinalforeach/cosmicreach/rendering/IMeshData;IIII[S[I)V"))
    private static void addVertices(BlockState state, IMeshData meshData, int bx, int by, int bz, int opaqueBitmask, short[] blockLightLevels, int[] skyLightLevels, @Local Chunk chunk) {
        Zone zone = chunk.getZone();

        ISidedBlockConnector.getInstance().connect(zone, chunk, state, bx, by, bz, meshData, opaqueBitmask, blockLightLevels, skyLightLevels);
    }

}
