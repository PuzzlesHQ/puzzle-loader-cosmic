package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

public @interface BlockEventsTriggerInjection {

    String triggerGroup();

    /* -1 is bottom */
    int injectionPoint() default -1;

}
