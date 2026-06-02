package dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.enhanced;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.BlockModelGenerator;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelCuboid;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelPlane;
import dev.puzzleshq.puzzleloader.cosmic.game.blockloader.generation.model.ModelTexture;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.util.constants.Direction;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.hjson.Stringify;

import java.util.*;

public class EnhancedBlockModelGenerator extends BlockModelGenerator {

    public final Map<String, ModelPartGroup> groups = new HashMap<>();

    public EnhancedBlockModelGenerator(String name) {
        super(name);
    }

    public EnhancedBlockModelGenerator(String parentModel, String name) {
        super(parentModel, name);
    }

    public EnhancedBlockModelGenerator(BlockModelGenerator parentModelGenerator, String name) {
        super(parentModelGenerator, name);
    }

    public void addCuboidToGroup(String group, ModelCuboid cuboid) {
        if (!this.groups.containsKey(group)) {
            ModelPartGroup group1 = new ModelPartGroup(
                    this,
                    null, group,
                    new ArrayList<>(), new ArrayList<>()
            );
            group1.cuboids.add(cuboid);
            this.groups.put(group, group1);
            return;
        }
        ModelPartGroup group1 = this.groups.get(group);
        group1.cuboids.add(cuboid);
    }

    public void addPlaneToGroup(String group, ModelPlane plane) {
        if (!this.groups.containsKey(group)) {
            ModelPartGroup group1 = new ModelPartGroup(
                    this,
                    null, group,
                    new ArrayList<>(), new ArrayList<>()
            );
            group1.planes.add(plane);
            this.groups.put(group, group1);
            return;
        }
        ModelPartGroup group1 = this.groups.get(group);
        group1.planes.add(plane);
    }

    private static void fix(int w, int h, float[] uv) {
        uv[0] = uv[0] / (w / 16f);
        uv[1] = uv[1] / (h / 16f);
        uv[2] = uv[2] / (w / 16f);
        uv[3] = uv[3] / (h / 16f);
    }

