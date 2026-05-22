package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model;

import dev.puzzleshq.annotation.documentation.Note;
import finalforeach.cosmicreach.util.Identifier;

import javax.annotation.Nullable;

@Note("This class is subject to change as I refactor the block model package")
public class ModelTexture {

    private Identifier regularTexture;
    private Identifier emissiveTexture;

    public ModelTexture(Identifier regularTexture, Identifier emissiveTexture) {
        this.regularTexture = regularTexture;
        this.emissiveTexture = emissiveTexture;
    }

    public ModelTexture(Identifier regularTexture) {
        this.regularTexture = regularTexture;
        this.emissiveTexture = null;
    }

    public void setRegularTexture(Identifier regularTexture) {
        this.regularTexture = regularTexture;
    }

    public void setEmissiveTexture(Identifier emissiveTexture) {
        this.emissiveTexture = emissiveTexture;
    }

    public Identifier getRegularTexture() {
        return regularTexture;
    }

    public @Nullable Identifier getEmissiveTexture() {
        return emissiveTexture;
    }

    @Override
    public String toString() {
        return "ModelTexture{" +
                "regularTexture=" + regularTexture +
                ", emissiveTexture=" + emissiveTexture +
                '}';
    }
}
