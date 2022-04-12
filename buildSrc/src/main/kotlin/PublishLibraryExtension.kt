import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.withType

open class PublishLibraryExtension(
    private val project: Project
) {
    var group: String = ""
    var artifactId: String = ""
    var version: String = ""

    var pomConfigure: Action<in MavenPom>? = null
        private set

    fun pom(configure: Action<in MavenPom>) {
        pomConfigure = configure
    }
}