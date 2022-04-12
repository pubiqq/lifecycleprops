import com.android.build.gradle.LibraryExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import java.util.*

class PublishLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            if (!pluginManager.hasPlugin("com.android.library")) {
                throw GradleException("Plugin is applicable only to \"com.android.library\" projects (${target.name})")
            }

            val props = loadProperties()
            val extension = extensions.create(
                "publishLibrary",
                PublishLibraryExtension::class.java,
                this
            )

            preconfigureMavenPublish()
            preconfigureSigning(props)

            afterEvaluate {
                configureMavenPublish(props, extension)
                configureSigning(extension)
            }
        }
    }

    private fun Project.loadProperties(): Properties {
        val propsFile = rootProject.file("local.properties")
        if (!propsFile.exists()) {
            throw GradleException("local.properties is not found.")
        }

        val props = Properties()
        propsFile.inputStream().use { props.load(it) }
        return props
    }

    private fun Project.preconfigureSigning(props: Properties) {
        apply<SigningPlugin>()

        extra["signing.keyId"] = props["signing.keyId"]
        extra["signing.password"] = props["signing.password"]
        extra["signing.secretKeyRingFile"] = props["signing.secretKeyRingFile"]
    }

    private fun Project.configureSigning(extension: PublishLibraryExtension) {
        configure<SigningExtension> {
            if (!extension.version.endsWith("SNAPSHOT")) {
                sign(extensions.getByType<PublishingExtension>().publications)
            }
        }
    }

    private fun Project.preconfigureMavenPublish() {
        apply<MavenPublishPlugin>()

        extensions.configure<LibraryExtension>("android") {
            publishing {
                singleVariant("release") {
                    withSourcesJar()
                }
            }
        }
    }

    private fun Project.configureMavenPublish(props: Properties, extension: PublishLibraryExtension) {
        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("release") {
                    this.groupId = extension.group
                    this.artifactId = extension.artifactId
                    this.version = extension.version

                    from(components["release"])

                    extension.pomConfigure?.let { pom(it) }
                }
            }

            repositories {
                maven {
                    name = "mavenCentral"
                    url = if (extension.version.endsWith("SNAPSHOT")) {
                        uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    } else {
                        uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    }

                    credentials {
                        username = props["mavenCentral.username"].toString()
                        password = props["mavenCentral.password"].toString()
                    }
                }
            }
        }
    }
}