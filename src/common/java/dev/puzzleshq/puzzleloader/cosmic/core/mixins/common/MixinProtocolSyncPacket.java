package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import com.badlogic.gdx.utils.ObjectIntMap;
import dev.puzzleshq.puzzleloader.cosmic.game.network.packet.PacketInterceptor;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.packets.meta.ProtocolSyncPacket;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(ProtocolSyncPacket.class)
public abstract class MixinProtocolSyncPacket extends GamePacket {

    @Inject(method = "write", at = @At("HEAD"))
    private void initPacketInterceptor(CallbackInfo ci) {
        PacketInterceptor.init();
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
            if (PacketInterceptor.PUZZLE_RESERVED_PACKET_IDS.contains(packetNamesToIntId.value)) {
                return;
            }
            puzzle_loader_cosmic$nameToIdMap.put(packetNamesToIntId.key, packetNamesToIntId.value);
        });

        return puzzle_loader_cosmic$nameToIdMap;
    }

}