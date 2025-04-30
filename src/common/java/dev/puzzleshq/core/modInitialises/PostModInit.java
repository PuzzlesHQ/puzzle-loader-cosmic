package dev.puzzleshq.core.modInitialises;

import dev.puzzleshq.loader.mod.entrypoint.PreLaunchInitializer;
import dev.puzzleshq.loader.util.PuzzleEntrypointUtil;

public interface PostModInit {
    String ENTRYPOINT_KEY = "post-init";

    void onPostInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                PostModInit.class,
                PostModInit::onPostInit
        );
    }
}
