package dev.puzzleshq.core.modInitialises;

import dev.puzzleshq.loader.mod.entrypoint.PreLaunchInitializer;
import dev.puzzleshq.loader.util.PuzzleEntrypointUtil;

public interface ClientPostModInit {
    String ENTRYPOINT_KEY = "client-post-init";

    void onClientPostInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ClientPostModInit.class,
                ClientPostModInit::onClientPostInit
        );
    }
}
