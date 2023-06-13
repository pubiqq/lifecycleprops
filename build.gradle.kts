buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

// https://youtrack.jetbrains.com/issue/KTIJ-19369#focus=Comments-27-5181027.0-0
@Suppress("DSL_SCOPE_VIOLATION")
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