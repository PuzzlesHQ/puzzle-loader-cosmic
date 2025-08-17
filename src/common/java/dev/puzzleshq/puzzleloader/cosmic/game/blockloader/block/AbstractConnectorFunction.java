package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.block;

import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.connected.ISidedBlockConnector;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelFace;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.state.State;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import dev.puzzleshq.puzzleloader.loader.LoaderConstants;
import dev.puzzleshq.puzzleloader.loader.util.EnvType;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.util.constants.Direction;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

import java.util.function.Function;

public abstract class AbstractConnectorFunction implements ISidedBlockConnector.ConnectorFunction {

    BlockModelGenerator parentModelGenerator;
    BlockModelGenerator modelGenerator;

    public Identifier id;
    public Identifier texturePath;
    public String parentModelName;
    public String defaultModelName;
    public String templateModelName;
    private static final int corners = 16 | 32 | 64 | 128;

    boolean shouldCullClearFace = false;
    private TextureModes textureMode = TextureModes.TEXTURE_MODE_256;
    boolean shouldEndEdges = true;

    int emptyFace = 15;
    int defaultStartingValue;
    int loopCount;
    BlockModelGenerator connectedModelGenerator;

    public AbstractConnectorFunction(
            Identifier id,
            TextureModes mode
    ) {
        setMode(mode);

        texturePath = Identifier.of("connected-textures", id.getName() + "/states/");
        defaultModelName = "model_connected_textures-|-" + id.getName() + "-|-base-block";
        parentModelName = "model_connected_textures-|-" + id.getName() + "-|-texture-cache";
        templateModelName = "model_connected_textures_bordered-|-" + id.getName() + "-|-d+%s-v+%d";

        if (LoaderConstants.SIDE.equals(EnvType.SERVER)) return;
        parentModelGenerator = new BlockModelGenerator(parentModelName);

        parentModelGenerator.addTexture("xError", Identifier.of(texturePath + "state-error.png"));
        for (int i = 0; i < textureMode.textureCount; i++)
            parentModelGenerator.addTexture("x" + i, textureMode.getTextureId(texturePath, i));

        modelGenerator = new BlockModelGenerator(parentModelName, defaultModelName);
        modelGenerator.createCuboid(Vector3.Zero, 16, 16, 16).setTextureIds("x0");

        ISidedModelLoader.getInstance().loadModel(parentModelGenerator, ISidedModelLoader.DEFAULT_ROTATION);
        ISidedModelLoader.getInstance().loadModel(modelGenerator, ISidedModelLoader.DEFAULT_ROTATION);

        connectedModelGenerator = new BlockModelGenerator(parentModelName, "temp");
        connectedModelGenerator.createCuboid(Vector3.Zero, 16, 16, 16);
        connectedModelGenerator.isTransparent = true;

        for (Direction d : Direction.ALL_DIRECTIONS) {
            for (int i = 0; i < textureMode.modelCount; i++)
                createModel(d, i);
        }
    }

    protected void setMode(TextureModes textureMode) {
        this.textureMode = textureMode;
        this.defaultStartingValue = textureMode.startValue;
        this.loopCount = textureMode.loopCount;
    }

    public TextureModes textureMode() {
        return textureMode;
    }

