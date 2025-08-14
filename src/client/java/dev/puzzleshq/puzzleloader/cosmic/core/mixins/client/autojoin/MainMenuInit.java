package dev.puzzleshq.puzzleloader.cosmic.core.mixins.client.autojoin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import dev.puzzleshq.puzzleloader.cosmic.game.ClientPuzzle;
import dev.puzzleshq.puzzleloader.cosmic.game.CommonPuzzle;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.LoadingGame;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Mixin(MainMenu.class)
public class MainMenuInit extends GameState {
    @Unique
    private static final Json puzzle_loader_cosmic$JSON = new Json();

    @Unique
    private static boolean puzzle_loader_cosmic$wasLoaded = false;

    @Inject(method = "render", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (CommonPuzzle.autoJoinWorldName != null && !firstFrame && !puzzle_loader_cosmic$wasLoaded) {
            String worldRootLocation = SaveLocation.getAllWorldsSaveFolderLocation();

            File worldInfoFile = new File(worldRootLocation + "/" + CommonPuzzle.autoJoinWorldName + "/worldInfo.json");
            if (worldInfoFile.exists()) {
                String worldFileContents = Gdx.files.absolute(worldInfoFile.getAbsolutePath()).readString(StandardCharsets.UTF_8.name());
                World world = puzzle_loader_cosmic$JSON.fromJson(World.class, worldFileContents);
                world.worldFolderName = CommonPuzzle.autoJoinWorldName;
                LoadingGame.switchToLoadingState();
                Threads.runOnMainThread(() -> GameState.IN_GAME.loadWorld(world.worldFolderName));
            }
            puzzle_loader_cosmic$wasLoaded = true;
        }
    }

}
