package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event;

import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

public class BlockEventReader {

    public static BlockEventGenerator fromString(String string) {
        JsonObject object = JsonObject.readHjson(string).asObject();

        String parent = object.getString("parent", null);
        String id = object.getString("stringId", null);

        BlockEventGenerator generator = new BlockEventGenerator(parent != null ? Identifier.of(parent) : null, Identifier.of(id));
        JsonValue value = object.get("triggers");

        if (value == null) return generator;
        JsonObject triggerMap = value.asObject();

        for (String groupName : triggerMap.names()) {
            TriggerGroup group = generator.createTriggerGroup(groupName);

            for (JsonValue v : triggerMap.get(groupName).asArray()) {
                JsonObject triggerObj = v.asObject();
                Trigger trigger = group.createTrigger(triggerObj.getString("actionId", null));
                trigger.setCondition(triggerObj.get("if") == null ? null : triggerObj.get("if").asObject());

                if (triggerObj.get("parameters") == null) continue;

                JsonObject params = triggerObj.get("parameters").asObject();
                for (String paramName : params.names()) {
                    trigger.setParameter(paramName, params.get(paramName));
                }
            }
        }

        return generator;
    }

}
