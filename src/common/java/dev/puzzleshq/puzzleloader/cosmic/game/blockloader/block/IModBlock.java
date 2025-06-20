package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;

public interface IModBlock {

    @BlockEventsTriggerInjection(triggerGroup = "onBreak")
    default void onBreak(BlockEventArgs args) {}
    @BlockEventsTriggerInjection(triggerGroup = "onPlace")
    default void onPlace(BlockEventArgs args) {}
    @BlockEventsTriggerInjection(triggerGroup = "onInteract")
    default void onInteract(BlockEventArgs args) {}

    BlockGenerator getGenerator();

    default BlockModelGenerator[] getModelGenerators() {
        return null;
    }

    default BlockEventGenerator[] getEventGenerators() {
        return null;
    }

}
