import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import com.pubiqq.lifecycleprops.buildlogic.common.Config as CommonConfig
import com.pubiqq.lifecycleprops.buildlogic.library.Config as LibraryConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)

    alias(libs.plugins.lifecycleprops.common)
    alias(libs.plugins.lifecycleprops.library)
}

kotlin {
    jvmToolchain(CommonConfig.JdkVersion)

    android {
        namespace = "com.pubiqq.lifecycleprops"

        compileSdk = LibraryConfig.CompileSdk
        minSdk = LibraryConfig.MinSdk
        buildToolsVersion = LibraryConfig.BuildTools

        withHostTest {
            isIncludeAndroidResources = true
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

    js {
        nodejs()
    }

    @Suppress("OPT_IN_USAGE")
    wasmJs {
        nodejs()
    }

    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosDeviceArm64()
    watchosSimulatorArm64()

    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()

    mingwX64()

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

        getByName("androidHostTest").dependencies {
            implementation(libs.androidx.fragment.testing)
            implementation(libs.robolectric)
        }
    }

    @OptIn(ExperimentalAbiValidation::class)
    abiValidation {
        enabled = true

        filters {
            excluded {
                annotatedWith.add("com.pubiqq.lifecycleprops.ExperimentalConfigurationApi")
            }
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = LibraryConfig.Group,
        version = LibraryConfig.Version,
        artifactId = "lifecycleprops"
    )

    configure(
        KotlinMultiplatform(
            sourcesJar = true,
            javadocJar = JavadocJar.None()
        )
    )

    pom {
        name = "LifecycleProps"
        description = "Property delegates that enable you to associate properties with lifecycle-aware components."
        url = "https://github.com/pubiqq/lifecycleprops"

        licenses {
            license {
                name = "Apache License 2.0"
                url = "https://github.com/pubiqq/lifecycleprops/blob/${LibraryConfig.Version}/LICENSE.txt"
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

    publishToMavenCentral()
    signAllPublications()
}
