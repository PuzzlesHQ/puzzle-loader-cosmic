package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.enhanced;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelPlane;

import java.util.List;

public class ModelPartGroup {
    private boolean isVisible = true;
    public List<ModelCuboid> cuboids;
    public List<ModelPlane> planes;
    public String name;
    public String parentName;
    private final EnhancedBlockModelGenerator generator;
    private final Vector3 pivot = new Vector3();
    private final Vector3 rotation = new Vector3();

    public ModelPartGroup(
            EnhancedBlockModelGenerator generator,
            String parentName, String name,
            List<ModelCuboid> cuboids,
            List<ModelPlane> planes
    ) {
        this.cuboids = cuboids;
        this.planes = planes;
        this.name = name;
        this.parentName = parentName;
        this.generator = generator;
        setPivot(pivot);
        setRotation(rotation);
    }

    public Vector3 getPivot() {
        return pivot;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public ModelPartGroup setPivot(Vector3 pivot) {
        this.pivot.set(pivot);
        return this;
    }

    public ModelPartGroup setRotation(Vector3 rotation) {
        this.rotation.set(rotation);
        return this;
    }

    public ModelPartGroup setPivot(float x, float y, float z) {
        this.pivot.set(new Vector3(x, y, z));
        return this;
    }

    public ModelPartGroup setRotation(float x, float y, float z) {
        this.rotation.set(new Vector3(x, y, z));
        return this;
    }

    public List<ModelCuboid> getCuboids() {
        return cuboids;
    }

    public List<ModelPlane> getPlanes() {
        return planes;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        if (this.parentName != null && generator.groups.containsKey(this.parentName)) {
            return getParent().isVisible() && isVisible;
        }
        return isVisible;
    }

    public ModelPartGroup getParent() {
        return generator.groups.get(this.parentName);
    }
}