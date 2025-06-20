package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.Stringify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class BlockModelGenerator {

    public Identifier parentModel;

    public List<ModelCuboid> cuboids;
    public Map<String, Identifier> textures;

    String name;

    public BlockModelGenerator(String name) {
        this(null, name);
    }

    public BlockModelGenerator(Identifier parentModel, String name) {
        this.cuboids = new ArrayList<>();
        this.textures = new HashMap<>();

        this.parentModel = parentModel;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static BlockModelGenerator of(AtomicReference<String> outputName, String name) {
        outputName.set(name = (name + "@" + System.nanoTime()));
        // appends @248123502451... to the name preventing model collision.

        return new BlockModelGenerator(name);
    }

    public void addTexture(
            String id,
            Identifier location
    ) {
        this.textures.put(id, location);
    }

    public ModelCuboid createCuboid(
            Vector3 min,
            Vector3 max
    ) {
        ModelCuboid cuboid = new ModelCuboid(min, max);
        this.cuboids.add(cuboid);
        return cuboid;
    }

    public ModelCuboid createCuboid(
            Vector3 position,
            float width, float height, float depth
    ) {
        ModelCuboid cuboid = new ModelCuboid(position, width, height, depth);
        this.cuboids.add(cuboid);
        return cuboid;
    }

    public JsonObject toJson() {
        JsonObject modelJson = new JsonObject();

        JsonObject textures = new JsonObject();
        if (parentModel != null) modelJson.add("parent", parentModel.toString());
        for (Map.Entry<String, Identifier> texture : this.textures.entrySet()) {
            JsonObject textureObj = new JsonObject();
            textureObj.add("fileName", texture.getValue().toString());
            textures.add(texture.getKey(), textureObj);
        }
        modelJson.add("textures", textures);

        JsonArray cuboids = new JsonArray();
        for (ModelCuboid modelCuboid : this.cuboids) {
            cuboids.add(modelCuboid.toHJson());
        }
        modelJson.add("cuboids", cuboids);

        return modelJson;
    }

    public String toString() {
        return toJson().toString(Stringify.FORMATTED);
    }



}
