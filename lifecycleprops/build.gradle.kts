import utils.by

// https://youtrack.jetbrains.com/issue/KTIJ-19369#focus=Comments-27-5181027.0-0
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id("publish-library")
}

android {
    namespace = "com.pubiqq.lifecycleprops"

    compileSdk = LibraryConfig.CompileSdk
    buildToolsVersion = LibraryConfig.BuildTools

    defaultConfig {
        minSdk = LibraryConfig.MinSdk
        targetSdk = LibraryConfig.TargetSdk

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = false
        resValues = false
    }

    compileOptions {
        sourceCompatibility = LibraryConfig.JvmTarget
        targetCompatibility = LibraryConfig.JvmTarget
    }

    kotlinOptions {
        jvmTarget = LibraryConfig.JvmTarget.toString()
        allWarningsAsErrors = true

        @Suppress("SuspiciousCollectionReassignment")
        freeCompilerArgs += listOf(
            "-opt-in=com.pubiqq.lifecycleprops.LifecycleAwareOptions",
            "-Xexplicit-api=strict"
        )
    }
}

publishLibrary {
    group = LibraryConfig.Group
    version = LibraryConfig.Version
    artifactId = "lifecycleprops"

    pom {
        name by "LifecycleProps"
        description by "Property delegates that enable you to associate properties with lifecycle-aware components."
        url by "https://github.com/pubiqq/lifecycleprops"

        licenses {
            license {
                name by "Apache License 2.0"
                url by "https://github.com/pubiqq/lifecycleprops/blob/${LibraryConfig.Version}/LICENSE.md"
                distribution by "repo"
            }
        }

        developers {
            developer {
                id by "pubiqq"
            }
        }

        scm {
            url by "https://github.com/pubiqq/lifecycleprops"
            connection by "scm:git:https://github.com/pubiqq/lifecycleprops.git"
            developerConnection by "scm:git:https://github.com/pubiqq/lifecycleprops.git"
        }
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.common)
}