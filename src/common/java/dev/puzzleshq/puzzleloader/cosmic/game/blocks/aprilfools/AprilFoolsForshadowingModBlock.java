package dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.util.Identifier;

public class AprilFoolsForshadowingModBlock implements IModBlock {

    BlockGenerator generator;
    BlockModelGenerator modelGenerator;
    BlockEventGenerator eventGenerator;

    public AprilFoolsForshadowingModBlock() {
        generator = new BlockGenerator(Identifier.of("base:foreshadowing"));

        State state = generator.createState("default");
        state.modelId = "puzzle_model_foreshadowing";
        state.blockEventId = Identifier.of("puzzle", "foreshadowing_event");
        state.tags.add("tool_pickaxe_effective");

        modelGenerator = new BlockModelGenerator("puzzle_model_foreshadowing");
        modelGenerator.addTexture("top", Identifier.of("puzzle-loader-cosmic", "textures/blocks/foreshadowing_side.png"));
        modelGenerator.addTexture("bottom", Identifier.of("puzzle-loader-cosmic", "textures/blocks/foreshadowing_side.png"));
        modelGenerator.addTexture("side", Identifier.of("puzzle-loader-cosmic", "textures/blocks/foreshadowing_face.png"));

        ModelCuboid cuboid = modelGenerator.createCuboid(Vector3.Zero, 16, 16, 16);
        cuboid.setTextureIds("top", "bottom", "side");

        eventGenerator = new BlockEventGenerator(BlockEventGenerator.DEFAULT_BLOCK_EVENTS_ID, Identifier.of("puzzle", "foreshadowing_event"));
        eventGenerator.inheritParentContents();
        eventGenerator.inject(-1, "onInteract", this::onInteract);
    }

    @Override
    public void onInteract(BlockEventArgs args) {
        System.err.println("TEST");
        args.srcPlayer.getEntity().forceHit(1);
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }

    @Override
    public BlockModelGenerator[] getModelGenerators() {
        return new BlockModelGenerator[]{modelGenerator};
    }

    @Override
    public BlockEventGenerator[] getEventGenerators() {
        return new BlockEventGenerator[]{eventGenerator};
    }
}
