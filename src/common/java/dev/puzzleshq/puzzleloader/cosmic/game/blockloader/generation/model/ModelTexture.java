package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model;

import finalforeach.cosmicreach.util.Identifier;

import javax.annotation.Nullable;

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
