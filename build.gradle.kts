plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
}

subprojects {
    tasks.withType<Test>().configureEach {
        // XXX: https://github.com/robolectric/robolectric/issues/11434
        jvmArgs("--add-opens=java.base/jdk.internal.access=ALL-UNNAMED")
    }
}