// https://youtrack.jetbrains.com/issue/KTIJ-19369#focus=Comments-27-5181027.0-0
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false

    alias(libs.plugins.kotlinx.abiValidator)
}

// ATTENTION: nonPublicMarkers is not working properly yet, do not rely on the dump
//  to get information about the public api.
//  https://github.com/Kotlin/binary-compatibility-validator/issues/36
//  https://github.com/Kotlin/binary-compatibility-validator/issues/58
apiValidation {
    ignoredProjects += listOf("sample")
    ignoredClasses += listOf("com.pubiqq.lifecycleprops.LifecycleAwareOptions")
    nonPublicMarkers += listOf("com.pubiqq.lifecycleprops.LifecycleAwareOptions")
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}