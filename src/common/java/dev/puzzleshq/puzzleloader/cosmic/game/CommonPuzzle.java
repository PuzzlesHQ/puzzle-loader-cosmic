package dev.puzzleshq.puzzleloader.cosmic.game;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.ModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.PostModInit;
import dev.puzzleshq.puzzleloader.cosmic.core.modInitialises.PreModInit;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.AutomatedModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.TriggerGroup;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.BlockLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.CommonSidedModelLoader;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import dev.puzzleshq.puzzleloader.loader.LoaderConstants;
import dev.puzzleshq.puzzleloader.loader.util.EnvType;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonValue;

public class CommonPuzzle implements PreModInit, ModInit, PostModInit {

    public static void main(String[] args) {
        AutomatedModBlock modBlock = new AutomatedModBlock(Identifier.of("base", "laser_switch2"));

        {
            BlockGenerator generator = modBlock.getGenerator();
            State defaultStateParams = generator.getDefaultProperties();
            defaultStateParams.blockEventId = Identifier.of("base", "block_events_laser_switch2");
            defaultStateParams.tags.add("tool_pickaxe_effective");
            defaultStateParams.placementRules = "omnidirectional_towards";
            defaultStateParams.allowsStateSwapping.set(false);
            defaultStateParams.isCatalogHidden.set(true);
            defaultStateParams.stateGenerators.add("base:laser_switch_power_off");
            defaultStateParams.dropParameters.put("direction", JsonValue.valueOf("NegZ"));

            State s = generator.createState("power=on,direction=NegZ");
            s.isCatalogHidden.set(false);

            s = generator.createState("power=on,direction=PosX");
            s.rotation[1] = 90;

            s = generator.createState("power=on,direction=PosZ");
            s.rotation[1] = 180;

            s = generator.createState("power=on,direction=NegX");
            s.rotation[1] = 270;

            s = generator.createState("power=on,direction=PosY");
            s.rotation[0] = 270;

            s = generator.createState("power=on,direction=NegY");
            s.rotation[0] = 90;

            System.out.println("-------------------------------------------------");
            System.out.println(generator);
        }

        {
            BlockEventGenerator generator = modBlock.addGenerator(new BlockEventGenerator(
                    Identifier.of("base", "block_events_glass"),
                    Identifier.of("base", "block_events_laser_switch2")
            ));
            TriggerGroup group = generator.createTriggerGroup("onLaserHit");
            group.createTrigger("base:run_trigger")
                    .setParameter("triggerId", "relayTogglePower");
            group = generator.createTriggerGroup("onInteract");
            group.createTrigger("base:run_trigger")
                    .setParameter("triggerId", "relayTogglePower");

            group = generator.createTriggerGroup("relayTogglePower");
            group.createTrigger("base:cycle_block_state_params")
                    .setParameter("params", "off", "on");

            System.out.println("-------------------------------------------------");
            System.out.println(generator);
        }

        {

            BlockModelGenerator generator = modBlock.addGenerator(new BlockModelGenerator("model_laser_switch_on"));
            generator.addTexture("side", Identifier.of("base:textures/blocks/laser_switch_side_on.png"));
            generator.addTexture("front", Identifier.of("base:textures/blocks/laser_emitter_top.png"));

            ModelCuboid cuboid = generator.createCuboid(Vector3.Zero, 16, 2, 16);
            cuboid.setTextureIds("sides");

            cuboid.faces[ModelCuboid.LOCAL_POS_Y].canCullFace = false;
            cuboid.faces[ModelCuboid.LOCAL_NEG_Z].uv[1] = 14;
            cuboid.faces[ModelCuboid.LOCAL_POS_Z].uv[1] = 14;
            cuboid.faces[ModelCuboid.LOCAL_NEG_X].uv[1] = 14;
            cuboid.faces[ModelCuboid.LOCAL_POS_X].uv[1] = 14;

            cuboid = generator.createCuboid(new Vector3(0, 2, 0), 2, 14, 16);
            cuboid.setTextureIds("sides");

            cuboid.faces[ModelCuboid.LOCAL_POS_X].canCullFace = false;

            cuboid.faces[ModelCuboid.LOCAL_POS_Y].setUVs(0, 0, 2, 16);
            cuboid.faces[ModelCuboid.LOCAL_NEG_Y].setUVs(0, 0, 2, 16);
            cuboid.faces[ModelCuboid.LOCAL_NEG_Z].setUVs(14, 2, 16, 14);
            cuboid.faces[ModelCuboid.LOCAL_POS_Z].setUVs(0, 2, 2, 14);
            cuboid.faces[ModelCuboid.LOCAL_POS_X].setUVs(0, 2, 16, 14);
            cuboid.faces[ModelCuboid.LOCAL_NEG_X].setUVs(0, 2, 16, 14);

            cuboid = generator.createCuboid(new Vector3(2, 2, 2), 14, 14, 16);
            cuboid.setTextureIds("sides");

            cuboid.faces[ModelCuboid.LOCAL_POS_Y] = null;
            cuboid.faces[ModelCuboid.LOCAL_NEG_Y] = null;
            cuboid.faces[ModelCuboid.LOCAL_NEG_Z].setUVs(2, 2, 14, 14);
            cuboid.faces[ModelCuboid.LOCAL_NEG_Z].textureId = "front";
            cuboid.faces[ModelCuboid.LOCAL_POS_Z].setUVs(2, 2, 14, 14);
            cuboid.faces[ModelCuboid.LOCAL_POS_X] = null;
            cuboid.faces[ModelCuboid.LOCAL_NEG_X] = null;

            cuboid = generator.createCuboid(new Vector3(14, 2, 0), 16, 14, 16);
            cuboid.setTextureIds("sides");

            cuboid.faces[ModelCuboid.LOCAL_NEG_X].canCullFace = false;

            cuboid.faces[ModelCuboid.LOCAL_POS_Y].setUVs(14, 0, 16, 16);
            cuboid.faces[ModelCuboid.LOCAL_NEG_Y].setUVs(14, 0, 16, 16);
            cuboid.faces[ModelCuboid.LOCAL_NEG_Z].setUVs(0, 2, 2, 14);
            cuboid.faces[ModelCuboid.LOCAL_POS_Z].setUVs(0, 2, 2, 14);
            cuboid.faces[ModelCuboid.LOCAL_POS_X].setUVs(0, 2, 16, 14);
            cuboid.faces[ModelCuboid.LOCAL_NEG_X].setUVs(0, 2, 16, 14);

            cuboid = generator.createCuboid(new Vector3(0,14,0), 16,16,16);
            cuboid.setTextureIds("sides");

            cuboid.faces[ModelCuboid.LOCAL_NEG_Y].canCullFace = false;

            cuboid.faces[ModelCuboid.LOCAL_POS_Y].setUVs(0,0,16,16);
            cuboid.faces[ModelCuboid.LOCAL_NEG_Y].setUVs(0,0,16,16);
            cuboid.faces[ModelCuboid.LOCAL_NEG_Z].setUVs(0,0,16,2);
            cuboid.faces[ModelCuboid.LOCAL_POS_Z].setUVs(0,0,16,2);
            cuboid.faces[ModelCuboid.LOCAL_POS_X].setUVs(0,0,16,2);
            cuboid.faces[ModelCuboid.LOCAL_NEG_X].setUVs(0,0,16,2);

            System.out.println("-------------------------------------------------");
            System.out.println(generator);
        }

    }

