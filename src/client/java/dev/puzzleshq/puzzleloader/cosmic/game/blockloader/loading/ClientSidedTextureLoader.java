package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import dev.puzzleshq.puzzleloader.loader.util.ReflectionUtil;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonTexture;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;

public class ClientSidedTextureLoader implements ISidedTextureLoader {

    public static Pixmap getAtlasPixmap() {
        try {
            return (Pixmap) ReflectionUtil.getField(ChunkShader.class, "allBlocksPix").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getX() {
        try {
            return (int) ReflectionUtil.getField(ChunkShader.class, "terrainPixCurX").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getY() {
        try {
            return (int) ReflectionUtil.getField(ChunkShader.class, "terrainPixCurY").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setX(int i) {
        try {
            ReflectionUtil.getField(ChunkShader.class, "terrainPixCurX").set(null, i);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setY(int i) {
        try {
            ReflectionUtil.getField(ChunkShader.class, "terrainPixCurY").set(null, i);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static float[] addTextureToAtlas(Pixmap pixmap) {
        float[] uv = addTextureToAtlas(pixmap, getX(), getY());

        setX(getX() + pixmap.getWidth());
        if ((float)getX() > (float)(getAtlasPixmap().getWidth() * 15) / 16.0F) {
            setX(0);
            setY(getY() + pixmap.getHeight());
        }

        return uv;
    }

    public static float[] addTextureToAtlas(BlockModelJsonTexture tex, boolean setUv) {
        Texture texture = GameAssetLoader.getTexture(tex.fileName);
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        float[] uv = addTextureToAtlas(pixmap, getX(), getY());

        setX(getX() + pixmap.getWidth());
        if ((float)getX() > (float)(getAtlasPixmap().getWidth() * 15) / 16.0F) {
            setX(0);
            setY(getY() + pixmap.getHeight());
        }
        texture.dispose();
        if (setUv) tex.uv = uv;

        return uv;
    }

    public static float[] addTextureToAtlas(Pixmap pixmap, int x, int y) {
        Pixmap atlas = getAtlasPixmap();

        getAtlasPixmap().drawPixmap(pixmap, x, y);

        return new float[]{
                /* MIN_U */ (float) x / atlas.getWidth(),
                /* MIN_V */ (float) y / atlas.getHeight(),
                /* NAX_U */ (float) (x + pixmap.getWidth()) / atlas.getWidth(),
                /* MAX_V */ (float) (y + pixmap.getHeight()) / atlas.getHeight(),
        };
    }

}
