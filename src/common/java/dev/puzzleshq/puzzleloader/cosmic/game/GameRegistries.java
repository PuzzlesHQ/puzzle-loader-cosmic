package dev.puzzleshq.puzzleloader.cosmic.game;

import dev.puzzleshq.puzzleloader.cosmic.core.registries.GenericRegistry;
import dev.puzzleshq.puzzleloader.cosmic.core.registries.IRegistry;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import finalforeach.cosmicreach.util.Identifier;

public class GameRegistries {

    public static final IRegistry<BlockModelGenerator> MODEL_GENERATOR_REGISTRY = new GenericRegistry<>(Identifier.of("puzzle-loader-cosmic", "model-generator-registry"));

}
