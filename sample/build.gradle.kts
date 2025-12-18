import com.pubiqq.lifecycleprops.buildlogic.common.Config as CommonConfig
import com.pubiqq.lifecycleprops.buildlogic.sample.Config as SampleConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.lifecycleprops.common)
    alias(libs.plugins.lifecycleprops.sample)
}

kotlin {
    jvmToolchain(CommonConfig.JdkVersion)
}

android {
    namespace = "com.pubiqq.lifecycleprops.sample"

    compileSdk = SampleConfig.CompileSdk
    buildToolsVersion = SampleConfig.BuildTools

    defaultConfig {
        applicationId = "com.pubiqq.lifecycleprops.sample"

        minSdk = SampleConfig.MinSdk
        targetSdk = SampleConfig.TargetSdk

        versionCode = 1
        versionName = "1.0"
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
        viewBinding = true
    }
}

dependencies {
    implementation(projects.lifecycleprops)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.navigation.fragment)

    implementation(libs.material)
    debugImplementation(libs.leakcanary)
}
