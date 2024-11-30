import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.*

plugins {
    java
    alias(libs.plugins.shadow)
}

allprojects {
    apply<JavaPlugin>()
    apply<ShadowPlugin>()

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}

subprojects {
    tasks {
        withType<ShadowJar> {
            val platformName = project.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            archiveFileName.set("EpicGuard$platformName-${project.version}.jar")

            minimize()

            // Copy compiled platform jars to '/build' directory for convenience.
            if (project.name != "core") {
                doLast {
                    copy {
                        from(archiveFile)
                        into("${rootProject.projectDir}/build")
                    }
                }
            }

        }

        build {
            dependsOn(shadowJar)
        }

        withType<Delete> {
            delete("run")
        }

        withType<JavaCompile> {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(21)
        }
    }
}
