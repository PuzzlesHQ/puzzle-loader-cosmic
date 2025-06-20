package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event;

import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class BlockEventGenerator implements HJsonSerializable {

    public static final Identifier DEFAULT_BLOCK_EVENTS_ID = Identifier.of("base", "block_events_default");

    final Identifier id;
    final Identifier parentId;
    final Map<String, TriggerGroup> triggerMap = new HashMap<>();

    public BlockEventGenerator(BlockEventGenerator generator, Identifier id) {
        this.id = id;
        this.parentId = generator.id;
    }

    public BlockEventGenerator(Identifier parent, Identifier id) {
        this.id = id;
        this.parentId = parent;
    }

    public BlockEventGenerator(Identifier id) {
        this.id = id;
        this.parentId = null;
    }

    public Map<String, TriggerGroup> getTriggerMap() {
        return triggerMap;
    }

    public Identifier getId() {
        return id;
    }

    public Identifier getParentId() {
        return parentId;
    }

    public TriggerGroup createTriggerGroup(String name) {
        TriggerGroup group = new TriggerGroup(name);
        this.triggerMap.put(name, group);
        return group;
    }

    public TriggerGroup getTriggerGroup(String name) {
        return this.triggerMap.get(name);
    }

    public TriggerGroup putTriggerGroup(TriggerGroup group) {
        return this.triggerMap.put(group.name, group);
    }

    public JsonObject toHJson() {
        JsonObject event = new JsonObject();
        if (parentId != null) event.add("parent", parentId.toString());
        event.add("stringId", id.toString());

        JsonObject triggerGroups = new JsonObject();
        for (TriggerGroup group : this.triggerMap.values()) {
            triggerGroups.add(group.name, group.toHJson());
        }
        event.add("triggers", triggerGroups);

        return event;
    }

    @Override
    public String toString() {
        return stringify();
    }

}
