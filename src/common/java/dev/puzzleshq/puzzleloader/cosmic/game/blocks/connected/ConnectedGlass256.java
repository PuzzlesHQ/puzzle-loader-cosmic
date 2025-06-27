package dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected;

import finalforeach.cosmicreach.util.Identifier;

public class ConnectedGlass256 extends AbstractConnectedBlock {

    public static final Identifier id = Identifier.of("connected-textures", "smooth-glass-256");

    public ConnectedGlass256() {
        super(id, false);

        shouldCullClearFace = true;
    }

}
