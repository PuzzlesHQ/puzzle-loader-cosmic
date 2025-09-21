package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event;

import com.badlogic.gdx.files.FileHandle;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.InjectedBlockAction;
import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.gameevents.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class BlockEventGenerator implements HJsonSerializable {

    public static final Identifier DEFAULT_BLOCK_EVENTS_ID = Identifier.of("base", "block_events_default");

    final Identifier id;
    Identifier parentId;
    final Map<String, TriggerGroup> triggerMap = new HashMap<>();

    final static Map<Identifier, BlockEventGenerator> GENERATOR_MAP = new ConcurrentHashMap<>();

    public BlockEventGenerator(BlockEventGenerator generator, Identifier id) {
        this.id = id;
        this.parentId = generator.id;

        GENERATOR_MAP.put(id, this);
    }

    public BlockEventGenerator(Identifier parent, Identifier id) {
        this.id = id;
        this.parentId = parent;

        GENERATOR_MAP.put(id, this);
    }

    public BlockEventGenerator(Identifier id) {
        this.id = id;
        this.parentId = null;

        GENERATOR_MAP.put(id, this);
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

    public TriggerGroup getOrCreateTriggerGroup(String name) {
        TriggerGroup group = this.triggerMap.get(name);
        if (group == null) this.triggerMap.put(name, group = new TriggerGroup(name));
        return group;
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

    public BlockEventGenerator getParent() {
        if (parentId == null) return null;

        if (GENERATOR_MAP.get(parentId) != null) return GENERATOR_MAP.get(parentId);
        String ns = parentId.getNamespace();
        String path = "block_events/" + parentId.getName() + ".json";

        FileHandle handle = GameAssetLoader.loadAsset(Identifier.of(ns, path));
        return BlockEventReader.fromString(handle.readString());
    }

    public void inheritParentContents() {
        BlockEventGenerator generator = getParent();
        if (generator == null) return;
        for (Map.Entry<String, TriggerGroup> groupEntry : generator.triggerMap.entrySet()) {
            if (!triggerMap.containsKey(groupEntry.getKey())) triggerMap.put(groupEntry.getKey(), groupEntry.getValue());
            else getTriggerGroup(groupEntry.getKey()).triggers.addAll(generator.triggerMap.get(groupEntry.getKey()).triggers);
        }
    }

    public void inject(int index, String group, Consumer<BlockEventArgs> argsConsumer) {
        long id = System.nanoTime();

        InjectedBlockAction.CONSUMER_MAP.put(id, argsConsumer);

        TriggerGroup group1 = getTriggerGroup(group);
        if (group1 == null) group1 = createTriggerGroup(group);

        Trigger trigger = new Trigger("puzzle:injected_method").setParameter("injected_method_id", id);

        if (index == -1) group1.triggers.addLast(trigger);
        else group1.triggers.add(index, trigger);
    }

    public void setParent(Identifier id) {
        parentId = id;
    }
}
