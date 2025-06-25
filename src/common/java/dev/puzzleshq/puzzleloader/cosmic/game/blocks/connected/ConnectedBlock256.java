package dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IConnectedBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelFace;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.util.constants.Direction;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class ConnectedBlock256 implements IModBlock, IConnectedBlock {

    static BlockModelGenerator parentModelGenerator;
    BlockGenerator generator;
    BlockModelGenerator modelGenerator;

    public static final Identifier id = Identifier.of("connected-textures", "connected-block-256");
    public static final Identifier texturePath = Identifier.of("connected-textures", id.getName() + "/states/");
    public static final String parentModelName = "model_connected_textures-|-" + id.getName() + "-|-texture-cache";
    public static final String defaultModelName = "model_connected_textures-|-" + id.getName() + "-|-base-block";

    public ConnectedBlock256() {
        generator = new BlockGenerator(id);
        State state = generator.createState("default");
        state.modelId = defaultModelName;

        parentModelGenerator = new BlockModelGenerator(parentModelName);

        parentModelGenerator.addTexture("xError", Identifier.of(texturePath + "state-error.png"));
        for (int i = 0; i < 256; i++)
            parentModelGenerator.addTexture("x" + i, Identifier.of(texturePath.toString() + (i & ~corners) + "/state-" + i + ".png"));

        modelGenerator = new BlockModelGenerator(parentModelName, defaultModelName);
        modelGenerator.createCuboid(Vector3.Zero, 16, 16, 16).setTextureIds("x0");

        ISidedModelLoader.getInstance().loadModel(parentModelGenerator, ISidedModelLoader.DEFAULT_ROTATION);

        for (Direction d : Direction.ALL_DIRECTIONS) {
            for (int i = 0; i < 256; i++)
                createModel(d, i);
        }
    }

    private void createModel(Direction direction, int i) {
        System.err.println("Generating Model for " + direction.name() + " with state " + i);
        String textureId = parentModelGenerator.textures.containsKey("x" + i) ? "x" + i : "xError";
        String dir = ("LOCAL_" + direction.name());

        ModelCuboid cuboid = connectedModelGenerator.cuboids.getFirst();
        for (int j = 0; j < 6; j++) cuboid.faces[j] = null;
        try {
            cuboid.faces[(int) ModelCuboid.class.getField(dir).get(null)] = new ModelFace();
            cuboid.faces[(int) ModelCuboid.class.getField(dir).get(null)].textureId = textureId;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        String modelName = createModelName(direction, i);

        connectedModelGenerator.setName(modelName);

        (ISidedModelLoader.getInstance()).loadModel(
                connectedModelGenerator,
                State.DEFAULT_ROTATION
        );
    }

    private String createModelName(Direction direction, int i) {
        return String.format("model_connected_textures-|-" + id.getName() + "-|-d+%s-v+%d", direction.name(), i);
    }

    private static final Vector3[] OFFSETS = new Vector3[] {
            /* _R 000001 */ new Vector3(1, 0, 1),
            /* _L 000010 */ new Vector3(2, 0, -1),
            /* U_ 000100 */ new Vector3(4, 1, 0),
            /* D_ 001000 */ new Vector3(8, -1, 0),

            /* DR 000001 */ new Vector3(16, -1, 1),
            /* DL 000001 */ new Vector3(32, -1, -1),
            /* UR 000010 */ new Vector3(64, 1, 1),
            /* UL 000010 */ new Vector3(128, 1, -1),
    };

    public void connect(Zone zone, Chunk chunk, BlockState blockState, int x, int y, int z, IMeshData meshData, int opaqueBitmask, short[] blockLightLevels, int[] skyLightLevels) {
        int POS_Y = 0;
        int NEG_Y = 0;
        int POS_X = 0;
        int NEG_X = 0;
        int POS_Z = 0;
        int NEG_Z = 0;

        BlockState check;
        for (Vector3 yc : OFFSETS) {
            /* POS_Y */ check = zone.getBlockState(x - yc.y, y, z + yc.z);
            /* POS_Y */ if (check != null && check.equals(blockState)) POS_Y |= (int) yc.x;
            /* NEG_Y */ check = zone.getBlockState(x - yc.y, y, z - yc.z);
            /* NEG_Y */ if (check != null && check.equals(blockState)) NEG_Y |= (int) yc.x;
            /* POS_X */ check = zone.getBlockState(x, y - yc.z, z + yc.y);
            /* POS_X */ if (check != null && check.equals(blockState)) POS_X |= (int) yc.x;
            /* NEG_X */ check = zone.getBlockState(x, y - yc.z, z - yc.y);
            /* NEG_X */ if (check != null && check.equals(blockState)) NEG_X |= (int) yc.x;
            /* POS_Z */ check = zone.getBlockState(x - yc.y, y - yc.z, z);
            /* POS_Z */ if (check != null && check.equals(blockState)) POS_Z |= (int) yc.x;
            /* NEG_Z */ check = zone.getBlockState(x + yc.y, y - yc.z, z);
            /* NEG_Z */ if (check != null && check.equals(blockState)) NEG_Z |= (int) yc.x;
        }

        final String MODEL_NAME_POS_X = createModelName(Direction.POS_X, POS_X);
        final String MODEL_NAME_NEG_X = createModelName(Direction.NEG_X, NEG_X);
        final String MODEL_NAME_POS_Y = createModelName(Direction.POS_Y, POS_Y);
        final String MODEL_NAME_NEG_Y = createModelName(Direction.NEG_Y, NEG_Y);
        final String MODEL_NAME_POS_Z = createModelName(Direction.POS_Z, POS_Z);
        final String MODEL_NAME_NEG_Z = createModelName(Direction.NEG_Z, NEG_Z);

        final BlockModel MODEL_POS_X = ISidedModelLoader.getInstance().loadModel(MODEL_NAME_POS_X);
        final BlockModel MODEL_NEG_X = ISidedModelLoader.getInstance().loadModel(MODEL_NAME_NEG_X);
        final BlockModel MODEL_POS_Y = ISidedModelLoader.getInstance().loadModel(MODEL_NAME_POS_Y);
        final BlockModel MODEL_NEG_Y = ISidedModelLoader.getInstance().loadModel(MODEL_NAME_NEG_Y);
        final BlockModel MODEL_POS_Z = ISidedModelLoader.getInstance().loadModel(MODEL_NAME_POS_Z);
        final BlockModel MODEL_NEG_Z = ISidedModelLoader.getInstance().loadModel(MODEL_NAME_NEG_Z);

        MODEL_POS_X.addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
        MODEL_NEG_X.addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
        MODEL_POS_Y.addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
        MODEL_NEG_Y.addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
        MODEL_POS_Z.addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
        MODEL_NEG_Z.addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
    }

    static BlockModelGenerator connectedModelGenerator = new BlockModelGenerator(parentModelName,"");
    private static final int corners = 16 | 32 | 64 | 128;

    static {
        connectedModelGenerator.createCuboid(Vector3.Zero, 16, 16, 16);
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }

    @Override
    public BlockModelGenerator[] getModelGenerators() {
        return new BlockModelGenerator[]{ modelGenerator };
    }
}
