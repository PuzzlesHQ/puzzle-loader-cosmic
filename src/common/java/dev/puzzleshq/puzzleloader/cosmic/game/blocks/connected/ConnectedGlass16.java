package dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected;

import finalforeach.cosmicreach.util.Identifier;

public class ConnectedGlass16 extends AbstractConnectedBlock {

    public static final Identifier id = Identifier.of("connected-textures", "smooth-glass-16");

    public ConnectedGlass16() {
        super(id, true);

        shouldCullClearFace = true;
    }

}
