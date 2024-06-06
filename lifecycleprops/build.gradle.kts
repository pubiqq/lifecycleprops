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
    // https://github.com/androidx/androidx/blob/995638a3bc28a50cfe000be11b374234557fc4f2/lifecycle/lifecycle-common/build.gradle#L36-L41
    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
    macosArm64()

    linuxX64()

    // Not supported by androidx, see:
    // https://github.com/androidx/androidx/blob/1a1b3c9aa4baedbd4179d3de59ffb5bde67d7bab/buildSrc/private/src/main/kotlin/androidx/build/AndroidXMultiplatformExtension.kt#L500
//    linuxArm64()


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

    // XXX: Bug in Android Studio?
    //
    // == Android Studio Jellyfish | 2023.3.1 Patch 1 ==
    // Cannot locate tasks that match ':lifecycleprops:testClasses' as task 'testClasses' not found in project ':lifecycleprops'. Some candidates are: 'jvmTestClasses'.
    // https://youtrack.jetbrains.com/issue/IDEA-348814/Android-Studio-Iguana-breaks-KMP-compilation#focus=Comments-27-9483444.0-0
    //
    // Works on IntelliJ IDEA 2024.1.2 (Community Edition) + org.jetbrains.android (241.17011.79)
    task("testClasses")
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