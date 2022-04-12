plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gradle.android.tools)
    implementation(libs.gradle.kotlin)
}

gradlePlugin {
    plugins {
        register("publishLibrary") {
            id = "publish-library"
            implementationClass = "PublishLibraryPlugin"
        }
    }
}