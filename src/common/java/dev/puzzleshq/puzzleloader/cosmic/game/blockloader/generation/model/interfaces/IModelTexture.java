package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.interfaces;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.interfaces.impl.BlockModelTexture;
import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import finalforeach.cosmicreach.util.Identifier;

public interface IModelTexture extends HJsonSerializable {

    static BlockModelTexture newBasicTexture() {
        return new BlockModelTexture();
    }

    default IModelTexture setDiffuse(String diffuse) {
        return setDiffuse(Identifier.of(diffuse));
    }

    default IModelTexture setEmissive(String emissive) {
        return setEmissive(Identifier.of(emissive));
    }

    IModelTexture setDiffuse(Identifier diffuseLocation);
    IModelTexture setEmissive(Identifier emissiveLocation);

    Identifier getDiffuse();
    Identifier getEmissive();

}
