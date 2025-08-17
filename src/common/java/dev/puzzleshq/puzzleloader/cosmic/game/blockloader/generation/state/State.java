package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state;

import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.hjson.Stringify;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class State implements HJsonSerializable {

    public static final float[] DEFAULT_ROTATION = new float[]{0, 0, 0};
    public final String name;

    public String languageKey = null;
    public String modelId = null;
    public String swapGroupId = null;
    public Identifier blockEventId = null;
    public String itemIcon = null;
    public String dropId = null;
    public String placementRules = null;

    public final AtomicReference<Boolean> allowsStateSwapping = new AtomicReference<>(null);
    public final AtomicReference<Boolean> isOpaque = new AtomicReference<>(null);
    public final AtomicReference<Boolean> canRaycastForBreak = new AtomicReference<>(null);
    public final AtomicReference<Boolean> canRaycastForPlaceOn = new AtomicReference<>(null);
    public final AtomicReference<Boolean> canRaycastForReplace = new AtomicReference<>(null);
    public final AtomicReference<Boolean> canDrop = new AtomicReference<>(null);
    public final AtomicReference<Boolean> isCatalogHidden = new AtomicReference<>(null);
    public final AtomicReference<Boolean> canWalkThrough = new AtomicReference<>(null);
    public final AtomicReference<Boolean> isFluid = new AtomicReference<>(null);
    public final AtomicReference<Boolean> laserResistant = new AtomicReference<>(null);

    public int lightAttenuation = -1;
    public int redLightStrength = -1;
    public int blueLightStrength = -1;
    public int greenLightStrength = -1;

    public float hardnessValue = -1;
    public float blastResistance = -1;
    public float friction = -1;
    public float bounciness = -1;
    public float refractiveIndex = -1;

    public final List<String> stateGenerators = new ArrayList<>();
    public final List<String> tags = new ArrayList<>();
    public final Map<String, Integer> intValueMap = new HashMap<>();
    public final Map<String, JsonValue> dropParameters = new HashMap<>();

    public float[] rotation = new float[]{0, 0, 0};

    public JsonObject canPlacePredicate = new JsonObject();

    public State(String name) {
        this.name = name;
    }

    public void setRotXZRotation(int rotXZ) {
        this.rotation[1] = rotXZ;
    }

    public void setPlacementPredicate(int xOffs, int yOffs, int zOffs, PredicateEnum predicateEnum, String value) {
        canPlacePredicate.add("xoff", xOffs);
        canPlacePredicate.add("yoff", yOffs);
        canPlacePredicate.add("zoff", zOffs);

        canPlacePredicate.add(predicateEnum.name, value);
    }

    public JsonObject toHJson() {
        JsonObject state = new JsonObject();

        if (canPlacePredicate != null && !canPlacePredicate.isEmpty())
            state.add("canPlace", canPlacePredicate);

        addIfNotNull(state, "langKey", languageKey);
        addIfNotNull(state, "modelName", modelId);
        addIfNotNull(state, "swapGroupId", swapGroupId);
        addIfNotNull(state, "blockEventsId", blockEventId == null ? null : blockEventId.toString());
        addIfNotNull(state, "itemIcon", itemIcon);
        addIfNotNull(state, "dropId", dropId);
        addIfNotNull(state, "placementRules", placementRules);

        addIdNotDefault(state, "allowSwapping", allowsStateSwapping);
        addIdNotDefault(state, "canRaycastForBreak", canRaycastForBreak);
        addIdNotDefault(state, "canRaycastForPlaceOn", canRaycastForPlaceOn);
        addIdNotDefault(state, "canRaycastForReplace", canRaycastForReplace);
        addIdNotDefault(state, "canDrop", canDrop);
        addIdNotDefault(state, "catalogHidden", isCatalogHidden);
        addIdNotDefault(state, "walkThrough", canWalkThrough);
        addIdNotDefault(state, "isFluid", isFluid);
        addIdNotDefault(state, "isOpaque", isOpaque);
        addIdNotDefault(state, "stopsLasers", laserResistant);

        addIdNotDefault(state, "lightAttenuation", lightAttenuation);
        addIdNotDefault(state, "redLightStrength", redLightStrength);
        addIdNotDefault(state, "blueLightStrength", blueLightStrength);
        addIdNotDefault(state, "greenLightStrength", greenLightStrength);

        addIdNotDefault(state, "hardnessValue", hardnessValue);
        addIdNotDefault(state, "blastResistance", blastResistance);
        addIdNotDefault(state, "friction", friction);
        addIdNotDefault(state, "bounciness", bounciness);
        addIdNotDefault(state, "refractiveIndex", refractiveIndex);

        if (this.stateGenerators != null && !this.stateGenerators.isEmpty()) {
            JsonArray stateGenerators = new JsonArray();
            for (String s : this.stateGenerators) stateGenerators.add(s);
            state.add("stateGenerators", stateGenerators);
        }

        if (this.intValueMap != null && !this.intValueMap.isEmpty()) {
            JsonObject intMap = new JsonObject();
            for (Map.Entry<String, Integer> entry : this.intValueMap.entrySet())
                intMap.add(entry.getKey(), entry.getValue());
            state.add("intProperties", intMap);
        }

        if (this.dropParameters != null && !this.dropParameters.isEmpty()) {
            JsonObject dropParameters = new JsonObject();
            for (Map.Entry<String, JsonValue> entry : this.dropParameters.entrySet())
                dropParameters.add(entry.getKey(), entry.getValue());
            state.add("dropParams", dropParameters);
        }

        if (this.tags != null && !this.tags.isEmpty()) {
            JsonArray tags = new JsonArray();
            for (String tag : this.tags) {
                tags.add(tag);
            }
            state.add("tags", tags);
        }

        if (this.rotation != null && this.rotation.length == 3 && !Arrays.equals(this.rotation, State.DEFAULT_ROTATION)){
            JsonArray rotation = new JsonArray();
            for (float tag : this.rotation) {
                rotation.add(tag);
            }
            state.add("rotation", rotation);
        }

        System.out.println(name + " |  " + state.toString(Stringify.FORMATTED));

        return state;
    }

    private void addIdNotDefault(JsonObject state, String str, int i) {
        if (-1 != i)
            state.add(str, i);
    }

    private void addIdNotDefault(JsonObject state, String str, float i) {
        if (-1f != i)
            state.add(str, i);
    }

    private void addIdNotDefault(JsonObject state, String str, AtomicReference<Boolean> i) {
        if (null != i.get())
            state.add(str, i.get());
    }

    private void addIfNotNull(JsonObject state, String key, String value) {
        if (value != null)
            state.add(key, value);
    }

    public String toString() {
        return stringify();
    }

}
