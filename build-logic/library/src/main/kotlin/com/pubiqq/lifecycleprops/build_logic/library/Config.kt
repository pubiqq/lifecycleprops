package com.pubiqq.lifecycleprops.build_logic.library

import org.gradle.api.JavaVersion

object Config {
    const val Group = "io.github.pubiqq"
    const val Version = "2.1.0-SNAPSHOT"

    const val MinSdk = 14
    const val CompileSdk = 33
    const val BuildTools = "33.0.2"

    val JvmTarget = JavaVersion.VERSION_11
}