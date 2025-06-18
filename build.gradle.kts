import kotlinx.validation.ExperimentalBCVApi

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
    alias(libs.plugins.vanniktech.mavenPublish) apply false
}

apiValidation {
    @OptIn(ExperimentalBCVApi::class)
    klib {
        enabled = true
    }

    ignoredProjects += listOf("sample")
    nonPublicMarkers += listOf("com.pubiqq.lifecycleprops.ExperimentalConfigurationApi")
}