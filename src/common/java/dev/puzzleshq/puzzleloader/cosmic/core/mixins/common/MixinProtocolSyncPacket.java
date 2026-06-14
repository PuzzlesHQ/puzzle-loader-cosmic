package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import dev.puzzleshq.puzzleloader.cosmic.game.network.api.IServerIdentity;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.PacketInterceptor;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.packets.meta.ProtocolSyncPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.collection.IntObjectMap;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Mixin(ProtocolSyncPacket.class)
public abstract class MixinProtocolSyncPacket extends GamePacket {

    @Shadow
    @Final
    public Array<ProtocolSyncPacket.PacketClassRegistration> recievedRegistrations;

    @Inject(method = "write", at = @At("HEAD"))
    private void initPacketInterceptor(CallbackInfo ci) {
        PacketInterceptor.init();
    }

    @Unique
    boolean puzzle_loader_cosmic$is_modded = false;
    @Unique
    String puzzle_loader$client_name = "vanilla";

    @Inject(method = "receive", at = @At("TAIL"))
    private void dataChecking(ByteBuf in, CallbackInfo ci) {
        puzzle_loader_cosmic$is_modded = false;
        puzzle_loader$client_name = "vanilla";
        if (in.readableBytes() != 0) {
            puzzle_loader_cosmic$is_modded = readBoolean(in);
            puzzle_loader$client_name = readString(in);

            int count = readInt(in);
            for (int i = 0; i < count; i++) {
                String name = readString(in);
                int id = readInt(in);
                boolean compressed = readBoolean(in);
                Class<? extends GamePacket> packetClass = packetNamesToClasses.get(name);

                if (packetClass == null) {
                    throw new RuntimeException("Missing packet: " + name);
                }

                if (id == 0) {
                    throw new RuntimeException("No packet can have an id of 0.");
                }

                if ((id == 1) ^ (packetClass == ProtocolSyncPacket.class)) {
                    throw new RuntimeException("Only ProtocolSyncPacket can have an id of 1.");
                }

                recievedRegistrations.add(new ProtocolSyncPacket.PacketClassRegistration(name, id, packetClass, compressed));
            }
        }
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void dataWrite(CallbackInfo ci) {
        writeBoolean(true);
        writeString("puzzle-loader");

        writeInt(PacketInterceptor.PUZZLE_ID_TO_PACKET_NAME.size());
        for (IntObjectMap.PrimitiveEntry<String> entry : PacketInterceptor.PUZZLE_ID_TO_PACKET_NAME.entries()) {
            writeString(entry.value());
            writeInt(entry.key());
            boolean compressed = false;
            Supplier<? extends GamePacket> supplier = idsToPackets.get(entry.key());
            if (supplier != null) {
                GamePacket instance =supplier.get();
                if (instance != null) {
                    compressed = instance.shouldCompress();
                }
            }
            writeBoolean(compressed);
        }
    }

    @Unique
    private final ObjectIntMap<String> puzzle_loader_cosmic$nameToIdMap = new ObjectIntMap<>();
    @Unique
    private final AtomicBoolean puzzle_loader_cosmic$isLoaded = new AtomicBoolean(false);

    @Redirect(method = "write", at = @At(value = "FIELD", target = "Lfinalforeach/cosmicreach/networking/packets/meta/ProtocolSyncPacket;packetNamesToIntIds:Lcom/badlogic/gdx/utils/ObjectIntMap;", opcode = Opcodes.GETSTATIC))
    private ObjectIntMap<String> test() {
        if (puzzle_loader_cosmic$isLoaded.get()) {
            return puzzle_loader_cosmic$nameToIdMap;
        }

        puzzle_loader_cosmic$isLoaded.set(true);

        puzzle_loader_cosmic$nameToIdMap.clear();
        packetNamesToIntIds.forEach((packetNamesToIntId) -> {
            if (PacketInterceptor.PUZZLE_ID_TO_PACKET_NAME.containsKey(packetNamesToIntId.value)) {
                return;
            }
            puzzle_loader_cosmic$nameToIdMap.put(packetNamesToIntId.key, packetNamesToIntId.value);
        });

        return puzzle_loader_cosmic$nameToIdMap;
    }

    @Inject(method = "handle", at = @At("HEAD"))
    private void handlePacket(NetworkIdentity identity, ChannelHandlerContext ctx, CallbackInfo ci) {
        switch (identity.getSide()) {
            case CLIENT -> {
                PacketInterceptor.LOGGER.log(Level.INFO, "Joined \"{}\" Server", puzzle_loader$client_name);
            }
            case SERVER -> {
                PacketInterceptor.LOGGER.log(Level.INFO, "\"{}\" Client Joined", puzzle_loader$client_name);
                ((IServerIdentity)identity).setModdedState(
                        puzzle_loader$client_name,
                        puzzle_loader_cosmic$is_modded
                );
            }
        }
    }

}