// XXX: Bug in Android plugin?
//
// == Android Studio Jellyfish | 2023.3.1 Patch 1 ==
// == IntelliJ IDEA 2024.1.2 (Community Edition) + org.jetbrains.android (241.17011.79) ==
// On rebuild:
// "Unable to make progress running work. There are items queued for execution but none of them can be started"
//
// https://issuetracker.google.com/issues/315023802
// https://issuetracker.google.com/issues/328871352
//
gradle.startParameter.excludedTaskNames.addAll(
    gradle.startParameter.taskNames.filter { it.contains("testClasses") }
)

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.kotlinx.abiValidator)

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

apiValidation {
    ignoredProjects += listOf("sample")
    ignoredClasses += listOf("com.pubiqq.lifecycleprops.LifecycleAwareConfigurationApi")
    nonPublicMarkers += listOf("com.pubiqq.lifecycleprops.LifecycleAwareConfigurationApi")
}