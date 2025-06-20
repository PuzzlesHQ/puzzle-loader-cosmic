package dev.puzzleshq.puzzleloader.cosmic.core.crash;

import javax.swing.*;
import java.awt.*;

public class BetterCrashScreen extends JFrame {

    public BetterCrashScreen() {}

    public static void fromDialog(JDialog dialog) {
        BetterCrashScreen screen = new BetterCrashScreen();
        screen.setTitle(dialog.getTitle());
        for (Component component : dialog.getComponents()) {
            screen.add(component);
        }
        screen.setVisible(true);
    }

}
