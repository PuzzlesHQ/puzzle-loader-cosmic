package dev.puzzleshq.buildsrc;

import dev.puzzleshq.accesswriter.AccessWriters;
import dev.puzzleshq.mod.ModFormats;
import dev.puzzleshq.mod.info.ModInfoBuilder;
import org.gradle.api.artifacts.transform.InputArtifact;
import org.gradle.api.artifacts.transform.TransformAction;
import org.gradle.api.artifacts.transform.TransformOutputs;
import org.gradle.api.artifacts.transform.TransformParameters;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.provider.Provider;
import org.hjson.JsonObject;

import java.io.*;
import java.util.zip.ZipFile;

public abstract class JarSearcher implements TransformAction<TransformParameters.None> {

    @InputArtifact
    public abstract Provider<FileSystemLocation> getInputArtifact();

    @Override
    public void transform(TransformOutputs outputs) {
        File inp = getInputArtifact().get().getAsFile();

        var out = outputs.file(inp.getName().replace(".jar", "-checked.jar"));

        try {
            ZipFile zip = new ZipFile(inp);

            if (zip.getEntry("puzzle-loader-core.manipulator") != null) {
                InputStream stream1 = zip.getInputStream(zip.getEntry("puzzle-loader-core.manipulator"));
                String str = new String(stream1.readAllBytes());
                stream1.close();

                AccessWriters.MERGED.add(AccessWriters.getFormat("manipulator").parse(str));
            }

            if (zip.getEntry("puzzle.mod.json") != null) {
                InputStream stream1 = zip.getInputStream(zip.getEntry("puzzle.mod.json"));
                String str = new String(stream1.readAllBytes());
                stream1.close();

                ModInfoBuilder builder = new ModInfoBuilder();
                ModFormats.getFormat(3).parse(builder, JsonObject.readHjson(str).asObject());
                for (String accessWriter : builder.getAccessWriters()) {
                    InputStream stream2 = zip.getInputStream(zip.getEntry(accessWriter));
                    String str2 = new String(stream2.readAllBytes());
                    stream2.close();

                    AccessWriters.MERGED.add(AccessWriters.getFormat(accessWriter).parse(str2));
                }
            }
            FileInputStream inStream = new FileInputStream(inp);
            FileOutputStream outStream = new FileOutputStream(out);
            outStream.write(inStream.readAllBytes());
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
