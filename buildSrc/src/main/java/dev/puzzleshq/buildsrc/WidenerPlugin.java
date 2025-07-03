package dev.puzzleshq.buildsrc;

import dev.puzzleshq.accesswriter.AccessWriters;
import dev.puzzleshq.accesswriter.api.IWriterFormat;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.attributes.AttributesSchema;
import org.gradle.api.internal.provider.DefaultProperty;
import org.gradle.api.provider.Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class WidenerPlugin implements Plugin<Project> {

    Attribute<Boolean> checked = Attribute.of("checked", Boolean.class);
    Attribute<Boolean> widened = Attribute.of("widened", Boolean.class);

    public Property<File> widenerPath = new DefaultProperty<>((e) -> null, File.class);

    @Override
    public void apply(Project target) {
        System.err.println("WidenerPlugin Activated");

        var t = target.getExtensions().create("widenerPlugin", WidenerPlugin.class);

        target.afterEvaluate(project -> {
            AccessWriters.initDefaultFormats();
            AccessWriters.MERGED.clear();

            if (t.widenerPath.isPresent()) {
                File path = t.widenerPath.get();
                if (!path.exists()) throw new RuntimeException(path + " does not exist!");

                IWriterFormat format = AccessWriters.getFormat(path.getName());

                try {
                    FileInputStream stream = new FileInputStream(path);
                    String s = new String(stream.readAllBytes());
                    stream.close();

                    format.parse(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            DependencyHandler dependencyHandler = project.getDependencies();
            AttributesSchema schema = dependencyHandler.getAttributesSchema();
            schema.attribute(checked);
            schema.attribute(widened);

            dependencyHandler.getArtifactTypes().getByName("jar", artifact -> {
                artifact.getAttributes().attribute(checked, false);
                artifact.getAttributes().attribute(widened, false);
            });

            dependencyHandler.registerTransform(JarSearcher.class, noneTransformSpec -> {
                noneTransformSpec.getFrom().attribute(checked, false);
                noneTransformSpec.getTo().attribute(checked, true);
            });

            dependencyHandler.registerTransform(JarTransformer.class, noneTransformSpec -> {
                noneTransformSpec.getFrom().attribute(widened, false);
                noneTransformSpec.getTo().attribute(widened, true);
            });

            project.getConfigurations().forEach(it -> {
                if (it.isCanBeResolved()) {
                    it.getAttributes().attribute(widened, true);
                    it.getAttributes().attribute(checked, true);
                }
            });
        });
    }

}
