package dev.puzzleshq.puzzleloader.cosmic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import de.pottgames.tuningfork.*;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ClientSidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ClientSidedModelLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ClientSidedTextureLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedTextureLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.util.IndependentAssetLoader;
import dev.puzzleshq.puzzleloader.loader.LoaderConfig;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.client.ClientModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.client.ClientPostModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.client.ClientPreModInit;
import dev.puzzleshq.puzzleloader.loader.transformers.GLFWTransformer;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class ClientPuzzle implements ClientPreModInit, ClientModInit, ClientPostModInit {

    @Override
    public void onClientInit() {
        loadArgs();
        PuzzleIcons.setIcon();
        PuzzleIcons.disposeIcon();
    }

    @Override
    public void onClientPostInit() {
    }

    @Override
    public void onClientPreInit() {
        ISidedModelLoader.CONTEXTUAL_INSTANCE.set(new ClientSidedModelLoader());
        ISidedTextureLoader.CONTEXTUAL_INSTANCE.set(new ClientSidedTextureLoader());
        ISidedBlockConnector.CONTEXTUAL_INSTANCE.set(new ClientSidedBlockConnector());

        IndependentAssetLoader.registerLoadingMethod(Pixmap.class, (handle) -> {
            byte[] bytes = handle.getHandle().getBytes();

            return new Pixmap(bytes, 0, bytes.length);
        });
        IndependentAssetLoader.registerLoadingMethod(Texture.class, Texture::new);

        IndependentAssetLoader.registerLoadingMethod(SoundBuffer.class, (h) -> {
            SoundFileType type = SoundFileType.getByFileEnding(h.getHandle().getFile());
            InputStream stream = new ByteArrayInputStream(h.getHandle().getBytes());
            SoundBuffer buffer = switch (type) {
                case OGG -> OggLoader.load(stream);
                case WAV -> WaveLoader.load(stream);
                case FLAC -> FlacLoader.load(stream);
                case MP3 -> Mp3Loader.load(stream);
                case AIFF -> AiffLoader.load(stream);
                case QOA -> QoaLoader.load(stream);
            };
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return buffer;
        });
    }

    private static void loadArgs() {
        String[] args = LoaderConfig.COMMAND_LINE_ARGUMENTS;

        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();

        OptionSpec<String> windowTitle = parser.acceptsAll(List.of("window-title", "wt")).withOptionalArg()
                .ofType(String.class);

        OptionSpec<String> windowSize = parser.acceptsAll(List.of("window-size", "ws")).withOptionalArg()
                .ofType(String.class);

        OptionSpec<Boolean> fullScreen = parser.acceptsAll(List.of("fullscreen", "fs", "maximized", "m")).withOptionalArg()
                .ofType(Boolean.class);

        OptionSet set = parser.parse(args);

        if (set.has(windowTitle)) Gdx.graphics.setTitle(windowTitle.value(set));
        if (set.has(windowSize)) {
            String size = windowSize.value(set);

            String[] strings =  size.split("x");
            int w = Integer.parseInt(strings[0]);
            int h = Integer.parseInt(strings[1]);

            if (w != 0 || h != 0) {
                Gdx.graphics.setWindowedMode(w, h);
            }
        }
        if (set.has(fullScreen) && fullScreen.value(set)) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }

}