    private static void newCube(ModelPlane[] planes, Vector3 origin, Vector3 size, float inflate, Vector3 pivot, Vector3 rotation, ModelPartGroup group) {
        planes[ModelCuboid.LOCAL_NEG_Y].setVertex(0, -.5f, -.5f, -.5f);
        planes[ModelCuboid.LOCAL_NEG_Y].setVertex(3, -.5f, -.5f, .5f);
        planes[ModelCuboid.LOCAL_NEG_Y].setVertex(1, .5f, -.5f, -.5f);
        planes[ModelCuboid.LOCAL_NEG_Y].setVertex(2, .5f, -.5f, .5f);

        planes[ModelCuboid.LOCAL_POS_Y].setVertex(2, -.5f, .5f, -.5f);
        planes[ModelCuboid.LOCAL_POS_Y].setVertex(3, -.5f, .5f, .5f);
        planes[ModelCuboid.LOCAL_POS_Y].setVertex(1, .5f, .5f, -.5f);
        planes[ModelCuboid.LOCAL_POS_Y].setVertex(0, .5f, .5f, .5f);

        planes[ModelCuboid.LOCAL_NEG_X].setVertex(2, -.5f, -.5f, -.5f);
        planes[ModelCuboid.LOCAL_NEG_X].setVertex(3, -.5f, -.5f, .5f);
        planes[ModelCuboid.LOCAL_NEG_X].setVertex(1, -.5f, .5f, -.5f);
        planes[ModelCuboid.LOCAL_NEG_X].setVertex(0, -.5f, .5f, .5f);

        planes[ModelCuboid.LOCAL_POS_X].setVertex(0, .5f, -.5f, -.5f);
        planes[ModelCuboid.LOCAL_POS_X].setVertex(3, .5f, -.5f, .5f);
        planes[ModelCuboid.LOCAL_POS_X].setVertex(1, .5f, .5f, -.5f);
        planes[ModelCuboid.LOCAL_POS_X].setVertex(2, .5f, .5f, .5f);

        planes[ModelCuboid.LOCAL_NEG_Z].setVertex(0, -.5f, -.5f, -.5f);
        planes[ModelCuboid.LOCAL_NEG_Z].setVertex(3, .5f, -.5f, -.5f);
        planes[ModelCuboid.LOCAL_NEG_Z].setVertex(1, -.5f, .5f, -.5f);
        planes[ModelCuboid.LOCAL_NEG_Z].setVertex(2, .5f, .5f, -.5f);

        planes[ModelCuboid.LOCAL_POS_Z].setVertex(2, -.5f, -.5f, .5f);
        planes[ModelCuboid.LOCAL_POS_Z].setVertex(3, .5f, -.5f, .5f);
        planes[ModelCuboid.LOCAL_POS_Z].setVertex(1, -.5f, .5f, .5f);
        planes[ModelCuboid.LOCAL_POS_Z].setVertex(0, .5f, .5f, .5f);

        List<ModelPartGroup> groupList = new ArrayList<>();
        groupList.add(group);
        ModelPartGroup g = group;
        while (g.parentName != null && !g.parentName.isEmpty()) {
            g = g.getParent();
            groupList.add(g);
        }

        Matrix4 boneRotation = new Matrix4();
        Matrix4 currentRot = new Matrix4();

        ModelPartGroup root = groupList.get(groupList.size() - 1);
        Vector3 rootPivot = root.getPivot();
        Vector3 rootRotation = root.getRotation();
        boneRotation.translate(rootPivot);
        boneRotation.rotate(Vector3.Z, rootRotation.z);
        boneRotation.rotate(Vector3.Y, -rootRotation.y);
        boneRotation.rotate(Vector3.X, -rootRotation.x);
        boneRotation.translate(-rootPivot.x, -rootPivot.y, -rootPivot.z);
        groupList.remove(groupList.size() - 1);

        for (int i = groupList.size() - 1; i > -1; i--) {
            currentRot.idt();

            ModelPartGroup currentGroup = groupList.get(i);
            Vector3 groupPivot = currentGroup.getPivot();
            Vector3 groupRotation = currentGroup.getRotation();
            currentRot.translate(groupPivot);
            currentRot.rotate(Vector3.Z, groupRotation.z);
            currentRot.rotate(Vector3.Y, -groupRotation.y);
            currentRot.rotate(Vector3.X, -groupRotation.x);
            currentRot.translate(-groupPivot.x, -groupPivot.y, -groupPivot.z);

            currentRot.mulLeft(boneRotation);
            boneRotation.set(currentRot);
        }

        Quaternion quaternion = new Quaternion();
        quaternion.setEulerAngles(rotation.y, rotation.x, rotation.z);
        Matrix4 cubeRot = new Matrix4();
        cubeRot.translate(pivot);
        cubeRot.rotate(quaternion);
        cubeRot.translate(-pivot.x, -pivot.y, -pivot.z);

        Matrix4 cubeMat = new Matrix4();
        cubeMat.translate(origin);
        cubeMat.translate(size.x / 2f, size.y / 2f, size.z / 2f);
        cubeMat.scl(size.x + inflate, size.y + inflate, size.z + inflate);

        cubeMat.mulLeft(cubeRot);
        cubeMat.mulLeft(boneRotation);

        for (ModelPlane plane : planes) {
            for (Vector3 vertex : plane.getVertices()) {
                vertex.mul(cubeMat);
                vertex.add(8, 0, 8);
            }
        }
    }

