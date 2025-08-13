package dev.puzzleshq.puzzleloader.cosmic.game.util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import dev.puzzleshq.puzzleloader.loader.util.RawAssetLoader;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IndependentAssetLoader {

    @SuppressWarnings("rawtypes")
    public static final Map<Class, Function<IndependentFileHandle, ?>> LOADER_MAP = new HashMap<>();

    public static <T> void registerLoadingMethod(Class<T> type, Function<IndependentFileHandle, T> loadingMethod) {
        LOADER_MAP.put(type, loadingMethod);
    }

    public static RawAssetLoader.RawFileHandle loadAsset(Identifier identifier) {
        RawAssetLoader.RawFileHandle handle = RawAssetLoader.getLowLevelRelativeAsset(SaveLocation.getSaveFolder(), "/mods/" + identifier.getNamespace() + "/" + identifier.getName());
        if (handle != null) return handle;
        handle = RawAssetLoader.getLowLevelClassPathAsset("assets/" + identifier.getNamespace() + "/" + identifier.getName());
        if (handle != null) return handle;
        handle = RawAssetLoader.getLowLevelClassPathAsset(identifier.getNamespace() + "/" + identifier.getName());
        return handle;
    }

    public static IndependentFileHandle loadAsset2(Identifier identifier) {
        RawAssetLoader.RawFileHandle handle = RawAssetLoader.getLowLevelRelativeAsset(SaveLocation.getSaveFolder(), "/mods/" + identifier.getNamespace() + "/" + identifier.getName());
        if (handle != null) return new IndependentFileHandle(
                handle, SaveLocation.getSaveFolder() + "/" + identifier.getNamespace() + "/" + identifier.getName(), Files.FileType.Absolute
        );
        handle = RawAssetLoader.getLowLevelClassPathAsset("assets/" + identifier.getNamespace() + "/" + identifier.getName());
        if (handle != null) return new IndependentFileHandle(
                handle, "assets/" + identifier.getNamespace() + "/" + identifier.getName(), Files.FileType.Classpath
        );
        handle = RawAssetLoader.getLowLevelClassPathAsset(identifier.getNamespace() + "/" + identifier.getName());
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
