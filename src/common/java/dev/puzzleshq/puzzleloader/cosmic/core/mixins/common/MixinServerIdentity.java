package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import com.github.villadora.semver.Version;
import dev.puzzleshq.puzzleloader.cosmic.game.network.api.IServerIdentity;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerIdentity.class)
public class MixinServerIdentity implements IServerIdentity {

    @Unique
    String puzzleLoader$name;
    @Unique
    boolean puzzleLoader$isModded;

    @Unique
    Map<String, Version> puzzle_loader_cosmic$modList = new HashMap<>();

    @Override
    public void setModdedState(String clientName, boolean isModded) {
        puzzleLoader$name = clientName;
        puzzleLoader$isModded = isModded;
    }

    public void setModList(Map<String, Version> modList) {
        this.puzzle_loader_cosmic$modList = modList;
    }

    public Map<String, Version> getModList() {
        return puzzle_loader_cosmic$modList;
    }

    @Override
    public boolean isModded() {
        return puzzleLoader$isModded;
    }

    @Override
    public String getClientName() {
        return puzzleLoader$name;
    }
}