    public static EnhancedBlockModelGenerator fromEntityModelJsonAsPlanes(String modelName, String json, boolean makeCollider) {
        JsonObject value = JsonObject.readHjson(json).asObject();
        EnhancedBlockModelGenerator generator = new EnhancedBlockModelGenerator(
                modelName
        );

        int textureWidth = value.get("texture_width").asInt();
        int textureHeight = value.get("texture_height").asInt();

        JsonValue textures = value.get("textures");
        if (textures != null) {
            ModelTexture texture = new ModelTexture(Identifier.of(textures.asObject().get("diffuse").asString()));
            if (textures.asObject().get("emission") != null)
                texture.setEmissiveTexture(Identifier.of(textures.asObject().get("emission").asString()));
            generator.addTexture("all", texture);
        }

        value.get("bones").asArray().forEach(item -> {
            JsonObject bone = item.asObject();
            String groupName = bone.get("name").asString();
            String parentGroup = bone.getString("parent", null);

            ModelPartGroup group = new ModelPartGroup(
                    generator,
                    parentGroup, groupName,
                    new ArrayList<>(), new ArrayList<>()
            );
            generator.groups.put(groupName, group);
            JsonValue pivotValue = bone.get("pivot");
            if (pivotValue != null) {
                JsonArray pivot = pivotValue.asArray();
                group.setPivot(pivot.get(0).asFloat(), pivot.get(1).asFloat(), pivot.get(2).asFloat());
            }
            JsonValue rotationValue = bone.get("rotation");
            if (rotationValue != null) {
                JsonArray rotation = rotationValue.asArray();
                group.setRotation(-rotation.get(0).asFloat(), rotation.get(1).asFloat(), rotation.get(2).asFloat());
            }
        });

        value.get("bones").asArray().forEach(item -> {
            JsonObject bone = item.asObject();
            String groupName = bone.get("name").asString();
            JsonValue cubes = bone.get("cubes");

            ModelPartGroup group = generator.groups.get(groupName);
            Vector3 pivot = new Vector3();
            Vector3 rotation = new Vector3();
            if (cubes != null) {
                JsonArray cubesArray = cubes.asArray();
                Vector3 min = new Vector3();
                Vector3 max = new Vector3();
                cubesArray.forEach(cubeValue -> {
                    JsonObject cubeObject = cubeValue.asObject();
                    ModelPlane[] planes = new ModelPlane[]{
                            new ModelPlane("all"), new ModelPlane("all"),
                            new ModelPlane("all"), new ModelPlane("all"),
                            new ModelPlane("all"), new ModelPlane("all")
                    };
                    JsonArray origin = cubeObject.get("origin").asArray();
                    JsonArray size = cubeObject.get("size").asArray();
                    JsonArray uv = cubeObject.get("uv").asArray();
                    float inflate = cubeObject.getFloat("inflate", 0);
                    min.set(
                            origin.get(0).asFloat(),
                            origin.get(1).asFloat(),
                            origin.get(2).asFloat()
                    );
                    max.set(
                            size.get(0).asFloat(),
                            size.get(1).asFloat(),
                            size.get(2).asFloat()
                    );

                    pivot.setZero();
                    rotation.setZero();
                    JsonValue pivotValue = cubeObject.get("pivot");
                    if (pivotValue != null) {
                        JsonArray pivot2 = pivotValue.asArray();
                        pivot.set(pivot2.get(0).asFloat(), pivot2.get(1).asFloat(), pivot2.get(2).asFloat());
                    }
                    JsonValue rotationValue = cubeObject.get("rotation");
                    if (rotationValue != null) {
                        JsonArray rotation2 = rotationValue.asArray();
                        rotation.set(rotation2.get(0).asFloat(), rotation2.get(1).asFloat(), rotation2.get(2).asFloat());
                    }
                    newCube(planes, min, max, inflate, pivot, rotation, group);
                    ModelCuboid c = null;
                    if (makeCollider){
                        c = generator.createCuboid(min, max);
                        c.min.sub(
                                (c.max.x * inflate) / 2,
                                (c.max.y * inflate) / 2,
                                (c.max.z * inflate) / 2
                        );
                        c.max.scl(inflate + 1);
                        c.min.add(8, 0, 8);
                        c.max.add(c.min);
                    }

                    float sX = size.get(0).asFloat();
                    float sY = size.get(1).asFloat();
                    float sZ = size.get(2).asFloat();

                    float U = uv.get(0).asFloat();
                    float V = uv.get(1).asFloat();

                    // UNWRAPPING LAYOUT
                    //     PY,NY
                    // PX, NZ,NX, PZ
                    float[] PY = new float[]{
                            U+sZ+sX, V, U+sZ, V+sZ
                    };
                    float[] NY = new float[]{
                            U+sZ+sX*2, V, U+sZ+sX, V+sZ
                    };
                    float[] PX = new float[]{
                            U+sZ, V+sZ, U, V+sZ+sY
                    };
                    float[] NZ = new float[]{
                            U+sZ+sX, V+sZ, U+sZ, V+sZ+sY
                    };
                    float[] NX = new float[]{
                            U+sZ*2+sX, V+sZ, U+sZ+sX, V+sZ+sY
                    };
                    float[] PZ = new float[]{
                            U+(sZ*2)+(sX*2), V+sZ, U+(sZ*2)+sX, V+sZ+sY
                    };

                    if (sX == 4.5 && sZ == 4.5 && sY == 9) {
                        System.out.println(Arrays.toString(PZ));
                    }

                    fix(textureWidth, textureHeight, PY);
                    fix(textureWidth, textureHeight, NY);
                    fix(textureWidth, textureHeight, PX);
                    fix(textureWidth, textureHeight, NX);
                    fix(textureWidth, textureHeight, PZ);
                    fix(textureWidth, textureHeight, NZ);

                    setUVs(planes[ModelCuboid.LOCAL_POS_X].setUvRotation(270), PX, false, false);
                    setUVs(planes[ModelCuboid.LOCAL_NEG_X].setUvRotation(0), NX, true, false);
                    setUVs(planes[ModelCuboid.LOCAL_POS_Y].setUvRotation(90), PY, true, false);
                    setUVs(planes[ModelCuboid.LOCAL_NEG_Y].setUvRotation(180), NY, true, false);
                    setUVs(planes[ModelCuboid.LOCAL_POS_Z].setUvRotation(0), PZ, false, false);
                    setUVs(planes[ModelCuboid.LOCAL_NEG_Z].setUvRotation(270), NZ, false, false);

                    if (makeCollider){
                        Arrays.fill(c.faces, null);
                        generator.addCuboidToGroup(groupName, c);
                    }
                    generator.addPlaneToGroup(groupName, planes[ModelCuboid.LOCAL_POS_X]);
                    generator.addPlaneToGroup(groupName, planes[ModelCuboid.LOCAL_NEG_X]);
                    generator.addPlaneToGroup(groupName, planes[ModelCuboid.LOCAL_POS_Y]);
                    generator.addPlaneToGroup(groupName, planes[ModelCuboid.LOCAL_NEG_Y]);
                    generator.addPlaneToGroup(groupName, planes[ModelCuboid.LOCAL_POS_Z]);
                    generator.addPlaneToGroup(groupName, planes[ModelCuboid.LOCAL_NEG_Z]);
                });
            }
        });
        return generator;
    }

