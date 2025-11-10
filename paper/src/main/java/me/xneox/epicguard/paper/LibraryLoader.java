package me.xneox.epicguard.paper;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import me.xneox.epicguard.core.util.Constants;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class LibraryLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        final MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(new RemoteRepository.Builder(
                "central",
                "default",
                MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR
        ).build());

        Stream.of(
                "com.zaxxer:HikariCP:" + Constants.HIKARI,
                "com.github.ben-manes.caffeine:caffeine:" + Constants.CAFFEINE,
                "org.apache.commons:commons-compress:" + Constants.COMMANDS_COMPRESS,
                "org.apache.commons:commons-text:" + Constants.COMMONS_TEXT,
                "com.maxmind.geoip2:geoip2:" + Constants.GEOIP,
                "com.maxmind.db:maxmind-db:" + Constants.MAXMIND_DB,
                "com.fasterxml.jackson.core:jackson-annotations:" + Constants.JACKSON,
                "com.fasterxml.jackson.core:jackson-core:" + Constants.JACKSON,
                "com.fasterxml.jackson.core:jackson-databind:" + Constants.JACKSON,
                "me.xdrop:fuzzywuzzy:" + Constants.FUZZYWUZZY,
                "cloud.commandframework:cloud-paper:" + Constants.CLOUD
        )
                .map(artifact -> new Dependency(new DefaultArtifact(artifact), null))
                .forEach(resolver::addDependency);

        classpathBuilder.addLibrary(resolver);
    }
}
