package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model;

import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import org.hjson.JsonArray;
import org.hjson.JsonObject;

public class ModelFace implements HJsonSerializable {

    public float[] uv = new float[]{0, 0, 16, 16};
    public boolean useAmbientOcclusion = true;
    public boolean canCullFace = true;
    public String textureId;

    public void setUVs(float minU, float minV, float maxU, float maxV) {
        this.uv[0] = minU;
        this.uv[1] = minV;
        this.uv[2] = maxU;
        this.uv[3] = maxV;
    }

    public JsonObject toHJson() {
        JsonObject face = new JsonObject();

        float[] uv = this.uv;
        JsonArray uvs = new JsonArray();
        uvs.add(uv[0]);
        uvs.add(uv[1]);
        uvs.add(uv[2]);
        uvs.add(uv[3]);
        face.add("uv", uvs);

        face.add("ambientocclusion", this.useAmbientOcclusion);
        face.add("cullface", this.canCullFace);
        face.add("texture", this.textureId);
        return face;
    }

    @Override
    public String toString() {
        return stringify();
    }
}