package dev.puzzleshq.core.modInitialises;

import dev.puzzleshq.loader.mod.entrypoint.PreLaunchInitializer;
import dev.puzzleshq.loader.util.PuzzleEntrypointUtil;

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
