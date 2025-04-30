package dev.puzzleshq.core.modInitialises;

import dev.puzzleshq.loader.mod.entrypoint.PreLaunchInitializer;
import dev.puzzleshq.loader.util.PuzzleEntrypointUtil;

public interface PreModInit {
    String ENTRYPOINT_KEY = "pre-init";

    void onPreInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                PreModInit.class,
                PreModInit::onPreInit
        );
    }
}
