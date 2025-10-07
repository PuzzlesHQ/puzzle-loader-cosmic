package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.client;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import dev.puzzleshq.annotation.documentation.Note;
import dev.puzzleshq.annotation.stability.Experimental;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelFace;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelTexture;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.loading.ISidedModelLoader;
import dev.puzzleshq.puzzleloader.loader.util.ReflectionUtil;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonCuboid;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonCuboidFace;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonTexture;
import finalforeach.cosmicreach.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Experimental
@Note("This implementation may change between versions due to this heavily using reflection and due it poking into the BlockModelJson class's internals, its recommended to use the generator itself without this class.")
public class BlockModelBuilder {

    BlockModelJson modelJson;
    String name;

    public BlockModelBuilder(String name) {
        this.name = name;
        try {
            modelJson = (BlockModelJson) ReflectionUtil.getConstructor(BlockModelJson.class).newInstance();
            setPool(new OrderedMap<>());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public BlockModelBuilder(BlockModelGenerator generator) {
        this(generator.getName());

        modelJson.isTransparent = generator.isTransparent;
        // added to make sure the game does not crash if this gets removed, as we are unsure if this variable is used in the base game.
        try {
            ReflectionUtil.getField(modelJson, "cullsSelf").set(modelJson, generator.canCullSelf);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("remove \"cullsSelf\" attribute in BlockModelBuilder and BlockModelGenerator as it has been removed from the base game!");
        }

        for (Map.Entry<String, ModelTexture> entry : generator.textures.entrySet()) {
            addTexture(entry.getKey(), entry.getValue());
        }
        for (ModelCuboid cuboid : generator.cuboids) {
            addCuboid(cuboid);
        }
        if (generator.parentModel != null) {
            BlockModelJson parent = (BlockModelJson) ISidedModelLoader.getInstance().loadModel(generator.parentModel);

            if (parent.getTextures() != null) {
                for (ObjectMap.Entry<String, BlockModelJsonTexture> entry : parent.getTextures().entries()) {
                    if (!modelJson.getTextures().containsKey(entry.key)) {
                        modelJson.getTextures().put(entry.key, entry.value);
                    }
                }
            }

            if (generator.cuboids.isEmpty()) {
                try {
                    BlockModelJsonCuboid[] cuboids = (BlockModelJsonCuboid[]) ReflectionUtil.getField(BlockModelJson.class, "cuboids").get(modelJson);
                    if (cuboids != null) for (BlockModelJsonCuboid cuboid : cuboids) addCuboid(cuboid);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setPool(OrderedMap<String, BlockModelJsonTexture> textureOrderedMap) {
        try {
            ReflectionUtil.getField(modelJson, "textures").set(modelJson, textureOrderedMap);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTexture(String id, ModelTexture location) {
        BlockModelJsonTexture texture = new BlockModelJsonTexture();
        texture.fileName = location.getRegularTexture().toString();
        if (location.getEmissiveTexture() != null) {
            texture.emissivefileName = location.getEmissiveTexture().toString();
        }
        modelJson.getTextures().put(id, texture);
    }

    List<BlockModelJsonCuboid> cuboids = new ArrayList<>();

    public void addCuboid(BlockModelJsonCuboid cuboid) {
        this.cuboids.add(cuboid);
    }

    public BlockModelJsonCuboid addCuboid(ModelCuboid modelCuboid) {
        BlockModelJsonCuboid cuboid = new BlockModelJsonCuboid();

        cuboid.setLocalBounds(new float[]{
                modelCuboid.min.x,
                modelCuboid.min.y,
                modelCuboid.min.z,

                modelCuboid.max.x,
                modelCuboid.max.y,
                modelCuboid.max.z,
        });

        try {
            ReflectionUtil.getField(cuboid, "faces").set(cuboid, new OrderedMap<>());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        if (modelCuboid.faces[ModelCuboid.LOCAL_NEG_X] != null) cuboid.faces.put("localNegX", createFace(modelCuboid.faces[ModelCuboid.LOCAL_NEG_X]));
        if (modelCuboid.faces[ModelCuboid.LOCAL_POS_X] != null) cuboid.faces.put("localPosX", createFace(modelCuboid.faces[ModelCuboid.LOCAL_POS_X]));
        if (modelCuboid.faces[ModelCuboid.LOCAL_NEG_Y] != null) cuboid.faces.put("localNegY", createFace(modelCuboid.faces[ModelCuboid.LOCAL_NEG_Y]));
        if (modelCuboid.faces[ModelCuboid.LOCAL_POS_Y] != null) cuboid.faces.put("localPosY", createFace(modelCuboid.faces[ModelCuboid.LOCAL_POS_Y]));
        if (modelCuboid.faces[ModelCuboid.LOCAL_NEG_Z] != null) cuboid.faces.put("localNegZ", createFace(modelCuboid.faces[ModelCuboid.LOCAL_NEG_Z]));
        if (modelCuboid.faces[ModelCuboid.LOCAL_POS_Z] != null) cuboid.faces.put("localPosZ", createFace(modelCuboid.faces[ModelCuboid.LOCAL_POS_Z]));

        this.cuboids.add(cuboid);
        return cuboid;
    }

    private BlockModelJsonCuboidFace createFace(ModelFace face) {
        BlockModelJsonCuboidFace fce = new BlockModelJsonCuboidFace();
        fce.uv = face.uv;
        fce.ambientocclusion = face.useAmbientOcclusion;
        fce.texture = face.textureId;
        try {
            ReflectionUtil.getField(fce, "cullFace").set(fce, face.canCullFace);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return fce;
    }

    public String getName() {
        return name;
    }

    public BlockModelJson build() {
        return build(0, 0, 0);
    }


    public BlockModelJson build(int rotX, int rotY, int rotZ) {
        try {
            ReflectionUtil.getField(BlockModelJson.class, "cuboids").set(modelJson, cuboids.toArray(BlockModelJsonCuboid[]::new));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            ReflectionUtil.getMethod(modelJson, "initialize", int.class, int.class, int.class).invoke(modelJson, rotX, rotY, rotZ);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            ReflectionUtil.getMethod(modelJson, "initShader").invoke(modelJson);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return modelJson;
    }

    public void setTransparency(boolean b) {
        this.modelJson.isTransparent = b;
    }

    public void cullSelf(boolean b) {
        this.modelJson.cullsSelf = b;
    }
}
