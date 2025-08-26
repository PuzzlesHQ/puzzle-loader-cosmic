package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.events.command.EventRegisterCommand;
import finalforeach.cosmicreach.chat.commands.Command;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Mixin(Command.class)
public class MixinCommand {

    @Unique
    private final static String[] puzzle_loader_cosmic$empty = new String[0];

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        List<Pair<String[], Supplier<Command>>> commandFactories = new ArrayList<>();

        EventRegisterCommand commandEvent = new EventRegisterCommand(commandFactories);
        GameRegistries.COSMIC_EVENT_BUS.post(commandEvent);

        for (Pair<String[], Supplier<Command>> commandFactory : commandFactories) {
            String[] aliases = commandFactory.getKey();
            if (aliases.length > 1) {
                String[] knockoffs = Arrays.copyOfRange(aliases, 1, aliases.length);
                Command.registerCommand(commandFactory.getRight(), aliases[0], knockoffs);
                continue;
            }
            Command.registerCommand(commandFactory.getRight(), aliases[0], puzzle_loader_cosmic$empty);
        }
    }

}
