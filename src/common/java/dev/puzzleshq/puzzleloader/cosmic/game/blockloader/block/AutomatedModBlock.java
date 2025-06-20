package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AutomatedModBlock implements IModBlock {

    BlockGenerator generator;

    Consumer<BlockEventArgs> onPlaceConsumer = (args) -> {};
    Consumer<BlockEventArgs> onBreakConsumer = (args) -> {};
    Consumer<BlockEventArgs> onInteractConsumer = (args) -> {};

    final List<BlockEventGenerator> eventGenerators = new ArrayList<>();
    final List<BlockModelGenerator> modelGenerators = new ArrayList<>();

    final Identifier id;

    public AutomatedModBlock(Identifier blockId) {
        this.id = blockId;

        this.generator = new BlockGenerator(blockId);
    }

    public BlockGenerator replaceGenerator(BlockGenerator generator) {
        if (generator == null) throw new IllegalArgumentException("Cannot replace block generator to block \"" + id + "\" since it was null.");
        this.generator = generator;
        return generator;
    }

    public BlockEventGenerator addGenerator(BlockEventGenerator generator) {
        if (generator == null) throw new IllegalArgumentException("Cannot add block event generator to block \"" + id + "\" since it was null.");
        this.eventGenerators.add(generator);
        return generator;
    }

    public BlockModelGenerator addGenerator(BlockModelGenerator generator) {
        if (generator == null) throw new IllegalArgumentException("Cannot add block model generator to block \"" + id + "\" since it was null.");
        this.modelGenerators.add(generator);
        return generator;
    }

    @Override
    public void onBreak(BlockEventArgs args) {
        onBreakConsumer.accept(args);
    }

    @Override
    public void onPlace(BlockEventArgs args) {
        onPlaceConsumer.accept(args);
    }

    @Override
    public void onInteract(BlockEventArgs args) {
        onInteractConsumer.accept(args);
    }

    public BlockGenerator getGenerator() {
        return generator;
    }

    @Override
    public BlockModelGenerator[] getModelGenerators() {
        return new BlockModelGenerator[0];
    }

    @Override
    public BlockEventGenerator[] getEventGenerator() {
        return new BlockEventGenerator[0];
    }

    public void setOnBreakConsumer(Consumer<BlockEventArgs> onBreakConsumer) {
        if (onBreakConsumer == null) throw new IllegalArgumentException("Cannot set onBreakConsumer  \"" + id + "\" since it was null.");
        this.onBreakConsumer = onBreakConsumer;
    }

    public void setOnInteractConsumer(Consumer<BlockEventArgs> onInteractConsumer) {
        if (onInteractConsumer == null) throw new IllegalArgumentException("Cannot set onBreakConsumer  \"" + id + "\" since it was null.");
        this.onInteractConsumer = onInteractConsumer;
    }

    public void setOnPlaceConsumer(Consumer<BlockEventArgs> onPlaceConsumer) {
        if (onPlaceConsumer == null) throw new IllegalArgumentException("Cannot set onBreakConsumer  \"" + id + "\" since it was null.");
        this.onPlaceConsumer = onPlaceConsumer;
    }

    public Consumer<BlockEventArgs> getOnBreakConsumer() {
        return onBreakConsumer;
    }

    public Consumer<BlockEventArgs> getOnInteractConsumer() {
        return onInteractConsumer;
    }

    public Consumer<BlockEventArgs> getOnPlaceConsumer() {
        return onPlaceConsumer;
    }

    public Identifier getId() {
        return id;
    }
}
