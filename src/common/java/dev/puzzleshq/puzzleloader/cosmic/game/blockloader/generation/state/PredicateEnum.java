package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state;

public enum PredicateEnum {

    HAS_BLOCK_STATE_ID("has_block_state_id"),
    HAS_BLOCK_ID("has_block_id"),
    HAS_TAG("has_tag");

    public final String name;

    PredicateEnum(String name) {
        this.name = name;
    }
}
