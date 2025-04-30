package dev.puzzleshq.core.modInitialises;

import dev.puzzleshq.loader.mod.entrypoint.PreLaunchInitializer;
import dev.puzzleshq.loader.util.PuzzleEntrypointUtil;

public interface ServerPostModInit {
    String ENTRYPOINT_KEY = "server-post-init";

    void onServerPostInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ServerPostModInit.class,
                ServerPostModInit::onServerPostInit
        );
    }
}
