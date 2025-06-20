package dev.puzzleshq.puzzleloader.cosmic.core.modInitialises;

import dev.puzzleshq.puzzleloader.loader.util.PuzzleEntrypointUtil;

public interface ClientModInit {
    String ENTRYPOINT_KEY = "client-init";

    void onClientInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ClientModInit.class,
                ClientModInit::onClientInit
        );
    }
}
