package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;

public interface IModBlock {

    default void onBreak(BlockEventArgs args) {}
    default void onPlace(BlockEventArgs args) {}
    default void onInteract(BlockEventArgs args) {}

    BlockGenerator getGenerator();
    BlockModelGenerator[] getModelGenerators();
    BlockEventGenerator[] getEventGenerator();

}
