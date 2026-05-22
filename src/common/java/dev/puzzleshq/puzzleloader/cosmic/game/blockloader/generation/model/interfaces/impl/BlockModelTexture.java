package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.interfaces.impl;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.interfaces.IModelTexture;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

public class BlockModelTexture implements IModelTexture {

    private Identifier diffuseLocation;
    private Identifier emissiveLocation;

    public BlockModelTexture() {
        this.diffuseLocation = Identifier.of("puzzle-loader-cosmic:textures/blocks/zombiis-connected-block-states/state-error.png");
        this.emissiveLocation = Identifier.of("puzzle-loader-cosmic:textures/blocks/zombiis-connected-block-states/state-error.png");
    }

    @Override
    public BlockModelTexture setDiffuse(Identifier diffuseLocation) {
        this.diffuseLocation = diffuseLocation;
        return this;
    }

    @Override
    public BlockModelTexture setEmissive(Identifier emissiveLocation) {
        this.emissiveLocation = emissiveLocation;
        return this;
    }

    @Override
    public Identifier getDiffuse() {
        return diffuseLocation;
    }

    @Override
    public Identifier getEmissive() {
        return emissiveLocation;
    }

    @Override
    public JsonValue toHJson() {
        JsonObject jsonObject = new JsonObject();
        if (diffuseLocation != null)
            jsonObject.add("fileName", diffuseLocation.toString());
        if (emissiveLocation != null)
            jsonObject.add("emissivefileName", emissiveLocation.toString());
        return jsonObject;
    }
}
