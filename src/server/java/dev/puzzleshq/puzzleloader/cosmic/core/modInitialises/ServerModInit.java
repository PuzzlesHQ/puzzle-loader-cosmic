package dev.puzzleshq.puzzleloader.cosmic.core.modInitialises;

import dev.puzzleshq.puzzleloader.loader.util.PuzzleEntrypointUtil;

public interface ServerModInit {
    String ENTRYPOINT_KEY = "server-init";

    void onServerInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ServerModInit.class,
                ServerModInit::onServerInit
        );
    }
}
