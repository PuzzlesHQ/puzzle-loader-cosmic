package dev.puzzleshq.puzzleloader.cosmic.core.modInitialises;

import dev.puzzleshq.puzzleloader.loader.util.PuzzleEntrypointUtil;

public interface ModInit {
    String ENTRYPOINT_KEY = "init";

    void onInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ModInit.class,
                ModInit::onInit
        );
    }
}
