package dev.puzzleshq.puzzleloader.cosmic.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Unique;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PuzzleIcons {

    @Unique
    private static final int[] iconSizes = {16, 32, 64, 128, 256};

    public static long window = 0L;

    static GLFWImage.Buffer imageBuf;
    public static void disposeIcon() {
        imageBuf.free();
    }

    public static void setIcon() {
        GLFW.glfwSetWindowIcon(window, imageBuf);
    }

    @Unique
    public static void loadIcons() {
        try {
            imageBuf = GLFWImage.malloc(iconSizes.length);
            for (int i = 0; i < iconSizes.length; i++) {
                int size = iconSizes[i];

                InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/puzzle-loader-core/icons/puzzle-loader-icon-" + size + ".png");
                assert stream != null;
                BufferedImage image = ImageIO.read(stream);
                stream.close();

                int width = image.getWidth();
                int height = image.getHeight();

                byte[] iconData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
                ByteBuffer byteBuffer = MemoryUtil.memAlloc(iconData.length);
                byteBuffer.order(ByteOrder.nativeOrder());
                for (int idx = 0; idx < iconData.length; idx += 4) {
                    byteBuffer.put((iconData[idx + 3]));
                    byteBuffer.put((iconData[idx + 2]));
                    byteBuffer.put((iconData[idx + 1]));
                    byteBuffer.put(iconData[idx]);
                }
                byteBuffer.flip();
                image.flush();
                imageBuf.position(i);
                imageBuf.width(width);
                imageBuf.height(height);
                imageBuf.pixels(byteBuffer);
            }
            imageBuf.flip();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
