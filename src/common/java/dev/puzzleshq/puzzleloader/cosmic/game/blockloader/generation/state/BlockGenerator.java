package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state;

import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class BlockGenerator implements HJsonSerializable {

    Map<String, State> stateMap = new HashMap<>();
    State defaultProperties = new State("nil");

    Identifier blockId;
    Identifier blockEntityId;
    Map<String, JsonValue> blockEntityParams = new HashMap<>();

    public BlockGenerator(Identifier id) {
        this.blockId = id;
    }

    public State getState(String id) {
        return this.stateMap.get(id);
    }

    public void setBlockEntity(Identifier bei) {
        this.blockEntityId = bei;
    }

    public void setDefaultProperties(State defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public State getDefaultProperties() {
        return defaultProperties;
    }

    public Map<String, State> getStateMap() {
        return stateMap;
    }

    public Map<String, JsonValue> getBlockEntityParams() {
        return blockEntityParams;
    }

    public BlockGenerator setBlockEntityParameter(String key, Identifier s) {
        this.blockEntityParams.put(key, JsonValue.valueOf(s.toString()));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, String s) {
        this.blockEntityParams.put(key, JsonValue.valueOf(s));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, byte i) {
        this.blockEntityParams.put(key, JsonValue.valueOf(i));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, short i) {
        this.blockEntityParams.put(key, JsonValue.valueOf(i));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, int i) {
        this.blockEntityParams.put(key, JsonValue.valueOf(i));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, long i) {
        this.blockEntityParams.put(key, JsonValue.valueOf(i));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, float i) {
        this.blockEntityParams.put(key, JsonValue.valueOf(i));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, double i) {
        this.blockEntityParams.put(key, JsonValue.valueOf(i));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, boolean i) {
        this.blockEntityParams.put(key, JsonValue.valueOf(i));
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, JsonValue value) {
        this.blockEntityParams.put(key, value);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, String... values) {
        JsonArray array = new JsonArray();
        for (String v : values) array.add(v);
        this.blockEntityParams.put(key, array);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, byte... values) {
        JsonArray array = new JsonArray();
        for (byte v : values) array.add(v);
        this.blockEntityParams.put(key, array);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, short... values) {
        JsonArray array = new JsonArray();
        for (short v : values) array.add(v);
        this.blockEntityParams.put(key, array);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, int... values) {
        JsonArray array = new JsonArray();
        for (int v : values) array.add(v);
        this.blockEntityParams.put(key, array);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, long... values) {
        JsonArray array = new JsonArray();
        for (long v : values) array.add(v);
        this.blockEntityParams.put(key, array);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, float... values) {
        JsonArray array = new JsonArray();
        for (float v : values) array.add(v);
        this.blockEntityParams.put(key, array);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, double... values) {
        JsonArray array = new JsonArray();
        for (double v : values) array.add(v);
        this.blockEntityParams.put(key, array);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, boolean... values) {
        JsonArray array = new JsonArray();
        for (boolean v : values) array.add(v);
        this.blockEntityParams.put(key, array);
        return this;
    }

    public BlockGenerator setBlockEntityParameter(String key, Identifier... values) {
        JsonArray array = new JsonArray();
        for (Identifier v : values) array.add(v.toString());
        this.blockEntityParams.put(key, array);
        return this;
    }

    public State createState(String name) {
        State state = new State(name);
        this.stateMap.put(name, state);
        return state;
    }

    public State createState(String name, String modelName) {
        State state = new State(name);
        state.modelId = modelName;
        this.stateMap.put(name, state);
        return state;
    }

    @Override
    public JsonValue toHJson() {
        JsonObject object = new JsonObject();
        object.add("stringId", blockId.toString());
        if (blockEntityId != null) object.add("blockEntityId", blockEntityId.toString());
        if (defaultProperties != null) object.add("defaultProperties", defaultProperties.toHJson());
        if (stateMap != null && !stateMap.isEmpty()) {
            JsonObject states = new JsonObject();
            for (Map.Entry<String, State> stateEntry : stateMap.entrySet()) {
                states.add(stateEntry.getKey(), stateEntry.getValue().toHJson());
            }
            object.add("blockStates", states);
        }
        if (blockEntityId != null) object.add("blockEntityId", blockEntityId.toString());
        if (blockEntityParams != null && !blockEntityParams.isEmpty()) {
            JsonObject params = new JsonObject();
            for (Map.Entry<String, JsonValue> entry : blockEntityParams.entrySet()) {
                params.add(entry.getKey(), entry.getValue());
            }
            object.add("blockEntityParams", params);
        }

        return object;
    }

    @Override
    public String toString() {
        return stringify();
    }

    public Identifier getId() {
        return blockId;
    }
}
