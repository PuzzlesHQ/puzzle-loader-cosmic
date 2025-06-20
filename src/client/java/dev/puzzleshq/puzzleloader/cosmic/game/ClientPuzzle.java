package dev.puzzleshq.puzzleloader.cosmic.game;

import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ClientModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ClientPostModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ClientPreModInit;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ClientSidedModelLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;

public class ClientPuzzle implements ClientPreModInit, ClientModInit, ClientPostModInit {

    @Override
    public void onClientInit() {
        System.err.println("ClientPuzzle init called");
    }

    @Override
    public void onClientPostInit() {
        System.err.println("ClientPuzzle postInit called");
    }

    @Override
    public void onClientPreInit() {
        ISidedModelLoader.CONTEXTUAL_INSTANCE.set(new ClientSidedModelLoader());

        System.err.println("ClientPuzzle preInit called");
    }

}
