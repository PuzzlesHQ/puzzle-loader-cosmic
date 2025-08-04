package dev.puzzleshq.puzzleloader.cosmic.game;

import com.badlogic.gdx.Gdx;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.PostModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.PreModInit;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsForshadowingModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blocks.aprilfools.AprilFoolsRedStoneModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.InjectedBlockAction;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.BlockLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.events.OnBlockRegisterEvent;
import dev.puzzleshq.puzzleloader.cosmic.game.events.OnLoadArgsEvent;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.io.SaveLocation;
import net.neoforged.bus.api.SubscribeEvent;

import java.io.File;

public class CommonPuzzle implements PreModInit, ModInit, PostModInit {

    public static String autoJoinWorldName = null;

    public CommonPuzzle() {
        GameRegistries.COSMIC_EVENT_BUS.register(this);
    }

    @Override
    public void onInit() {
        BlockEvents.registerBlockEventAction(InjectedBlockAction.class);

        OnBlockRegisterEvent event = new OnBlockRegisterEvent();
        GameRegistries.COSMIC_EVENT_BUS.post(event);

        for (IModBlock modBlock : event.getBlocks()) {
            BlockLoader.INSTANCE.generate(modBlock);
        }
    }

    @SubscribeEvent
    public void register(OnBlockRegisterEvent event) {
        event.register(new AprilFoolsRedStoneModBlock());
        event.register(new AprilFoolsForshadowingModBlock());
    }

    @SubscribeEvent
    public void loadArgs(OnLoadArgsEvent event) {
        String[] args = event.getArgs();
        for(int i = 0; i < args.length; ++i) {
            String arg = args[i];

            if (arg.equals("-s") || arg.equals("--save-location")) {
                SaveLocation.saveLocationOverride = args[i + 1];
                (new File(SaveLocation.saveLocationOverride)).mkdirs();
            }

            if (arg.equals("-wt") || arg.equals("--window-title")){
                Gdx.graphics.setTitle(args[i + 1]);
            }

            if (arg.equals("-ws") || arg.equals("--window-size")){
                int w = 0;
                int h = 0;
                if (args[i + 1].contains("x")){
                   String[] strings =  args[i + 1].split("x");
                   w = Integer.parseInt(strings[0]);
                   h = Integer.parseInt(strings[1]);
                }else if (i + 2 < args.length) {
                    int newW = 0;
                    int newH = 0;
                    try {
                        newW = Integer.parseInt(args[i + 1]);
                        newH = Integer.parseInt(args[i + 2]);
                    } catch (NumberFormatException ignored) {
                    }
                    w = newW;
                    h = newH;
                }
                if (w != 0 || h != 0) {
                    Gdx.graphics.setWindowedMode(w, h);
                }
            }else if (arg.equals("-fs") || arg.equals("--fullscreen")){
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else if (arg.equals("-m") || arg.equals("--maximized ")){
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
            if (arg.equals("-jw") || arg.equals("--join-world")){
                autoJoinWorldName = args[i + 1];
            }
        }
    }

    @Override
    public void onPostInit() {
    }

    @Override
    public void onPreInit() {
    }

}
