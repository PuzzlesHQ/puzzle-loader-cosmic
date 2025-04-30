package dev.puzzleshq.core.modInitialises;

import dev.puzzleshq.loader.mod.entrypoint.PreLaunchInitializer;
import dev.puzzleshq.loader.util.PuzzleEntrypointUtil;

public interface ClientPreModInit {
    String ENTRYPOINT_KEY = "client-pre-init";

    void onClientPreInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                ClientPreModInit.class,
                ClientPreModInit::onClientPreInit
        );
    }
}
