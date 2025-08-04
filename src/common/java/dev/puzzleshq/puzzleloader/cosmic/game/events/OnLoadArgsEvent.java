package dev.puzzleshq.puzzleloader.cosmic.game.events;

import net.neoforged.bus.api.Event;

public class OnLoadArgsEvent extends Event {

    String[] args;

    public OnLoadArgsEvent(String[] args){
        this.args = args.clone();
    }

    public String[] getArgs() {
        return args;
    }

}
