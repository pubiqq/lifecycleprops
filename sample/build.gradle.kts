import com.pubiqq.lifecycleprops.build_logic.common.toJavaVersion
import com.pubiqq.lifecycleprops.build_logic.common.Config as CommonConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.lifecycleprops.common)
}

kotlin {
    compilerOptions {
        jvmTarget = CommonConfig.JvmTarget
    }
}

android {
    namespace = "com.pubiqq.lifecycleprops.sample"

    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "com.pubiqq.lifecycleprops.sample"

        minSdk = 21
        targetSdk = 34

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

    compileOptions {
        sourceCompatibility = CommonConfig.JvmTarget.toJavaVersion()
        targetCompatibility = CommonConfig.JvmTarget.toJavaVersion()
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