import com.pubiqq.lifecycleprops.build_logic.common.Config as CommonConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.pubiqq.lifecycleprops.build_logic.common")
}

android {
    namespace = "com.pubiqq.lifecycleprops.sample"

    compileSdk = 33
    buildToolsVersion = "33.0.2"

    defaultConfig {
        applicationId = "com.pubiqq.lifecycleprops.sample"

        minSdk = 21
        targetSdk = 33

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
        sourceCompatibility = CommonConfig.JvmTarget
        targetCompatibility = CommonConfig.JvmTarget
    }

    kotlinOptions {
        jvmTarget = CommonConfig.JvmTarget.toString()
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