import org.gradle.api.JavaVersion

object LibraryConfig {
    const val Group = "io.github.pubiqq"
    const val Version = "1.2.0-SNAPSHOT"

    const val MinSdk = 14
    const val CompileSdk = 33
    const val BuildTools = "33.0.2"

    val JvmTarget = JavaVersion.VERSION_11
}