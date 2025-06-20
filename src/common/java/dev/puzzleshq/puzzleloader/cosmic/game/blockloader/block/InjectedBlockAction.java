package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.blockevents.actions.ActionId;
import finalforeach.cosmicreach.blockevents.actions.IBlockAction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@ActionId(id = "puzzle:injected_method")
public class InjectedBlockAction implements IBlockAction {

    public static final Map<Long, Consumer<BlockEventArgs>> CONSUMER_MAP = new ConcurrentHashMap<>();

    public long injected_method_id;

    @Override
    public void act(BlockEventArgs blockEventArgs) {
        CONSUMER_MAP.get(injected_method_id).accept(blockEventArgs);
    }

}
