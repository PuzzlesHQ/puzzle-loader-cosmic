package dev.puzzleshq.puzzleloader.cosmic.game.network.api;

import com.github.zafarkhaja.semver.Version;

import java.util.Map;

public interface IServerIdentity {

    void setModdedState(String clientName, boolean isModded);
    boolean isModded();
    String getClientName();

    void setModList(Map<String, Version> modList);
    Map<String, Version> getModList();

}