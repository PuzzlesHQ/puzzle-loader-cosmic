package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import finalforeach.cosmicreach.gameevents.ActionId;
import finalforeach.cosmicreach.gameevents.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.gameevents.blockevents.actions.IBlockAction;
import finalforeach.cosmicreach.util.logging.AnsiColours;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@ActionId(id = "puzzle:injected_method")
public class InjectedBlockAction implements IBlockAction {

    public static final Map<Long, Consumer<BlockEventArgs>> CONSUMER_MAP = new ConcurrentHashMap<>();

    public long injected_method_id;

    @Override
    public void act(BlockEventArgs blockEventArgs) {
        System.out.println(AnsiColours.BG_RED +  "Executing trigger " + injected_method_id + AnsiColours.RESET);
        CONSUMER_MAP.get(injected_method_id).accept(blockEventArgs);
    }

}
