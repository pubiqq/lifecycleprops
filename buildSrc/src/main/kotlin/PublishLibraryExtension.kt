import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom

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