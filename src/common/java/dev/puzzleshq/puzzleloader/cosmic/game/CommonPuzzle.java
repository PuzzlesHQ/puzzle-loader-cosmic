package dev.puzzleshq.puzzleloader.cosmic.game;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.InjectedBlockAction;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.Trigger;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.TriggerGroup;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.BlockLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsForeshadowingModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsRedStoneModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected.ExampleConnectedBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.events.block.EventModBlockRegister;
import dev.puzzleshq.puzzleloader.cosmic.game.events.block.EventRegisterBlockEvent;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventRegisterPacket;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.cts.CTSIdentificationPacket;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.cts.CTSModlistPacket;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.stc.STCModlistRequestPacket;
import dev.puzzleshq.puzzleloader.loader.LoaderConstants;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.ModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PostModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PreModInit;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.blocks.MissingBlockStateResult;
import finalforeach.cosmicreach.entities.IDamageSource;
import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.gameevents.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.gameevents.blockevents.BlockEventTrigger;
import finalforeach.cosmicreach.gameevents.blockevents.BlockEvents;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.particles.GameParticleSystem;
import finalforeach.cosmicreach.rendering.GameParticleMesh;
import finalforeach.cosmicreach.util.ArrayUtils;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Zone;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.neoforged.bus.api.SubscribeEvent;
import org.hjson.JsonObject;

import java.io.File;
import java.util.List;

public class CommonPuzzle implements PreModInit, ModInit, PostModInit {

    public static String autoJoinWorldName = null;

    public CommonPuzzle() {
        GameRegistries.COSMIC_EVENT_BUS.register(this);
        GameRegistries.NETWORK_EVENT_BUS.register(this);
    }

    @Override
    public void onInit() {
        loadArgs();

        EventModBlockRegister event = new EventModBlockRegister();
        GameRegistries.COSMIC_EVENT_BUS.post(event);

        for (IModBlock modBlock : event.getBlocks()) {
            BlockLoader.INSTANCE.generate(modBlock);
        }
    }

    @SubscribeEvent
    public void register(EventModBlockRegister event) {
        event.register(new AprilFoolsRedStoneModBlock());
        event.register(new AprilFoolsForeshadowingModBlock());
        event.register(new ExampleConnectedBlock());
    }

    @SubscribeEvent
    public void register(EventRegisterPacket event) {
        event.registerReservedPacket("identification-packet", 9000, CTSIdentificationPacket.class);
        event.registerReservedPacket("modlist-request-packet", 9001, STCModlistRequestPacket.class);
        event.registerReservedPacket("modlist-send-packet", 9002, CTSModlistPacket.class);
    }

    @Override
    public void onPostInit() {
    }

    @Override
    public void onPreInit() {
    }

    public void loadArgs() {
        String[] args = LoaderConstants.CLIConfiguration.COMMAND_LINE_ARGUMENTS;

        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();

        OptionSpec<String> saveLocation = parser.acceptsAll(List.of("save-location", "s")).withOptionalArg()
                .ofType(String.class);

        OptionSpec<String> worldJoin = parser.acceptsAll(List.of("join-world", "jw")).withOptionalArg()
                .ofType(String.class);

        OptionSet set = parser.parse(args);

        if (set.has(saveLocation)) {
            SaveLocation.saveLocationOverride = saveLocation.value(set);
            (new File(SaveLocation.saveLocationOverride)).mkdirs();
        }

        if (set.has(worldJoin)) {
            autoJoinWorldName = worldJoin.value(set);
        }

    }

}
