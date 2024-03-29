import com.pubiqq.lifecycleprops.build_logic.common.Config as CommonConfig
import com.pubiqq.lifecycleprops.build_logic.library.Config as LibraryConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("com.pubiqq.lifecycleprops.build_logic.common")
    id("com.pubiqq.lifecycleprops.build_logic.library")
    id("com.pubiqq.lifecycleprops.build_logic.publish")
}

android {
    namespace = "com.pubiqq.lifecycleprops"

    compileSdk = LibraryConfig.CompileSdk
    buildToolsVersion = LibraryConfig.BuildTools

    defaultConfig {
        minSdk = LibraryConfig.MinSdk

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
        sourceCompatibility = CommonConfig.JvmTarget
        targetCompatibility = CommonConfig.JvmTarget
    }

    kotlinOptions {
        jvmTarget = CommonConfig.JvmTarget.toString()
        allWarningsAsErrors = true

        freeCompilerArgs += listOf(
            "-opt-in=com.pubiqq.lifecycleprops.LifecycleAwareConfigurationApi",
            "-Xexplicit-api=strict"
        )
    }
}

libraryPublishing {
    group = LibraryConfig.Group
    version = LibraryConfig.Version
    artifactId = "lifecycleprops"

    pom {
        name = "LifecycleProps"
        description = "Property delegates that enable you to associate properties with lifecycle-aware components."
        url = "https://github.com/pubiqq/lifecycleprops"

        licenses {
            license {
                name = "Apache License 2.0"
                url = "https://github.com/pubiqq/lifecycleprops/blob/${LibraryConfig.Version}/LICENSE.md"
                distribution = "repo"
            }
        }

        developers {
            developer {
                id = "pubiqq"
            }
        }

        scm {
            url = "https://github.com/pubiqq/lifecycleprops"
            connection = "scm:git:https://github.com/pubiqq/lifecycleprops.git"
            developerConnection = "scm:git:https://github.com/pubiqq/lifecycleprops.git"
        }
    }
}

dependencies {
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.common)

    testImplementation(libs.junit)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.mockk)
}