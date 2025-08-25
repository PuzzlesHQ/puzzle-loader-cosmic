package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.events.zone.EventRegisterZoneGenerator;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(ZoneGenerator.class)
public abstract class MixinZoneGenerator {
    @Shadow
    public static void registerZoneGenerator(ZoneGenerator zoneGenerator) {}

    @Inject(method = "registerZoneGenerators", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/worldgen/ZoneGenerator;registerZoneGenerator(Lfinalforeach/cosmicreach/worldgen/ZoneGenerator;)V", ordinal = 0))
    private static void registerZoneGenerators(CallbackInfo ci) {
        List<Supplier<ZoneGenerator>> zoneGenerators = new ArrayList<>();

        GameRegistries.COSMIC_EVENT_BUS.post(new EventRegisterZoneGenerator(zoneGenerators));

        for (Supplier<ZoneGenerator> generatorSupplier : zoneGenerators) {
            registerZoneGenerator(generatorSupplier.get());
        }

    }
}