    public static EnhancedBlockModelGenerator fromEntityModelJsonAsCuboids(String modelName, String json) {
        JsonObject value = JsonObject.readHjson(json).asObject();
        EnhancedBlockModelGenerator generator = new EnhancedBlockModelGenerator(
                modelName
        );

        int textureWidth = value.get("texture_width").asInt();
        int textureHeight = value.get("texture_height").asInt();

        JsonValue textures = value.get("textures");
        if (textures != null) {
            ModelTexture texture = new ModelTexture(Identifier.of(textures.asObject().get("diffuse").asString()));
            if (textures.asObject().get("emission") != null)
                texture.setEmissiveTexture(Identifier.of(textures.asObject().get("emission").asString()));
            generator.addTexture("all", texture);
        }

        value.get("bones").asArray().forEach(item -> {
            JsonObject bone = item.asObject();
            String groupName = bone.get("name").asString();
            String parentGroup = bone.getString("parent", null);

            ModelPartGroup group = new ModelPartGroup(
                    generator,
                    parentGroup, groupName,
                    new ArrayList<>(), new ArrayList<>()
            );
            generator.groups.put(groupName, group);
        });

        value.get("bones").asArray().forEach(item -> {
            JsonObject bone = item.asObject();
            String groupName = bone.get("name").asString();
            JsonValue cubes = bone.get("cubes");

            if (cubes != null) {
                JsonArray cubesArray = cubes.asArray();
                cubesArray.forEach(cubeValue -> {
                    JsonObject cubeObject = cubeValue.asObject();

                    JsonArray origin = cubeObject.get("origin").asArray();
                    JsonArray size = cubeObject.get("size").asArray();
                    JsonArray uv = cubeObject.get("uv").asArray();
                    float inflate = cubeObject.getFloat("inflate", 0);
                    ModelCuboid c = generator.createCuboid(Vector3.Zero, 0, 0, 0);
                    c.max.set(
                            size.get(0).asFloat(),
                            size.get(1).asFloat(),
                            size.get(2).asFloat()
                    );
                    c.min.set(
                            origin.get(0).asFloat(),
                            origin.get(1).asFloat(),
                            origin.get(2).asFloat()
                    );

                    c.min.sub(c.max.x * inflate / 2,
                            c.max.y * inflate / 2,
                            c.max.z * inflate / 2);
                    c.min.add(8, 0, 8);
                    c.max.scl(inflate + 1);
                    c.max.add(c.min);

                    float sX = size.get(0).asFloat();
                    float sY = size.get(1).asFloat();
                    float sZ = size.get(2).asFloat();

                    float U = uv.get(0).asFloat();
                    float V = uv.get(1).asFloat();

                    // UNWRAPPING LAYOUT
                    //     PY,NY
                    // PX, NZ,NX, PZ
                    float[] PY = new float[]{
                            U+sZ+sX, V, U+sZ, V+sZ
                    };
                    float[] NY = new float[]{
                            U+sZ+sX*2, V, U+sZ+sX, V+sZ
                    };
                    float[] PX = new float[]{
                            U+sZ, V+sZ, U, V+sZ+sY
                    };
                    float[] NZ = new float[]{
                            U+sZ+sX, V+sZ, U+sZ, V+sZ+sY
                    };
                    float[] NX = new float[]{
                            U+sZ*2+sX, V+sZ, U+sZ+sX, V+sZ+sY
                    };
                    float[] PZ = new float[]{
                            U+sZ*2+sX*2, V+sZ, U+sZ*2+sX, V+sZ+sY
                    };

                    fix(textureWidth, textureHeight, PY);
                    fix(textureWidth, textureHeight, NY);
                    fix(textureWidth, textureHeight, PX);
                    fix(textureWidth, textureHeight, NX);
                    fix(textureWidth, textureHeight, PZ);
                    fix(textureWidth, textureHeight, NZ);

                    float tmp = PX[0];
                    PX[0] = PX[2];
                    PX[2] = tmp;

                    tmp = NX[0];
                    NX[0] = NX[2];
                    NX[2] = tmp;

                    tmp = PZ[0];
                    PZ[0] = PZ[2];
                    PZ[2] = tmp;

                    tmp = NZ[0];
                    NZ[0] = NZ[2];
                    NZ[2] = tmp;

                    tmp = PY[1];
                    PY[1] = PY[3];
                    PY[3] = tmp;

                    c.faces[Direction.POS_X.ordinal()].uv = PX;
                    c.faces[Direction.NEG_X.ordinal()].uv = NX;
                    c.faces[Direction.POS_Y.ordinal()].uv = PY;
                    c.faces[Direction.NEG_Y.ordinal()].uv = NY;
                    c.faces[Direction.POS_Z.ordinal()].uv = PZ;
                    c.faces[Direction.NEG_Z.ordinal()].uv = NZ;
                    c.setTextureIds("all");

                    generator.addCuboidToGroup(groupName, c);
                });
            }
        });
        return generator;
    }

