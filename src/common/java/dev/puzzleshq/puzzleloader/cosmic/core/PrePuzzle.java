package dev.puzzleshq.puzzleloader.cosmic.core;

import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.PreLaunchInitializer;

public class PrePuzzle implements PreLaunchInitializer {

    @Override
    public void onPreLaunch() {
        System.err.println("Pre-Puzzle Loaded");
    }

}
