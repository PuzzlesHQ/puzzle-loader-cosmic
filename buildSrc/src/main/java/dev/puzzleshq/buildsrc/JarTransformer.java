package dev.puzzleshq.buildsrc;

import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;
import org.gradle.api.artifacts.transform.InputArtifact;
import org.gradle.api.artifacts.transform.TransformAction;
import org.gradle.api.artifacts.transform.TransformOutputs;
import org.gradle.api.artifacts.transform.TransformParameters;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.provider.Provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class JarTransformer implements TransformAction<TransformParameters.None> {

    @InputArtifact
    public abstract Provider<FileSystemLocation> getInputArtifact();

    @Override
    public void transform(TransformOutputs outputs) {
        var inp = getInputArtifact().get().getAsFile();
        var out = outputs.file(inp.getName().replace(".jar", "-transformed.jar"));

        try {
//            List<Relocation> rules = new ArrayList<>();
//
//            rules.add(new Relocation("org.objectweb", "bundled.org.objectweb"));
//            rules.add(new Relocation("org.spongepowered.include", "bundled"));
//
//            JarRelocator relocator = new JarRelocator(inp, out, rules);
//            relocator.run();

            GenericTransformer.transform(inp, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
