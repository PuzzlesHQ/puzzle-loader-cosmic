package dev.puzzleshq.puzzleloader.cosmic.core.modInitialises;

import dev.puzzleshq.puzzleloader.loader.util.PuzzleEntrypointUtil;

public interface PostModInit {
    String ENTRYPOINT_KEY = "postInit";

    void onPostInit();

    static void invoke() {
        PuzzleEntrypointUtil.invoke(
                ENTRYPOINT_KEY,
                PostModInit.class,
                PostModInit::onPostInit
        );
    }
}
