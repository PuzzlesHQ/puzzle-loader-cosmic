package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Queue;
import dev.puzzleshq.puzzleloader.cosmic.game.GameRegistries;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.events.block.EventModBlockRegister;
import dev.puzzleshq.puzzleloader.cosmic.game.util.ImmutableMapWrapper;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.gameevents.blockevents.BlockEvents;
import finalforeach.cosmicreach.util.GameTag;
import finalforeach.cosmicreach.util.GameTagList;
import finalforeach.cosmicreach.util.logging.Logger;
import finalforeach.cosmicreach.util.logging.LoggerLevel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockLoader {

    public static final BlockLoader INSTANCE = new BlockLoader();
    public static final Map<IModBlock, Block> MODDED_TO_VANILLA_BLOCK_MAP = new ImmutableMapWrapper<>(INSTANCE.internalModdedToVanillaBlockMap);
    public static final Map<Block, IModBlock> VANILLA_TO_MODDED_BLOCK_MAP = new ImmutableMapWrapper<>(INSTANCE.internalVanillaToModdedBlockMap);

    private final Json json;

    private final Map<IModBlock, Block> internalModdedToVanillaBlockMap = new ConcurrentHashMap<>();
    private final Map<Block, IModBlock> internalVanillaToModdedBlockMap = new ConcurrentHashMap<>();

    public BlockLoader() {
        json = new Json();

        json.setSerializer(Vector3.class, new Json.Serializer<>() {
            public void write(Json json, Vector3 object, Class knownType) {
                json.writeValue(new float[]{object.x, object.y, object.z});
            }

            public Vector3 read(Json json, JsonValue jsonData, Class type) {
                float[] f = jsonData.asFloatArray();
                return new Vector3(f);
            }
        });
        json.setSerializer(GameTag.class, new Json.Serializer<>() {
            public void write(Json json, GameTag tag, Class knownType) {
                json.writeValue(tag.name);
            }

            public GameTag read(Json json, JsonValue jsonData, Class type) {
                String s = json.readValue(String.class, jsonData);
                return GameTag.get(s);
            }
        });
        json.setSerializer(GameTagList.class, GameTagList.GAME_TAG_JSON_SERIALIZER);

    }

    public static void injectIntoQueue(Queue<Runnable> queue) {
        queue.addLast(() -> {
            EventModBlockRegister event = new EventModBlockRegister();
            GameRegistries.COSMIC_EVENT_BUS.post(event);

            for (IModBlock modBlock : event.getBlocks()) {
                BlockLoader.INSTANCE.generate(modBlock);
            }
        });
    }

    public Block generate(IModBlock block) {
        BlockGenerator generator = block.getGenerator();
        BlockEventGenerator[] eventGenerators = block.getEventGenerators();
        BlockModelGenerator[] modelGenerators = block.getModelGenerators();

        if (eventGenerators != null) {
            for (BlockEventGenerator eventGenerator : eventGenerators) {
                String jsonStr = eventGenerator.toString();
                BlockEvents blockEvents = json.fromJson(BlockEvents.class, jsonStr);
                BlockEvents.INSTANCES.put(eventGenerator.getId().toString(), blockEvents);
            }
        }

        if (modelGenerators != null) {
            for (BlockModelGenerator modelGenerator : modelGenerators) {
                ISidedModelLoader loader = ISidedModelLoader.getInstance();

                loader.loadModel(modelGenerator, true);
            }
        }

        if (generator != null) {
            Block b = json.fromJson(Block.class, generator.toString());
            if (b.blockStates.containsKey("default")) {
                b.blockStates.put("", b.blockStates.get("default"));
                b.blockStates.remove("default");
            }

            Array<String> blockStateKeysToAdd = b.blockStates.keys().toArray();

            for (String stateKey : blockStateKeysToAdd) {
                BlockState blockState = b.blockStates.get(stateKey);
                blockState.rotation = new float[]{0, 0, 0};
                blockState.stringId = stateKey;
                blockState.initialize(b);
                blockState.initTagList();
                Block.allBlockStates.put(blockState.stringId, blockState);
            }

            Block.blocksByStringId.put(generator.getId().toString(), b);

            internalVanillaToModdedBlockMap.put(b, block);
            internalModdedToVanillaBlockMap.put(block, b);
            block.onRegistered(b);
            return b;
        }

        throw new IllegalStateException("Block \"" + block.getId() + "\" has failed to load! This could be caused by the block loader being out of date.");
    }

    public boolean wasLoadedByBlockLoader(Block block) {
        return internalVanillaToModdedBlockMap.containsKey(block);
    }

    public boolean wasLoadedByBlockLoader(IModBlock block) {
        return internalModdedToVanillaBlockMap.containsKey(block);
    }

    public IModBlock getModdedFromVanillaBlock(Block block) {
        return internalVanillaToModdedBlockMap.get(block);
    }

    public Block getVanillaFromModdedBlock(IModBlock block) {
        return internalModdedToVanillaBlockMap.get(block);
    }

    public boolean wasLoadedByGlobalBlockLoader(Block block) {
        return VANILLA_TO_MODDED_BLOCK_MAP.containsKey(block);
    }

    public boolean wasLoadedByGlobalBlockLoader(IModBlock block) {
        return MODDED_TO_VANILLA_BLOCK_MAP.containsKey(block);
    }

    public static IModBlock getModdedFromVanillaBlockGlobal(Block block) {
        return VANILLA_TO_MODDED_BLOCK_MAP.get(block);
    }

    public static Block getVanillaFromModdedBlockGlobal(IModBlock block) {
        return MODDED_TO_VANILLA_BLOCK_MAP.get(block);
    }

}
