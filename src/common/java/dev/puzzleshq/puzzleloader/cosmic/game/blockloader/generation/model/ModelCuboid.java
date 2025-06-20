package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import org.hjson.JsonArray;
import org.hjson.JsonObject;

public class ModelCuboid implements HJsonSerializable {

    public static final int LOCAL_NEG_X = 0;
    public static final int LOCAL_POS_X = 1;
    public static final int LOCAL_NEG_Y = 2;
    public static final int LOCAL_POS_Y = 3;
    public static final int LOCAL_NEG_Z = 4;
    public static final int LOCAL_POS_Z = 5;

    private static final String[] directions = new String[]{
            "localNegX",
            "localPosX",
            "localNegY",
            "localPosY",
            "localNegZ",
            "localPosZ",
    };

    public ModelFace[] faces = new ModelFace[6];

    public Vector3 min = new Vector3();
    public Vector3 max = new Vector3();

    public ModelCuboid() {
        this.min.set(0, 0, 0);
        this.max.set(16, 16, 16);

        this.resetFaces();
    }

    public ModelCuboid(
            Vector3 position,
            float width, float height, float depth
    ) {
        this.min.set(position);
        this.max.set(width, height, depth);

        this.resetFaces();
    }

    public ModelCuboid(
            Vector3 min,
            Vector3 max
    ) {
        this.min.set(min);
        this.max.set(max);

        this.resetFaces();
    }

    public void resetFaces() {
        for (int i = 0; i < 6; i++)
            this.faces[i] = new ModelFace();
    }

    public void setAO(boolean useAmbientOcclusion) {
        for (int i = 0; i < 6; i++)
            this.faces[i].useAmbientOcclusion = useAmbientOcclusion;
    }

    public void setCullFace(boolean canCullFaces) {
        for (int i = 0; i < 6; i++)
            this.faces[i].canCullFace = canCullFaces;
    }

    public void setTextureIds(String textureId) {
        for (int i = 0; i < 6; i++)
            this.faces[i].textureId = textureId;
    }

    public void setTextureIds(String topId, String bottomId, String sideId) {
        this.faces[LOCAL_POS_Y].textureId = topId;
        this.faces[LOCAL_NEG_Y].textureId = bottomId;

        this.faces[LOCAL_NEG_X].textureId = sideId;
        this.faces[LOCAL_NEG_Z].textureId = sideId;

        this.faces[LOCAL_POS_X].textureId = sideId;
        this.faces[LOCAL_POS_Z].textureId = sideId;
    }

    public void setTextureIds(String topId, String bottomId, String rightId, String leftId, String frontId, String backId) {
        this.faces[LOCAL_POS_Y].textureId = topId;
        this.faces[LOCAL_NEG_Y].textureId = bottomId;

        this.faces[LOCAL_NEG_X].textureId = leftId;
        this.faces[LOCAL_NEG_Z].textureId = backId;

        this.faces[LOCAL_POS_X].textureId = rightId;
        this.faces[LOCAL_POS_Z].textureId = frontId;
    }

    public JsonObject toHJson() {
        JsonObject cuboid = new JsonObject();

        JsonArray localBounds = new JsonArray();

        localBounds.add(this.min.x);
        localBounds.add(this.min.y);
        localBounds.add(this.min.z);

        localBounds.add(this.max.x);
        localBounds.add(this.max.y);
        localBounds.add(this.max.z);

        cuboid.add("localBounds", localBounds);

        JsonObject faces = new JsonObject();

        for (int i = 0; i < directions.length; i++) {
            String d = directions[i];
            ModelFace modelFace = this.faces[i];
            if (modelFace == null) continue;
            faces.add(d, modelFace.toHJson());
        }

        cuboid.add("faces", faces);

        return cuboid;
    }

    @Override
    public String toString() {
        return stringify();
    }

}