package dev.puzzleshq.puzzleloader.cosmic.game.network.packet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import dev.puzzleshq.mod.api.IModContainer;
import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventPacketBucketReceiveIntercept;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventPacketReceiveIntercept;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventRegisterPacket;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods.EventMissingMod;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods.EventModVersionMismatch;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.mods.EventReceiveClientModList;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.packets.PacketModListExchange;
import dev.puzzleshq.puzzleloader.loader.util.ModFinder;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.singletons.GameSingletonPlayers;
import finalforeach.cosmicreach.util.logging.AnsiColours;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.neoforged.bus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class PacketInterceptor {

    public static final int PUZZLE_PACKET_RANGE_FIRST = 9000;
    public static final int PUZZLE_PACKET_RANGE_LAST = 9999;

    public static Logger LOGGER = LogManager.getLogger("PacketInterceptor");

    public static int INTERCEPTED_PACKET_COUNT = 0;
    public static PacketInterceptor INSTANCE;

    public PacketInterceptor() {
        INSTANCE = this;
        GameRegistries.NETWORK_EVENT_BUS.register(INSTANCE);
        GameRegistries.COSMIC_EVENT_BUS.register(INSTANCE);
    }

    public static final IntObjectMap<String> PUZZLE_ID_TO_PACKET_NAME = new IntObjectHashMap<>();
    public static final IntObjectMap<Class<? extends GamePacket>> PUZZLE_ID_TO_PACKET = new IntObjectHashMap<>();

    public static <T extends GamePacket> int registerPacketLazy(Class<T> packetClass) {
        return PacketInterceptor.registerPacketLazy(packetClass.getName(), packetClass);
    }

    public static <T extends GamePacket> int registerPacketLazy(String strId, Class<T> packetClass) {
        int numId = idsToPackets().size + 1;
        return PacketInterceptor.registerPacket(strId, numId, packetClass);
    }

    public static <T extends GamePacket> int registerPacket(String strId, int numId, Class<T> packetClass) {
        try {
            Constructor<T> packetConstructor = packetClass.getDeclaredConstructor();
            Supplier<T> supplier = () -> {
                try {
                    return (T) packetConstructor.newInstance();
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    return null;
                }
            };
            idsToPackets().put(numId, supplier);
            packetsToIntIds().put(packetClass, numId);
            packetNamesToIntIds().put(strId, numId);
            packetNamesToClasses().put(strId, packetClass);
            LOGGER.info("Registered {}Puzzle{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_YELLOW, AnsiColours.RESET, AnsiColours.BLUE, strId, AnsiColours.RESET, AnsiColours.PURPLE, numId);
            PUZZLE_ID_TO_PACKET_NAME.put(numId, strId);
            PUZZLE_ID_TO_PACKET.put(numId, packetClass);
            return numId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static IntMap<Supplier<? extends GamePacket>> idsToPackets() {
        try {
            Field field = GamePacket.class.getDeclaredField("idsToPackets");
            field.setAccessible(true);
            return (IntMap<Supplier<? extends GamePacket>>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectIntMap<Class<? extends GamePacket>> packetsToIntIds() {
        try {
            Field field = GamePacket.class.getDeclaredField("packetsToIntIds");
            field.setAccessible(true);
            return (ObjectIntMap<Class<? extends GamePacket>>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectIntMap<String> packetNamesToIntIds() {
        try {
            Field field = GamePacket.class.getDeclaredField("packetNamesToIntIds");
            field.setAccessible(true);
            return (ObjectIntMap<String>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMap<String, Class<? extends GamePacket>> packetNamesToClasses() {
        try {
            Field field = GamePacket.class.getDeclaredField("packetNamesToClasses");
            field.setAccessible(true);
            return (ObjectMap<String, Class<? extends GamePacket>>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        if (INSTANCE != null)
            new PacketInterceptor();
    }

    public void modifyPacket(GamePacket packet) {
        INTERCEPTED_PACKET_COUNT++;
        short packetID = packet.packetID;
        String packetClassName = packet.getClass().getName();
    }

    @SubscribeEvent
    public void onEvent(EventPacketReceiveIntercept packetSingle) {
        modifyPacket(packetSingle.getPacket());
    }

    @SubscribeEvent
    public void onEvent(EventPacketBucketReceiveIntercept packetBucket) {
        Array<GamePacket> bucket = packetBucket.getPacketBucket();
        for (int i = 0; i < bucket.size; i++) {
            modifyPacket(bucket.get(i));
        }
    }

    public static void callRegisterPacket() {
        GameRegistries.NETWORK_EVENT_BUS.post(new EventRegisterPacket());
    }

    public void onEvent(EventReceiveClientModList event) {
        Set<PacketModListExchange.MiniModInfo> playerMods = event.getModList();

        AtomicBoolean clientSided = new AtomicBoolean(false);
        AtomicBoolean serverSided = new AtomicBoolean(false);

        StringBuilder builder = new StringBuilder();
        builder.append("Some mods have been found missing or mismatched!");

        List<IModContainer> serverMods = ModFinder.getModsArray();
        boolean causeKick = false;

        for (IModContainer serverMod : serverMods) {
            clientSided.set(false);
            serverSided.set(false);

            Map<String, Boolean> sides = serverMod.getInfo().getLoadableSides();
            sides.forEach((sid, isLoadable) -> {
                if (sid.equalsIgnoreCase("client")) clientSided.set(true);
                if (sid.equalsIgnoreCase("server")) serverSided.set(true);
            });

            if (clientSided.get() && serverSided.get()) {
                Optional<PacketModListExchange.MiniModInfo> matchingModInfo = playerMods.stream().filter(m -> m.worksClientSide() && m.worksServerSide() && m.modId().equals(serverMod.getID())).findFirst();
                if (matchingModInfo.isEmpty()) {
                    builder.append("\n- Missing Mod: ").append(serverMod.getID());
                    EventMissingMod missingMod = new EventMissingMod(serverMod.getID());
                    GameRegistries.COSMIC_EVENT_BUS.post(missingMod);
                    causeKick |= missingMod.doesKickPlayer();

                    continue;
                }
                if (!matchingModInfo.get().modVersion().equals(serverMod.getInfo().getVersion())) {
                    builder.append("\n- Version Mismatch: ").append(serverMod.getInfo().getVersion());
                    EventModVersionMismatch versionMismatch = new EventModVersionMismatch(
                            serverMod.getID(),
                            serverMod.getVersionStr(),
                            matchingModInfo.get().modVersion()
                    );
                    GameRegistries.COSMIC_EVENT_BUS.post(versionMismatch);
                    causeKick |= versionMismatch.doesKickPlayer();
                }
            }

            ServerIdentity c = ServerSingletons.getConnection(GameSingletonPlayers.getPlayerFromAccount(event.getPlayerAccount()));
            if (causeKick) {
                ServerSingletons.SERVER.kick(builder.toString(), c);
            } else {
                c.sendChatMessage(builder.toString());
            }
        }
    }

}