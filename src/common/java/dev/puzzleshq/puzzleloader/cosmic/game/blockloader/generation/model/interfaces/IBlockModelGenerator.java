package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.interfaces;

import java.util.List;

public interface IBlockModelGenerator {

    int LOCAL_NEG_X = 0;
    int LOCAL_POS_X = 1;
    int LOCAL_NEG_Y = 2;
    int LOCAL_POS_Y = 3;
    int LOCAL_NEG_Z = 4;
    int LOCAL_POS_Z = 5;

    String[] DIRECTIONS = new String[] {
            "localNegX", "localPosX",
            "localNegY", "localPosY",
            "localNegZ", "localPosZ",
    };

    String getParent();
    List<IModelCuboid> getCuboids();
    List<IModelPlane> getPlanes();


}
