package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event;

import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class Trigger implements HJsonSerializable {
        String actionId = null;
        JsonObject condition;
        Map<String, JsonValue> parameters = new HashMap<>();

        public Trigger(String actionId) {
            this.actionId = actionId;
            this.condition = null;
        }

        public Trigger(String actionId, JsonObject condition) {
            this.actionId = actionId;
            this.condition = condition;
        }

        public void setCondition(JsonObject argsPredicate) {
            this.condition = argsPredicate;
        }

        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        public String getActionId() {
            return actionId;
        }

        public Trigger setParameter(String key, Identifier s) {
            this.parameters.put(key, JsonValue.valueOf(s.toString()));
            return this;
        }

        public Trigger setParameter(String key, String s) {
            this.parameters.put(key, JsonValue.valueOf(s));
            return this;
        }

        public Trigger setParameter(String key, byte i) {
            this.parameters.put(key, JsonValue.valueOf(i));
            return this;
        }

        public Trigger setParameter(String key, short i) {
            this.parameters.put(key, JsonValue.valueOf(i));
            return this;
        }

        public Trigger setParameter(String key, int i) {
            this.parameters.put(key, JsonValue.valueOf(i));
            return this;
        }

        public Trigger setParameter(String key, long i) {
            this.parameters.put(key, JsonValue.valueOf(i));
            return this;
        }

        public Trigger setParameter(String key, float i) {
            this.parameters.put(key, JsonValue.valueOf(i));
            return this;
        }

        public Trigger setParameter(String key, double i) {
            this.parameters.put(key, JsonValue.valueOf(i));
            return this;
        }

        public Trigger setParameter(String key, boolean i) {
            this.parameters.put(key, JsonValue.valueOf(i));
            return this;
        }

        public Trigger setParameter(String key, JsonValue value) {
            this.parameters.put(key, value);
            return this;
        }

        public Trigger setParameter(String key, String... values) {
            JsonArray array = new JsonArray();
            for (String v : values) array.add(v);
            this.parameters.put(key, array);
            return this;
        }

        public Trigger setParameter(String key, byte... values) {
            JsonArray array = new JsonArray();
            for (byte v : values) array.add(v);
            this.parameters.put(key, array);
            return this;
        }

        public Trigger setParameter(String key, short... values) {
            JsonArray array = new JsonArray();
            for (short v : values) array.add(v);
            this.parameters.put(key, array);
            return this;
        }

        public Trigger setParameter(String key, int... values) {
            JsonArray array = new JsonArray();
            for (int v : values) array.add(v);
            this.parameters.put(key, array);
            return this;
        }

        public Trigger setParameter(String key, long... values) {
            JsonArray array = new JsonArray();
            for (long v : values) array.add(v);
            this.parameters.put(key, array);
            return this;
        }

        public Trigger setParameter(String key, float... values) {
            JsonArray array = new JsonArray();
            for (float v : values) array.add(v);
            this.parameters.put(key, array);
            return this;
        }

        public Trigger setParameter(String key, double... values) {
            JsonArray array = new JsonArray();
            for (double v : values) array.add(v);
            this.parameters.put(key, array);
            return this;
        }

        public Trigger setParameter(String key, boolean... values) {
            JsonArray array = new JsonArray();
            for (boolean v : values) array.add(v);
            this.parameters.put(key, array);
            return this;
        }

        public Trigger setParameter(String key, Identifier... values) {
            JsonArray array = new JsonArray();
            for (Identifier v : values) array.add(v.toString());
            this.parameters.put(key, array);
            return this;
        }

        public Map<String, JsonValue> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, JsonValue> parameters) {
            this.parameters = parameters;
        }

        public JsonObject toHJson() {
            JsonObject trigger = new JsonObject();
            trigger.add("actionId", actionId);
            if (condition != null) trigger.add("if", condition);

            JsonObject params = new JsonObject();
            for (Map.Entry<String, JsonValue> valueEntry : parameters.entrySet()) {
                params.add(valueEntry.getKey(), valueEntry.getValue());
            }
            trigger.add("parameters", params);

            return trigger;
        }

        @Override
        public String toString() {
            return stringify();
        }

    }