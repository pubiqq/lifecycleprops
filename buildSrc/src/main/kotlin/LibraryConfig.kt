import org.gradle.api.JavaVersion

object LibraryConfig {
    const val Group = "io.github.pubiqq"
    const val Version = "1.0.0-SNAPSHOT"

    const val MinSdk = 14
    const val CompileSdk = 32
    const val TargetSdk = 32
    const val BuildTools = "32.0.0"

    val JvmTarget = JavaVersion.VERSION_11
}