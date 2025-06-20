package dev.puzzleshq.puzzleloader.cosmic.core.modInitialises;

import dev.puzzleshq.puzzleloader.loader.util.PuzzleEntrypointUtil;

public interface ServerPreModInit {
    String ENTRYPOINT_KEY = "server-preInit";

    void onServerPreInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ServerPreModInit.class,
                ServerPreModInit::onServerPreInit
        );
    }
}
