package dev.puzzleshq.puzzleloader.cosmic.game;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.PostModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.PreModInit;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsForshadowingModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsRedStoneModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.AutomatedModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.InjectedBlockAction;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.TriggerGroup;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.BlockLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected.*;
import dev.puzzleshq.puzzleloader.cosmic.game.events.OnBlockRegisterEvent;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import org.hjson.JsonValue;

public class CommonPuzzle implements PreModInit, ModInit, PostModInit {

    public CommonPuzzle() {
        GameRegistries.COSMIC_EVENT_BUS.register(this);
    }

    @Override
    public void onInit() {
        System.err.println("CommonPuzzle init called");

        BlockEvents.registerBlockEventAction(InjectedBlockAction.class);

        OnBlockRegisterEvent event = new OnBlockRegisterEvent();
        GameRegistries.COSMIC_EVENT_BUS.post(event);

        for (IModBlock modBlock : event.getBlocks()) {
            Block block = BlockLoader.INSTANCE.generate(modBlock);
            System.err.println("Generated Block - " + block.getStringId());
        }

        ISidedBlockConnector.getInstance().registerStateAsConnectedBlock(Block.getInstance("glass").getDefaultBlockState(), new ConnectedGlass256());
    }

    @SubscribeEvent
    public void register(OnBlockRegisterEvent event) {
        event.register(new AprilFoolsRedStoneModBlock());
        event.register(new AprilFoolsForshadowingModBlock());
        event.register(new ConnectedBlock16());
        event.register(new ConnectedBlock256());
    }

    @Override
    public void onPostInit() {
        System.err.println("CommonPuzzle postInit called");
    }

    @Override
    public void onPreInit() {
        System.err.println("CommonPuzzle preInit called");
    }

}
