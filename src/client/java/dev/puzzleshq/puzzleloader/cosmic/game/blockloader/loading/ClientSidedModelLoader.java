package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;

public class ClientSidedModelLoader implements ISidedModelLoader {

    @Override
    public void loadModel(BlockModelGenerator modelGenerator) {
        float[] floats = new float[3];

        for (int x = 360; x > -1; x -= 90) {
            for (int y = 360; y > -1; y -= 90) {
                for (int z = 360; z > -1; z -= 90) {
                    floats[0] = x;
                    floats[1] = y;
                    floats[2] = z;
                    BlockModelJson.getInstanceFromJsonStr(modelGenerator.getName(), modelGenerator.toString(), floats);
                }
            }
        }
    }

}
