package dev.puzzleshq.puzzleloader.cosmic.game.aprilfools;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import finalforeach.cosmicreach.util.Identifier;

public class AprilFoolsRedStoneModBlock implements IModBlock {

    BlockGenerator generator;
    BlockModelGenerator modelGenerator;

    public AprilFoolsRedStoneModBlock() {
        generator = new BlockGenerator(Identifier.of("base:red_stone"));

        State state = generator.createState("default");
        state.modelId = "puzzle_model_red_stone";
        state.stateGenerators.add("base:slabs_seamed_all");
        state.tags.add("tool_pickaxe_effective");

        modelGenerator = new BlockModelGenerator("puzzle_model_red_stone");
        modelGenerator.addTexture("top", Identifier.of("puzzle-loader-cosmic", "textures/blocks/red_stone_block.png"));
        modelGenerator.addTexture("bottom", Identifier.of("puzzle-loader-cosmic", "textures/blocks/red_stone_block.png"));
        modelGenerator.addTexture("side", Identifier.of("puzzle-loader-cosmic", "textures/blocks/red_stone_block.png"));

        ModelCuboid cuboid = modelGenerator.createCuboid(Vector3.Zero, 16, 16, 16);
        cuboid.setTextureIds("top", "bottom", "side");
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }

    @Override
    public BlockModelGenerator[] getModelGenerators() {
        return new BlockModelGenerator[]{modelGenerator};
    }
}
