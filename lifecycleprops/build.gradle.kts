// https://youtrack.jetbrains.com/issue/KTIJ-19369#focus=Comments-27-5181027.0-0
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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
            isMinifyEnabled = true
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
        freeCompilerArgs += mutableListOf<String>().apply {
            add("-opt-in=kotlin.RequiresOptIn")
            add("-opt-in=com.pubiqq.lifecycleprops.LifecycleAwareOptions")
            add("-Xexplicit-api=strict")
        }
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.common)
}