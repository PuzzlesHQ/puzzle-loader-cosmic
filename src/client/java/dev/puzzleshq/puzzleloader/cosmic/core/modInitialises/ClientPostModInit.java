package dev.puzzleshq.puzzleloader.cosmic.core.modInitialises;

import dev.puzzleshq.puzzleloader.loader.util.PuzzleEntrypointUtil;

public interface ClientPostModInit {
    String ENTRYPOINT_KEY = "client-postInit";

    void onClientPostInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ClientPostModInit.class,
                ClientPostModInit::onClientPostInit
        );
    }
}
