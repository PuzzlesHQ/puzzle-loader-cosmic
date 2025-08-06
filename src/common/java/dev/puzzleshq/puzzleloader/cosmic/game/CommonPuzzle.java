package dev.puzzleshq.puzzleloader.cosmic.game;

import com.badlogic.gdx.Gdx;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.PostModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.PreModInit;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsForshadowingModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsRedStoneModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.InjectedBlockAction;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.BlockLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.events.OnBlockRegisterEvent;
import dev.puzzleshq.puzzleloader.loader.LoaderConstants;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.io.SaveLocation;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.neoforged.bus.api.SubscribeEvent;

import java.io.File;
import java.util.List;

public class CommonPuzzle implements PreModInit, ModInit, PostModInit {

    public CommonPuzzle() {
        GameRegistries.COSMIC_EVENT_BUS.register(this);
    }

    @Override
    public void onInit() {
        BlockEvents.registerBlockEventAction(InjectedBlockAction.class);

        OnBlockRegisterEvent event = new OnBlockRegisterEvent();
        GameRegistries.COSMIC_EVENT_BUS.post(event);

        for (IModBlock modBlock : event.getBlocks()) {
            BlockLoader.INSTANCE.generate(modBlock);
        }
    }

    @SubscribeEvent
    public void register(OnBlockRegisterEvent event) {
        event.register(new AprilFoolsRedStoneModBlock());
        event.register(new AprilFoolsForshadowingModBlock());
    }

    @Override
    public void onPostInit() {

    }

    @Override
    public void onPreInit() {
    }

}
