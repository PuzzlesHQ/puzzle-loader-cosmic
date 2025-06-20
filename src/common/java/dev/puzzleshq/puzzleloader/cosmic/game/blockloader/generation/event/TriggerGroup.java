package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event;

import dev.puzzleshq.puzzleloader.cosmic.game.util.HJsonSerializable;
import org.hjson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class TriggerGroup implements HJsonSerializable {
        List<Trigger> triggers = new ArrayList<>();

        final String name;

        public TriggerGroup(String name) {
            this.name = name;
        }

        public Trigger createTrigger(String actionId) {
            Trigger trigger = new Trigger();
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

        public void insertTrigger(int idx, Trigger trigger) {
            this.triggers.add(idx, trigger);
        }

        public void removeTrigger(int idx) {
            this.triggers.remove(idx);
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
    }