    private void createModel(Direction direction, int i) {
        String textureId = parentModelGenerator.textures.containsKey("x" + textureMode.getTextureNum(i)) ? "x" + textureMode.getMiniTextureId.apply(i) : "xError";
        String dir = ("LOCAL_" + direction.name());

        ModelCuboid cuboid = connectedModelGenerator.cuboids.getFirst();
        for (int j = 0; j < 6; j++) cuboid.faces[j] = null;
        try {
            int id = (int) ModelCuboid.class.getField(dir).get(null);

            cuboid.faces[id] = new ModelFace();
            cuboid.faces[id].textureId = textureId;
            cuboid.faces[id].uv = textureMode.getTextureUV(id);
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
        return String.format(templateModelName, direction.name(), i);
    }

    private static final int[] I_OFFSETS = new int[] {
            1,   0,  1,
            2,   0, -1,
            4,   1,  0,
            8,  -1,  0,

            16, -1,  1,
            32, -1, -1,
            64,  1,  1,
            128, 1, -1
    };

    public boolean canCheckFace(BlockState state, BlockState expect) {
        if (state == null) return true;
//        if (ISidedBlockConnector.getInstance().isConnectedBlock(state)) return !state.cullsSelf();
        if (expect.equals(state)) return !state.cullsSelf();
        return !state.isOpaque;
    }

    public boolean canAreaConnect(BlockState state, BlockState expect) {
//        if (state != null) return ISidedBlockConnector.getInstance().isConnectedBlock(state);
        if (state != null) return expect.equals(state);
        else return false;
    }

    public int checkFace(
            Direction direction,
            int loop, int v,
            Zone zone, BlockState expected,
            int xOffs, int yOffs, int zOffs,
            int x, int y, int z,
            int faceMod
    ) {
        final BlockState FACE = zone.getBlockState(
                direction.getXOffset() + x,
                direction.getYOffset() + y,
                direction.getZOffset() + z
        );
        if (!canCheckFace(FACE, expected)) return emptyFace;
        boolean isCovered = FACE != null && (FACE.isOpaque);
        if (isCovered) v = this.emptyFace;

        BlockState flatCheck = zone.getBlockState(x + xOffs, y + yOffs, z + zOffs);
        BlockState extrudedCheck = zone.getBlockState(
                direction.getXOffset() + x + xOffs,
                direction.getYOffset() + y + yOffs,
                direction.getZOffset() + z + zOffs
        );
        if (canAreaConnect(flatCheck, expected) && (!shouldEndEdges || !canAreaConnect(extrudedCheck, expected))) {
            return ((loop >= 12) ? (v & ~faceMod) : v | faceMod);
        }
        return v;
    }

    public void meshFace(Direction direction, int v, IMeshData meshData, int x, int y, int z, int opaqueBitmask, short[] blockLightLevels, int[] skyLightLevels) {
        if (v == emptyFace && shouldCullClearFace) return;

        BlockModel model = ISidedModelLoader.getInstance().loadModel(createModelName(direction, v));

        model.addVertices(meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
    }

    public void connect(Zone zone, Chunk chunk, BlockState blockState, int x, int y, int z, IMeshData meshData, int opaqueBitmask, short[] blockLightLevels, int[] skyLightLevels) {
        int POS_Y = defaultStartingValue;
        int NEG_Y = defaultStartingValue;
        int POS_X = defaultStartingValue;
        int NEG_X = defaultStartingValue;
        int POS_Z = defaultStartingValue;
        int NEG_Z = defaultStartingValue;

        for (int i = 0; i < loopCount; i += 3) {
            int faceModifier = I_OFFSETS[i];
            int offsetH = I_OFFSETS[i + 1];
            int offsetV = I_OFFSETS[i + 2];

            POS_X = checkFace(
                    Direction.POS_X, i, POS_X,
                    zone, blockState,
                    0, -offsetV, offsetH,
                    x, y, z,
                    faceModifier
            );

            NEG_X = checkFace(
                    Direction.NEG_X, i, NEG_X,
                    zone, blockState,
                    0, -offsetV, -offsetH,
                    x, y, z,
                    faceModifier
            );

            POS_Y = checkFace(
                    Direction.POS_Y, i, POS_Y,
                    zone, blockState,
                    -offsetH, 0, offsetV,
                    x, y, z,
                    faceModifier
            );

            NEG_Y = checkFace(
                    Direction.NEG_Y, i, NEG_Y,
                    zone, blockState,
                    -offsetH, 0, -offsetV,
                    x, y, z,
                    faceModifier
            );

            POS_Z = checkFace(
                    Direction.POS_Z, i, POS_Z,
                    zone, blockState,
                    -offsetH, -offsetV, 0,
                    x, y, z,
                    faceModifier
            );

            NEG_Z = checkFace(
                    Direction.NEG_Z, i, NEG_Z,
                    zone, blockState,
                    offsetH, -offsetV, 0,
                    x, y, z,
                    faceModifier
            );
        }

        meshFace(Direction.POS_Y, POS_Y, meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
        meshFace(Direction.NEG_Y, NEG_Y, meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);

        meshFace(Direction.POS_X, POS_X, meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
        meshFace(Direction.NEG_X, NEG_X, meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);

        meshFace(Direction.POS_Z, POS_Z, meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
        meshFace(Direction.NEG_Z, NEG_Z, meshData, x, y, z, opaqueBitmask, blockLightLevels, skyLightLevels);
    }

    final static float[] defaultUv = new float[]{0, 0, 16, 16};

    public enum TextureModes {
//        TEXTURE_MODE_6(6, 16, 0,
//                12, (i) -> "state-" + i + ".png",
//                (i) -> switch (i) {
//                    case 0 -> 0;
//                    case 1, 2, 4, 8 -> 1;
//                    case 3, 12 -> 2;
//                    case 5, 6, 9, 10 -> 3;
//                    case 7, 11, 13, 14 -> 4;
//                    case 15 -> 5;
//                    default -> throw new IllegalStateException("Unexpected value: " + i);
//                },
//                (i) -> switch (i) {
//                    case 0, 15 -> defaultUv;
//
//                    case 1 -> QuadUvUtil.createRotatedUv(defaultUv, 2);
//                    case 2 -> defaultUv;
//                    case 4 -> QuadUvUtil.createRotatedUv(defaultUv, 1);
//                    case 8 -> QuadUvUtil.createRotatedUv(defaultUv, 3);
//
//                    case 3 -> QuadUvUtil.createRotatedUv(defaultUv, 3);
//                    case 12 -> QuadUvUtil.createRotatedUv(defaultUv, 3);
//
//                    case 5 -> QuadUvUtil.createRotatedUv(defaultUv, 0);
//                    case 6 -> QuadUvUtil.createRotatedUv(defaultUv, 0);
//                    case 9 -> QuadUvUtil.createRotatedUv(defaultUv, 0);
//                    case 10 -> QuadUvUtil.createRotatedUv(defaultUv, 0);
//
//                    case 7 -> QuadUvUtil.createRotatedUv(defaultUv, 3);
//                    case 11 -> QuadUvUtil.createRotatedUv(defaultUv, 1);
//                    case 13 -> QuadUvUtil.createRotatedUv(defaultUv, 2);
//                    case 14 -> defaultUv;
//
//                    default -> defaultUv;
//                }
//        ),
        TEXTURE_MODE_16(16, 16, 0,
                12, (i) -> "state-" + i + ".png", (i) -> i,
                (i) -> defaultUv
        ),
        TEXTURE_MODE_256(256, 256, corners,
                24, (i) -> (i & ~corners) + "state-" + i + ".png", (i) -> i,
                (i) -> defaultUv
        );

        final int textureCount;
        final int startValue;
        final int loopCount;
        final Function<Integer, String> identifierFunction;
        final int modelCount;
        final Function<Integer, Integer> getMiniTextureId;
        final Function<Integer, float[]> getTextureIdUv;

        TextureModes(
                int textureCount, int modelCount,
                int startValue, int loopCount,
                Function<Integer, String> identifierFunction,
                Function<Integer, Integer> getMiniTextureId,
                Function<Integer, float[]> getTextureIdUv
                ) {
            this.textureCount = textureCount;
            this.modelCount = modelCount;
            this.startValue = startValue;
            this.loopCount = loopCount;
            this.identifierFunction = identifierFunction;
            this.getMiniTextureId = getMiniTextureId;
            this.getTextureIdUv = getTextureIdUv;
        }

        public Identifier getTextureId(Identifier texturePath, int i) {
            return Identifier.of(texturePath.toString() + identifierFunction.apply(i));
        }

        public int getTextureNum(int i) {
            return getMiniTextureId.apply(i);
        }

        public float[] getTextureUV(int id) {
            return getTextureIdUv.apply(id);
        }
    }

}
