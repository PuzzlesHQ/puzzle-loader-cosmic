package dev.puzzleshq.puzzleloader.cosmic.game.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import dev.puzzleshq.puzzleloader.loader.util.RawAssetLoader;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IndependentAssetLoader {

    @SuppressWarnings("rawtypes")
    public static final Map<Class, Function<RawAssetLoader.RawFileHandle, ?>> LOADER_MAP = new HashMap<>();

    public static <T> void registerLoadingMethod(Class<T> type, Function<RawAssetLoader.RawFileHandle, T> loadingMethod) {
        LOADER_MAP.put(type, loadingMethod);
    }

    public static RawAssetLoader.RawFileHandle loadAsset(Identifier identifier) {
        RawAssetLoader.RawFileHandle handle = RawAssetLoader.getLowLevelRelativeAsset(SaveLocation.getSaveFolder(), "/" + identifier.getNamespace() + "/" + identifier.getName());
        if (handle != null) return handle;
        handle = RawAssetLoader.getLowLevelClassPathAsset("/assets/" + identifier.getNamespace() + "/" + identifier.getName());
        if (handle != null) return handle;
        handle = RawAssetLoader.getLowLevelClassPathAsset("/" + identifier.getNamespace() + "/" + identifier.getName());
        return handle;
    }

    public static <T> T loadResource(Identifier identifier, Class<?> clazz) {
        RawAssetLoader.RawFileHandle handle = loadAsset(identifier);
        if (handle == null) return null;
        if (!LOADER_MAP.containsKey(clazz)) throw new RuntimeException(clazz + " does not have a registered loading method for this class");
        //noinspection unchecked
        return (T) LOADER_MAP.get(clazz).apply(handle);
    }

}
