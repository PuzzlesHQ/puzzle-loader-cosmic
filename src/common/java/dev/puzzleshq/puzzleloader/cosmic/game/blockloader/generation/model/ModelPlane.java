package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.annotation.documentation.Note;
import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

@Note("This class is subject to change as I refactor the block model package")
public class ModelPlane implements HJsonSerializable {

    private final Vector3[] vertices = new Vector3[]{
            new Vector3(0,0,0),
            new Vector3(16,0,0),
            new Vector3(0,16,0),
            new Vector3(16,16,0),
    };
    private final float[] uvs = new float[]{0, 0, 16, 0, 0, 16, 16, 16};
    private boolean cullFace = false;
    private int uvRotation = 0;
    private boolean doubleSided = false;
    private String cullFaceDirection = null;

    private String texture = "all";

    public ModelPlane() {}

    public ModelPlane(Vector3[] vertices) {
        setVerticies(vertices);
    }

    public ModelPlane(Vector3[] vertices, String texture) {
        setVerticies(vertices);
        this.texture = texture;
    }

    public ModelPlane(String texture) {
        this.texture = texture;
    }

    public Vector3[] getVertices() {
        return vertices;
    }

    public Vector3 getVertex(int index) {
        return vertices[index];
    }

    public ModelPlane setVertex(int index, Vector3 vertex) {
        vertices[index].set(vertex);
        return this;
    }

    public ModelPlane setVertex(int index, float x, float y, float z) {
        vertices[index].set(x, y, z);
        return this;
    }

    public ModelPlane setVerticies(Vector3[] vertices) {
        this.vertices[0].set(vertices[0]);
        this.vertices[1].set(vertices[1]);
        this.vertices[2].set(vertices[2]);
        this.vertices[3].set(vertices[3]);
        return this;
    }

    public float[] getUvs() {
        return uvs;
    }

    public int getUvRotation() {
        return uvRotation;
    }

    public String getTexture() {
        return texture;
    }

    public boolean getCullFace() {
        return cullFace;
    }

    public boolean isDoubleSided() {
        return doubleSided;
    }

    public ModelPlane setCullFace(boolean cullFace) {
        this.cullFace = cullFace;
        return this;
    }

    public ModelPlane setIsDoubleSided(boolean doubleSided) {
        this.doubleSided = doubleSided;
        return this;
    }

    public ModelPlane setTexture(String texture) {
        this.texture = texture;
        return this;
    }

    public ModelPlane setUvRotation(int uvRotation) {
        this.uvRotation = uvRotation;
        return this;
    }

    public ModelPlane setCullFaceDirection(String cullFaceDirection) {
        this.cullFaceDirection = cullFaceDirection;
        return this;
    }

    public String getCullFaceDirection() {
        return cullFaceDirection;
    }

    @Override
    public JsonValue toHJson() {
        JsonObject plane = new JsonObject();

        JsonArray vertexArray = new JsonArray();
        for (Vector3 vertex : vertices) {
            vertexArray.add(vertex.x);
            vertexArray.add(vertex.y);
            vertexArray.add(vertex.z);
        }
        JsonArray uvArray = new JsonArray();
        for (float uv : uvs) {
            uvArray.add(uv);
        }

        plane.set("vertices", vertexArray);
        plane.set("uv", uvArray);
        plane.set("texture", texture);
        plane.set("cullFace", cullFace);
        plane.set("doubleSided", doubleSided);
        plane.set("uvRotation", uvRotation);
        if (cullFaceDirection != null) {
            plane.set("cullFaceDirection", cullFaceDirection);
        }

        return plane;
    }
}
