package dev.puzzleshq.puzzleloader.cosmic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture3D;
import de.pottgames.tuningfork.*;
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
import dev.puzzleshq.puzzleloader.loader.LoaderConstants;
import finalforeach.cosmicreach.io.SaveLocation;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ClientPuzzle implements ClientPreModInit, ClientModInit, ClientPostModInit {

    public static String autoJoinWorldName = null;

    @Override
    public void onClientInit() {
        loadArgs();
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

        IndependentAssetLoader.registerLoadingMethod(SoundBuffer.class, (h) -> {
            SoundFileType type = SoundFileType.getByFileEnding(h.getFile());
            InputStream stream = new ByteArrayInputStream(h.getBytes());
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

    public static void loadArgs() {
        String[] args = LoaderConstants.CLIConfiguration.COMMAND_LINE_ARGUMENTS;

        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();

        OptionSpec<String> saveLocation = parser.acceptsAll(List.of("save-location", "s")).withOptionalArg()
                .ofType(String.class);

        OptionSpec<String> windowTitle = parser.acceptsAll(List.of("window-title", "wt")).withOptionalArg()
                .ofType(String.class);

        OptionSpec<String> windowSize = parser.acceptsAll(List.of("window-size", "ws")).withOptionalArg()
                .ofType(String.class);

        OptionSpec<Boolean> fullScreen = parser.acceptsAll(List.of("fullscreen", "fs", "maximized", "m")).withOptionalArg()
                .ofType(Boolean.class);

        OptionSpec<String> worldJoin = parser.acceptsAll(List.of("join-world", "jw")).withOptionalArg()
                .ofType(String.class);

        OptionSet set = parser.parse(args);

        if (set.has(saveLocation)) {
            SaveLocation.saveLocationOverride = saveLocation.value(set);
            (new File(SaveLocation.saveLocationOverride)).mkdirs();
        }

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
        if (set.has(worldJoin)) {
            autoJoinWorldName = worldJoin.value(set);
        }
    }

}
