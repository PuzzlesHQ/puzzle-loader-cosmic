package com.github.mrzombii;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

class OverlayCreator {
    public static void main(String[] args) throws IOException {
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

        Color CORNER_COLOR = Color.decode("#ed1d25");
        for (int z : iea) {
            BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);

            image.setRGB(0, 0, CORNER_COLOR.getRGB());
            image.setRGB(15, 0, CORNER_COLOR.getRGB());
            image.setRGB(0, 15, CORNER_COLOR.getRGB());
            image.setRGB(15, 15, CORNER_COLOR.getRGB());

            if ((z & 16) != 0) image.setRGB(0, 0, 0);
            if ((z & 32) != 0) image.setRGB(0, 15, 0);
            if ((z & 64) != 0) image.setRGB(15, 0, 0);
            if ((z & 128) != 0) image.setRGB(15, 15, 0);

            File file = new File("src/client/resources/assets/connected-textures/state-testing/overlays/overlay-" + z + ".png");
            ImageIO.write(image, "png", file);
        }
    }

}

class StateMover {
    public static void main(String[] args) {
        int[] states = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        for (int item : states) {
//            File oldFile = new File("src/client/resources/assets/connected-textures/state-testing/states/state-" + item + ".png");
//            File file = new File("src/client/resources/assets/connected-textures/state-testing/states/template/state-" + item + ".png");
            File oldFile = new File("src/client/resources/assets/connected-textures/smooth-glass/states/glass_" + item + ".png");
            File file = new File("src/client/resources/assets/connected-textures/smooth-glass/states/state-" + item + ".png");
            oldFile.renameTo(file);
        }
    }
}

class StateDeclarationMaker {
    public static void main(String[] args) {
        int[] states = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        int[] t = new int[]{0, 16, 32, 64, 128};
        Set<Integer> iea = new HashSet<>();
        Set<Integer> thing = new HashSet<>();

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
                thing.add(item | z);
                System.err.println("connected-textures:state-testing/states/" + item + "/state-" + (item | z) + ".png");
            }
        }

        System.err.println(thing);
    }
}

public class Main {

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

        Color CORNER_COLOR = Color.decode("#ed1d25");
        for (int z : iea) {
            for (int item : states) {
                File file = new File("src/client/resources/assets/connected-textures/state-testing/states/template/state-" + item + ".png");
                FileInputStream stream = new FileInputStream(file);
                BufferedImage image = ImageIO.read(stream);
                stream.close();

                if ((z & 16) == 0) image.setRGB(15, 15, CORNER_COLOR.getRGB());
                if ((z & 32) == 0) image.setRGB(15, 0, CORNER_COLOR.getRGB());
                if ((z & 64) == 0) image.setRGB(0, 15, CORNER_COLOR.getRGB());
                if ((z & 128) == 0) image.setRGB(0, 0, CORNER_COLOR.getRGB());

                new File("src/client/resources/assets/connected-textures/state-testing/states/" + item).mkdir();
                File newFile = new File("src/client/resources/assets/connected-textures/state-testing/states/" + item + "/state-" + (item | z) + ".png");
                ImageIO.write(image, "png", newFile);
            }
        }
    }

}
