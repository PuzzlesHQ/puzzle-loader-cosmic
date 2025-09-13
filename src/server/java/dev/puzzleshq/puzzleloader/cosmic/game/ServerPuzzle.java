package dev.puzzleshq.puzzleloader.cosmic.game;

import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ServerSidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedTextureLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ServerSidedModelLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ServerSidedTextureLoader;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.server.ServerModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.server.ServerPostModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.server.ServerPreModInit;

public class ServerPuzzle implements ServerPreModInit, ServerModInit, ServerPostModInit {
    @Override
    public void onServerInit() {
    }

    @Override
    public void onServerPostInit() {
    }

    @Override
    public void onServerPreInit() {
        ISidedModelLoader.CONTEXTUAL_INSTANCE.set(new ServerSidedModelLoader());
        ISidedBlockConnector.CONTEXTUAL_INSTANCE.set(new ServerSidedBlockConnector());
        ISidedTextureLoader.CONTEXTUAL_INSTANCE.set(new ServerSidedTextureLoader());
    }
}
