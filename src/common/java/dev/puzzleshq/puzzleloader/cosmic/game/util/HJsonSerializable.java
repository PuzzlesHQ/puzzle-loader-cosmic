package dev.puzzleshq.puzzleloader.cosmic.game.util;

import org.hjson.JsonValue;
import org.hjson.Stringify;

public interface HJsonSerializable {

    JsonValue toHJson();
    String toString();

    default String stringify() {
        return toHJson().toString(Stringify.FORMATTED);
    }
}
