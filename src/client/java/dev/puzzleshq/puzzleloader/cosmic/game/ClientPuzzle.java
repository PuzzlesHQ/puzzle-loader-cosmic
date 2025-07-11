package dev.puzzleshq.puzzleloader.cosmic.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import de.pottgames.tuningfork.SoundBuffer;
import de.pottgames.tuningfork.SoundBufferLoader;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ClientModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ClientPostModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ClientPreModInit;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ClientSidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ClientSidedModelLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ClientSidedTextureLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedTextureLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.util.IndependentAssetLoader;

public class ClientPuzzle implements ClientPreModInit, ClientModInit, ClientPostModInit {

    @Override
    public void onClientInit() {
    }

    @Override
    public void onClientPostInit() {
//        Threads.runOnMainThread(() -> {
//            for (int i = 0; i < 16; i++) {
//                BlockModelJsonTexture texture = new BlockModelJsonTexture();
//                texture.fileName = Identifier.of("connected-textures", "test/glass_" + i + ".png").toString();
//                texture.initialize();
//            }
//        });
//        Threads.runOnMainThread(ConnectedBlock16Connector::generate);
//        ISidedBlockConnector.getInstance().registerStateAsConnectedBlock(Block.getInstance("glass").getDefaultBlockState(), ConnectedBlock16Connector::connect);
    }

    @Override
    public void onClientPreInit() {
        ISidedModelLoader.CONTEXTUAL_INSTANCE.set(new ClientSidedModelLoader());
        ISidedTextureLoader.CONTEXTUAL_INSTANCE.set(new ClientSidedTextureLoader());
        ISidedBlockConnector.CONTEXTUAL_INSTANCE.set(new ClientSidedBlockConnector());

        IndependentAssetLoader.registerLoadingMethod(Pixmap.class, (handle) -> {
            byte[] bytes = handle.getBytes();

            return new Pixmap(bytes, 0, bytes.length);
        });
        IndependentAssetLoader.registerLoadingMethod(Texture.class, (handle) -> {
            byte[] bytes = handle.getBytes();

            return new Texture(new Pixmap(bytes, 0, bytes.length));
        });
    }

}
