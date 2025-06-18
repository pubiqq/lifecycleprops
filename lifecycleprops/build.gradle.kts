import com.pubiqq.lifecycleprops.build_logic.common.toJavaVersion
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import com.pubiqq.lifecycleprops.build_logic.common.Config as CommonConfig
import com.pubiqq.lifecycleprops.build_logic.library.Config as LibraryConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.vanniktech.mavenPublish)

    alias(libs.plugins.lifecycleprops.common)
    alias(libs.plugins.lifecycleprops.library)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget = CommonConfig.JvmTarget
        }
    }

    // Supports the same platforms as lifecycle-common, see:
    // https://github.com/androidx/androidx/blob/f738ba8e78eac927472758abe64c9628823ea9ef/lifecycle/lifecycle-common/build.gradle#L33-L36
    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
    macosArm64()

    linuxX64()
    linuxArm64()

    // Soon (see https://github.com/androidx/androidx/blob/834412e0397f485e1fe370cb9594b86588b9a694/lifecycle/lifecycle-common/build.gradle#L33-L41)
//    js()
//
//    @Suppress("OPT_IN_USAGE")
//    wasmJs()
//
//    watchosX64()
//    watchosArm32()
//    watchosArm64()
//    watchosDeviceArm64()
//    watchosSimulatorArm64()
//
//    tvosX64()
//    tvosArm64()
//    tvosSimulatorArm64()
//
//    mingwX64()

    compilerOptions {
        allWarningsAsErrors = true
        extraWarnings = true
        explicitApi = ExplicitApiMode.Strict
    }

    sourceSets {
        all {
            languageSettings {
                optIn("com.pubiqq.lifecycleprops.ExperimentalConfigurationApi")
            }
        }

        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.common)
        }

        commonTest.dependencies {
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.androidx.fragment)
        }
    }
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
        sourceCompatibility = CommonConfig.JvmTarget.toJavaVersion()
        targetCompatibility = CommonConfig.JvmTarget.toJavaVersion()
    }
}

mavenPublishing {
    coordinates(
        groupId = LibraryConfig.Group,
        version = LibraryConfig.Version,
        artifactId = "lifecycleprops"
    )

    configure(KotlinMultiplatform(
        sourcesJar = true,
        javadocJar = JavadocJar.None(),
        androidVariantsToPublish = listOf("release")
    ))

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

    publishToMavenCentral(SonatypeHost.S01)
    signAllPublications()
}