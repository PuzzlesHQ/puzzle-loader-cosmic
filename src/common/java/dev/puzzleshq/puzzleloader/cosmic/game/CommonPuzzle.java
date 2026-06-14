package dev.puzzleshq.puzzleloader.cosmic.game;

import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsForeshadowingModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsRedStoneModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected.ExampleConnectedBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.events.block.EventModBlockRegister;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventRegisterPacket;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.packets.PacketModListExchange;
import dev.puzzleshq.puzzleloader.loader.LoaderConfig;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.ModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PostModInit;
import dev.puzzleshq.puzzleloader.loader.mod.entrypoint.common.PreModInit;
import finalforeach.cosmicreach.util.SaveLocation;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.neoforged.bus.api.SubscribeEvent;

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
    }

    @SubscribeEvent
    public void register(EventModBlockRegister event) {
        event.register(new AprilFoolsRedStoneModBlock());
        event.register(new AprilFoolsForeshadowingModBlock());
        event.register(new ExampleConnectedBlock());
    }

    @SubscribeEvent
    public void register(EventRegisterPacket event) {
        event.registerPacket("modlist-exchange-packet", 9000, PacketModListExchange.class);
    }

    @Override
    public void onPostInit() {

    }

    @Override
    public void onPreInit() {
    }

    public void loadArgs() {
        String[] args = LoaderConfig.COMMAND_LINE_ARGUMENTS;

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
