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
}

apiValidation {
    ignoredProjects += listOf("sample")
    ignoredClasses += listOf("com.pubiqq.lifecycleprops.LifecycleAwareConfigurationApi")
    nonPublicMarkers += listOf("com.pubiqq.lifecycleprops.LifecycleAwareConfigurationApi")
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}