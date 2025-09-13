package dev.puzzleshq.puzzleloader.cosmic.game.util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import dev.puzzleshq.puzzleloader.loader.util.RawAssetLoader;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.util.Identifier;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IndependentAssetLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | Independent Asset Loader");

    @SuppressWarnings("rawtypes")
    public static final Map<Class, Function<IndependentFileHandle, ?>> LOADER_MAP = new HashMap<>();

    public static <T> void registerLoadingMethod(Class<T> type, Function<IndependentFileHandle, T> loadingMethod) {
        LOADER_MAP.put(type, loadingMethod);
    }

    public static RawAssetLoader.RawFileHandle loadAsset(Identifier identifier) {
        RawAssetLoader.RawFileHandle handle = RawAssetLoader.getLowLevelRelativeAssetErrors(SaveLocation.getSaveFolder(), "/mods/" + identifier.getNamespace() + "/" + identifier.getName(), false);
        if (handle != null) return handle;
        handle = RawAssetLoader.getLowLevelClassPathAssetErrors("assets/" + identifier.getNamespace() + "/" + identifier.getName(), false);
        if (handle != null) return handle;
        handle = RawAssetLoader.getLowLevelClassPathAssetErrors(identifier.getNamespace() + "/" + identifier.getName(), false);
        if (handle == null) {
            LOGGER.error("Cannot find resource {}", identifier);
            return null;
        }
        return handle;
    }

    public static IndependentFileHandle loadAsset2(Identifier identifier) {
        RawAssetLoader.RawFileHandle handle = RawAssetLoader.getLowLevelRelativeAssetErrors(SaveLocation.getSaveFolder(), "/mods/" + identifier.getNamespace() + "/" + identifier.getName(), false);
        if (handle != null) return new IndependentFileHandle(
                handle, SaveLocation.getSaveFolder() + "/" + identifier.getNamespace() + "/" + identifier.getName(), Files.FileType.Absolute
        );
        handle = RawAssetLoader.getLowLevelClassPathAssetErrors("assets/" + identifier.getNamespace() + "/" + identifier.getName(), false);
        if (handle != null) return new IndependentFileHandle(
                handle, "assets/" + identifier.getNamespace() + "/" + identifier.getName(), Files.FileType.Classpath
        );
        handle = RawAssetLoader.getLowLevelClassPathAssetErrors(identifier.getNamespace() + "/" + identifier.getName(), false);
        if (handle == null) {
            LOGGER.error("Cannot find resource {}", identifier);
            return null;
        }
        return new IndependentFileHandle(
                handle, identifier.getNamespace() + "/" + identifier.getName(), Files.FileType.Classpath
        );
    }

    public static <T> T loadResource(Identifier identifier, Class<?> clazz) {
        IndependentFileHandle handle = loadAsset2(identifier);

        if (handle == null) return null;
        if (!LOADER_MAP.containsKey(clazz)) throw new RuntimeException(clazz + " does not have a registered loading method for this class");
        //noinspection unchecked
        return (T) LOADER_MAP.get(clazz).apply(handle);
    }

    public static class IndependentFileHandle extends FileHandle {

        RawAssetLoader.RawFileHandle handle;

        public IndependentFileHandle(RawAssetLoader.RawFileHandle handle, String path, Files.FileType type) {
            this.type = type;
            this.file = new File(path);
            this.handle = handle;
        }

        @Override
        public InputStream read() {
            return new ByteArrayInputStream(handle.getBytes());
        }

        @Override
        public FileHandle parent() {
            return super.parent();
        }

        @Override
        public File file() {
            return this.file;
        }

        public RawAssetLoader.RawFileHandle getHandle() {
            return handle;
        }
    }

}
