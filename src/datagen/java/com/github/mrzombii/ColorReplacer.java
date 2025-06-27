package com.github.mrzombii;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ColorReplacer {

    public static void main(String[] args) throws IOException {
        int[] states = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        int[] t = new int[]{0, 16, 32, 64, 128};
        Set<Integer> iea = new HashSet<>();

        for (int j : t) {
            for (int k : t) {
                for (int element : t) {
                    for (int item : t) {
                        for (int value : t) {
                            iea.add(j | k | element | item | value);
                        }
                    }
                }
            }
        }

        for (int z : iea) {
            for (int item : states) {
                File file = new File("src/client/resources/assets/connected-textures/connected-block-256/states/" + item + "/state-" + (item | z) + ".png");
                FileInputStream stream = new FileInputStream(file);
                BufferedImage image = ImageIO.read(stream);
                stream.close();

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        int color = image.getRGB(x, y);
                        if (color == 0xFFED1C24) image.setRGB(x, y, Color.WHITE.getRGB());
                        else if (color == 0xFF880015) image.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }

                ImageIO.write(image, "png", file);
            }
        }
    }

}
