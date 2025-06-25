package com.github.mrzombii;

import java.io.File;

public class FastFileMover {
    public static void main(String[] args) {
        int[] states = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        for (int item : states) {
            File oldFile = new File("src/client/resources/assets/connected-textures/smooth-glass/states/glass_" + item + ".png");
            File file = new File("src/client/resources/assets/connected-textures/smooth-glass/states/state-" + item + ".png");
            oldFile.renameTo(file);
        }
    }
}
