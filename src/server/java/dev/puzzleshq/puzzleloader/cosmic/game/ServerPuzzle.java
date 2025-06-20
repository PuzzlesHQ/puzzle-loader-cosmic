package dev.puzzleshq.puzzleloader.cosmic.game;

import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ServerModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ServerPostModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ServerPreModInit;

public class ServerPuzzle implements ServerPreModInit, ServerModInit, ServerPostModInit {
    @Override
    public void onServerInit() {
        System.err.println("ServerPuzzle init called");
    }

    @Override
    public void onServerPostInit() {
        System.err.println("ServerPuzzle postInit called");
    }

    @Override
    public void onServerPreInit() {
        System.err.println("ServerPuzzle preInit called");
    }
}
