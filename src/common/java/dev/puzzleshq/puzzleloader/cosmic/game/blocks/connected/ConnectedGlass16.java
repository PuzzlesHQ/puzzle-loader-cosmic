package dev.puzzleshq.puzzleloader.cosmic.game.blocks.connected;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IConnectedBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block.IModBlock;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.BlockGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class ConnectedGlass16 implements ISidedBlockConnector.ConnectorFunction {

    BlockModelGenerator parentModelGenerator;
    BlockModelGenerator modelGenerator;

    public static final Identifier id = Identifier.of("connected-textures", "connected-glass-16");
    public static final String parentModelName = "model_connected_textures-|-connected-glass-16-empty";
    public static final String defaultModelName = "model_connected_textures-|-connected-glass-16-|-px+0-py+0-pz+0-nx+0-ny+0-nz+0";

    public static String createModelName(int px, int py, int pz, int nx, int ny, int nz) {
        return String.format("model_connected_textures-|-connected-glass-16-|-px+%d-py+%d-pz+%d-nx+%d-ny+%d-nz+%d", px, py, pz, nx, ny, nz);
    }

    public ConnectedGlass16() {
        parentModelGenerator = new BlockModelGenerator(parentModelName);
        parentModelGenerator.addTexture("x0", Identifier.of("connected-textures:glass/glass_0.png"));
        parentModelGenerator.addTexture("x1", Identifier.of("connected-textures:glass/glass_1.png"));
        parentModelGenerator.addTexture("x2", Identifier.of("connected-textures:glass/glass_2.png"));
        parentModelGenerator.addTexture("x3", Identifier.of("connected-textures:glass/glass_3.png"));
        parentModelGenerator.addTexture("x4", Identifier.of("connected-textures:glass/glass_4.png"));
        parentModelGenerator.addTexture("x5", Identifier.of("connected-textures:glass/glass_5.png"));
        parentModelGenerator.addTexture("x6", Identifier.of("connected-textures:glass/glass_6.png"));
        parentModelGenerator.addTexture("x7", Identifier.of("connected-textures:glass/glass_7.png"));
        parentModelGenerator.addTexture("x8", Identifier.of("connected-textures:glass/glass_8.png"));
        parentModelGenerator.addTexture("x9", Identifier.of("connected-textures:glass/glass_9.png"));
        parentModelGenerator.addTexture("x10", Identifier.of("connected-textures:glass/glass_10.png"));
        parentModelGenerator.addTexture("x11", Identifier.of("connected-textures:glass/glass_11.png"));
        parentModelGenerator.addTexture("x12", Identifier.of("connected-textures:glass/glass_12.png"));
        parentModelGenerator.addTexture("x13", Identifier.of("connected-textures:glass/glass_13.png"));
        parentModelGenerator.addTexture("x14", Identifier.of("connected-textures:glass/glass_14.png"));
        parentModelGenerator.addTexture("x15", Identifier.of("connected-textures:glass/glass_15.png"));

        modelGenerator = new BlockModelGenerator(parentModelName, defaultModelName);
        modelGenerator.createCuboid(Vector3.Zero, 16, 16, 16).setTextureIds("x0");

        ISidedModelLoader.getInstance().loadModel(parentModelGenerator, ISidedModelLoader.DEFAULT_ROTATION);
        ISidedModelLoader.getInstance().loadModel(modelGenerator, ISidedModelLoader.DEFAULT_ROTATION);

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    createModel(x, y, z, 0, 0, 0);
                    createModel(0, 0, 0, x, y, z);
                    createModel(x, y, z, x, y, z);
                }
            }
        }

        createModel(0, 0, 0, 0, 0, 0);
    }

    private static final Vector3[] OFFSETS = new Vector3[] {
            /* D_ 000001 */ new Vector3(1, 0, 1),
            /* U_ 000010 */ new Vector3(2, 0, -1),
            /* _L 000100 */ new Vector3(4, 1, 0),
            /* _R 001000 */ new Vector3(8, -1, 0),
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
            /* POS_Y */ check = zone.getBlockState(x - yc.y, y, z - yc.z);
            /* POS_Y */ if (check != null && check.equals(blockState)) POS_Y |= (int) yc.x;
            /* NEG_Y */ check = zone.getBlockState(x - yc.y, y, z + yc.z);
            /* NEG_Y */ if (check != null && check.equals(blockState)) NEG_Y |= (int) yc.x;
            /* POS_X */ check = zone.getBlockState(x, y - yc.z, z - yc.y);
            /* POS_X */ if (check != null && check.equals(blockState)) POS_X |= (int) yc.x;
            /* NEG_X */ check = zone.getBlockState(x, y - yc.z, z + yc.y);
            /* NEG_X */ if (check != null && check.equals(blockState)) NEG_X |= (int) yc.x;
            /* POS_Z */ check = zone.getBlockState(x + yc.y, y - yc.z, z);
            /* POS_Z */ if (check != null && check.equals(blockState)) POS_Z |= (int) yc.x;
            /* NEG_Z */ check = zone.getBlockState(x - yc.y, y - yc.z, z);
            /* NEG_Z */ if (check != null && check.equals(blockState)) NEG_Z |= (int) yc.x;
        }

        String modelName = createModelName(POS_X, POS_Y, POS_Z, NEG_X, NEG_Y, NEG_Z);

        if (ISidedModelLoader.getInstance().hasModel(modelName)) {
            BlockModel model = ISidedModelLoader.getInstance().loadModel(modelName);
            model.addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);

            return;
        }

        createModel(
                POS_X,
                POS_Y,
                POS_Z,
                NEG_X,
                NEG_Y,
                NEG_Z
        ).addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
    }

    static BlockModelGenerator connectedModelGenerator = new BlockModelGenerator(parentModelName,"");

    static {
        connectedModelGenerator.createCuboid(Vector3.Zero, 16, 16, 16);
    }

    private static BlockModel createModel(int posX, int posY, int posZ, int negX, int negY, int negZ) {
        ModelCuboid cuboid = connectedModelGenerator.cuboids.getFirst();
        cuboid.faces[ModelCuboid.LOCAL_NEG_X].textureId = "x" + posX;
        cuboid.faces[ModelCuboid.LOCAL_NEG_Y].textureId = "x" + posY;
        cuboid.faces[ModelCuboid.LOCAL_NEG_Z].textureId = "x" + posZ;
        cuboid.faces[ModelCuboid.LOCAL_POS_X].textureId = "x" + negX;
        cuboid.faces[ModelCuboid.LOCAL_POS_Y].textureId = "x" + negY;
        cuboid.faces[ModelCuboid.LOCAL_POS_Z].textureId = "x" + negZ;

        String modelName = createModelName(posX, posY, posZ, negX, negY, negZ);

        connectedModelGenerator.setName(modelName);

        System.err.println("Generating Model - " + modelName );

        return (ISidedModelLoader.getInstance()).loadModel(
                connectedModelGenerator,
                State.DEFAULT_ROTATION
        );
    }

}
