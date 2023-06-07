// https://youtrack.jetbrains.com/issue/KTIJ-19369#focus=Comments-27-5181027.0-0
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
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