    // 00, 01, 10, 11
    private static void setUVs(ModelPlane plane, float[] uv, boolean flipU, boolean flipV) {
        float[] uvs = plane.getUvs();
        if (flipU) {
            float tmp = uv[0];
            uv[0] = uv[2];
            uv[2] = tmp;
        }
        if (flipV) {
            float tmp = uv[1];
            uv[1] = uv[3];
            uv[3] = tmp;
        }

        uvs[0] = uv[0];
        uvs[1] = uv[1];

        uvs[2] = uv[2];
        uvs[3] = uv[1];

        uvs[4] = uv[2];
        uvs[5] = uv[3];

        uvs[6] = uv[0];
        uvs[7] = uv[3];
    }

    public ModelPartGroup getGroup(String name) {
        return groups.get(name);
    }

    @Override
    public JsonObject toJson() {
        JsonObject modelJson = new JsonObject();
        JsonObject textures = new JsonObject();
        if (this.parentModel != null) {
            modelJson.add("parent", this.parentModel);
        }

        modelJson.add("isTransparent", this.isTransparent);

        for(Map.Entry<String, ModelTexture> texture : this.textures.entrySet()) {
            JsonObject textureObj = new JsonObject();
            textureObj.add("fileName", texture.getValue().getRegularTexture().toString());
            if (texture.getValue().getEmissiveTexture() != null) {
                textureObj.add("emissivefileName", texture.getValue().getEmissiveTexture().toString());
            }

            textures.add(texture.getKey(), textureObj);
        }

        if (!textures.isEmpty()) {
            modelJson.add("textures", textures);
        }

        JsonArray cuboids = new JsonArray();
        JsonArray planes = new JsonArray();

        this.groups.values().forEach(group -> {
            if (group.isVisible()) {
                for (ModelCuboid cuboid : group.cuboids) {
                    cuboids.add(cuboid.toHJson());
                }
                for (ModelPlane plane : group.planes) {
                    planes.add(plane.toHJson());
                }
            }
        });

        if (!cuboids.isEmpty()) {
            modelJson.add("cuboids", cuboids);
        }

        if (!planes.isEmpty()) {
            modelJson.add("planes", planes);
        }

        return modelJson;
    }

    public String toString() {
        return this.toJson().toString(Stringify.FORMATTED);
    }

}
