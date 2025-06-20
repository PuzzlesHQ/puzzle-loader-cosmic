package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.event.BlockEventGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.util.GameTag;
import finalforeach.cosmicreach.util.GameTagList;

public class BlockLoader {

    public static final BlockLoader INSTANCE = new BlockLoader();

    private final Json json ;

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

    public Block generate(IModBlock block) {
        BlockGenerator generator = block.getGenerator();
        BlockEventGenerator[] eventGenerators = block.getEventGenerator();
        BlockModelGenerator[] modelGenerators = block.getModelGenerators();

        if (eventGenerators != null) {
            for (BlockEventGenerator eventGenerator : eventGenerators) {
                String jsonStr = eventGenerator.toString();
                BlockEvents blockEvents = json.fromJson(BlockEvents.class, jsonStr);
                BlockEvents.INSTANCES.put(eventGenerator.getId().toString(), blockEvents);
            }
        }

//        Map<String, BlockModelGenerator> generatorMap = new HashMap<>();

        if (modelGenerators != null) {
            for (BlockModelGenerator modelGenerator : modelGenerators) {
//                generatorMap.put(modelGenerator.getName(), modelGenerator);

                ISidedModelLoader loader = ISidedModelLoader.getInstance();

                loader.loadModel(modelGenerator);
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
                blockState.stringId = stateKey;
                blockState.initialize(b);
                Block.allBlockStates.put(blockState.stringId, blockState);
            }

            Block.blocksByStringId.put(generator.getId().toString(), b);
            return b;
        }

        throw new RuntimeException("If you are seeing this either final messed with me or a dev screwed up.");
    }

}
