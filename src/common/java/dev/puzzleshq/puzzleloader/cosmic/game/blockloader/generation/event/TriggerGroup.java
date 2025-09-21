package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.InjectedBlockAction;
import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import finalforeach.cosmicreach.gameevents.blockevents.BlockEventArgs;
import org.hjson.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TriggerGroup implements HJsonSerializable {

    List<Trigger> triggers = new ArrayList<>();

    final String name;

    public TriggerGroup(String name) {
        this.name = name;
    }

    public Trigger createTrigger(String actionId) {
        Trigger trigger = new Trigger(actionId);
        trigger.actionId = actionId;
        this.triggers.add(trigger);
        return trigger;
    }

    public void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }

    public void addTrigger(Trigger... triggers) {
        this.triggers.addAll(List.of(triggers));
    }

    public void inject(int index, Consumer<BlockEventArgs> argsConsumer) {
        long id = System.nanoTime();

        InjectedBlockAction.CONSUMER_MAP.put(id, argsConsumer);

        Trigger trigger = new Trigger("puzzle:injected_method").setParameter("injected_method_id", id);

        if (index == -1) triggers.addLast(trigger);
        else triggers.add(index, trigger);
    }

    public void insertTrigger(int idx, Trigger trigger) {
        this.triggers.add(idx, trigger);
    }

    public void removeTrigger(int idx) {
        if (idx == -1) this.triggers.removeLast();
        else this.triggers.remove(idx);
    }

    public void removeTrigger(Trigger trigger) {
        this.triggers.remove(trigger);
    }

    public String getName() {
        return name;
    }

    public JsonArray toHJson() {
        JsonArray group = new JsonArray();

        for (Trigger trigger : triggers) {
            group.add(trigger.toHJson());
        }

        return group;
    }

    @Override
    public String toString() {
            return stringify();
        }

    public void clear() {
        triggers.clear();
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }
}