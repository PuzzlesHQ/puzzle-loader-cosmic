package dev.puzzleshq.core.modInitialises;

import dev.puzzleshq.loader.mod.entrypoint.PreLaunchInitializer;
import dev.puzzleshq.loader.util.PuzzleEntrypointUtil;

public interface ServerPreModInit {
    String ENTRYPOINT_KEY = "server-pre-init";

    void onServerPreInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ServerPreModInit.class,
                ServerPreModInit::onServerPreInit
        );
    }
}
