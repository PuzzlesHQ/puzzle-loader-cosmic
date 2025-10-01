package dev.puzzleshq.puzzleloader.cosmic.game.network.packet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventPacketBucketReceiveIntercept;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventPacketReceiveIntercept;
import dev.puzzleshq.puzzleloader.cosmic.game.events.net.EventRegisterPacket;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.util.logging.AnsiColours;
import net.neoforged.bus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PacketInterceptor {

    public static final int PUZZLE_PACKET_RANGE_FIRST = 9000;
    public static final int PUZZLE_PACKET_RANGE_LAST = 9999;

    static Logger LOGGER = LogManager.getLogger("PacketInterceptor");

    public static Set<Integer> PUZZLE_RESERVED_PACKET_IDS = new HashSet<>();

    public static int INTERCEPTED_PACKET_COUNT = 0;
    public static PacketInterceptor INSTANCE;

    public PacketInterceptor() {
        INSTANCE = this;
        GameRegistries.NETWORK_EVENT_BUS.register(INSTANCE);
    }

    public static <T extends GamePacket> void registerPacketLazy(Class<T> packetClass) {
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

            int numId = idsToPackets().size + 1;
            idsToPackets().put(numId, supplier);
            packetsToIntIds().put(packetClass, numId);
            packetNamesToIntIds().put(packetClass.getName(), numId);
            packetNamesToClasses().put(packetClass.getName(), packetClass);
            LOGGER.info("Registered {}Lazy{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_GREEN, AnsiColours.RESET, AnsiColours.BLUE, packetClass.getName(), AnsiColours.RESET, AnsiColours.PURPLE, numId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends GamePacket> void registerPacketLazy(String strId, Class<T> packetClass) {
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

            int numId = idsToPackets().size + 1;
            idsToPackets().put(numId, supplier);
            packetsToIntIds().put(packetClass, numId);
            packetNamesToIntIds().put(strId, numId);
            packetNamesToClasses().put(strId, packetClass);
            LOGGER.info("Registered {}Lazy{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_GREEN, AnsiColours.RESET, AnsiColours.BLUE, strId, AnsiColours.RESET, AnsiColours.PURPLE, numId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends GamePacket> void registerPacket(String strId, int numId, Class<T> packetClass) {
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
            LOGGER.info("Registered {}Regular{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_YELLOW, AnsiColours.RESET, AnsiColours.BLUE, strId, AnsiColours.RESET, AnsiColours.PURPLE, numId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends GamePacket> void registerReservedPacket(String strId, int numId, Class<T> packetClass) {
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
            LOGGER.info("Registered {}Reserved{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_PURPLE, AnsiColours.RESET, AnsiColours.BLUE, strId, AnsiColours.RESET, AnsiColours.PURPLE, numId);

            PUZZLE_RESERVED_PACKET_IDS.add(numId);
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
//
//        switch (packetClassName) {
//            case "finalforeach.cosmicreach.networking.packets.MessagePacket":
//                MessagePacket messagePacket = (MessagePacket) packet;
//                System.out.println("Recived Msg: " + messagePacket.message + "from: " + messagePacket.playerUniqueId);
//                break;
//            default:
//                break;
//        }

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

}