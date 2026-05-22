package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.interfaces.impl;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.interfaces.IModelCuboid;
import org.hjson.JsonValue;

public class StaticModelCuboid implements IModelCuboid {

    private final Vector3 min = new Vector3();
    private final Vector3 max = new Vector3(16, 16, 16);

    public StaticModelCuboid() {}

    @Override
    public JsonValue toHJson() {
        return null;
    }
}
