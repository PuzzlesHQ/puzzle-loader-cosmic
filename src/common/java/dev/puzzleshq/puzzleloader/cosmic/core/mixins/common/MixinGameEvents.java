package dev.puzzleshq.puzzleloader.cosmic.core.mixins.common;

import com.badlogic.gdx.files.FileHandle;
import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.InjectedBlockAction;
import dev.puzzleshq.puzzleloader.cosmic.game.events.block.EventRegisterBlockEvent;
import finalforeach.cosmicreach.gameevents.GameEvents;
import finalforeach.cosmicreach.gameevents.blockevents.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static finalforeach.cosmicreach.gameevents.GameEvents.INSTANCES;
import static finalforeach.cosmicreach.gameevents.GameEvents.JSON;

@Mixin(GameEvents.class)
public class MixinGameEvents {

    private static boolean wasntInitialized = true;

    /**
     * @author Mr-Zombii
     * @reason Allow modification of BlockEvents
     */
    @Overwrite
    public static <G extends GameEvents<?, ?>> G loadGameEventsFromAsset(Class<? extends G> clazz, FileHandle asset) {
        if (wasntInitialized) {
            BlockEvents.registerBlockEventAction(InjectedBlockAction.class);
            wasntInitialized = false;
        }
        try {
            String jsonStr = asset.readString();
            if (clazz == BlockEvents.class) {
                EventRegisterBlockEvent events = new EventRegisterBlockEvent(jsonStr);
                GameRegistries.COSMIC_EVENT_BUS.post(events);
                jsonStr = events.getBlockEventsGenerator().toHJson().toString();
            }
            G gameEvents = JSON.fromJson(clazz, jsonStr);
            INSTANCES.put(gameEvents.stringId, gameEvents);
            return gameEvents;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to read game event from file: " + asset.path(), ex);
        }
    }

}