    @Override
    public void onInit() {
        System.err.println("CommonPuzzle init called");

        LoaderConstants.CORE_EVENT_BUS.post(new )

        AutomatedModBlock modBlock = new AutomatedModBlock(Identifier.of("test", "laser_switch2"));

        {
            BlockGenerator generator = modBlock.getGenerator();
            State defaultStateParams = generator.getDefaultProperties();
            defaultStateParams.blockEventId = Identifier.of("base", "block_events_laser_switch");
            defaultStateParams.modelId = "base:models/blocks/model_laser_switch_on.json";
            defaultStateParams.tags.add("tool_pickaxe_effective");
            defaultStateParams.placementRules = "omnidirectional_towards";
            defaultStateParams.allowsStateSwapping.set(false);
            defaultStateParams.isCatalogHidden.set(true);
            defaultStateParams.stateGenerators.add("base:laser_switch_power_off");
            defaultStateParams.dropParameters.put("direction", JsonValue.valueOf("NegZ"));

            State s = generator.createState("power=on,direction=NegZ");
            s.isCatalogHidden.set(false);

            s = generator.createState("power=on,direction=PosX");
            s.rotation[1] = 90;

            s = generator.createState("power=on,direction=PosZ");
            s.rotation[1] = 180;

            s = generator.createState("power=on,direction=NegX");
            s.rotation[1] = 270;

            s = generator.createState("power=on,direction=PosY");
            s.rotation[0] = 270;

            s = generator.createState("power=on,direction=NegY");
            s.rotation[0] = 90;

//            System.out.println("-------------------------------------------------");
//            System.out.println(generator);
        }

        Block block = BlockLoader.INSTANCE.generate(modBlock);
        System.err.println(block.getStringId());
    }

    @Override
    public void onPostInit() {
        System.err.println("CommonPuzzle postInit called");
    }

    @Override
    public void onPreInit() {
        if (LoaderConstants.SIDE.equals(EnvType.SERVER))
            ISidedModelLoader.CONTEXTUAL_INSTANCE.set(new CommonSidedModelLoader());

        System.err.println("CommonPuzzle preInit called");
    }

}
