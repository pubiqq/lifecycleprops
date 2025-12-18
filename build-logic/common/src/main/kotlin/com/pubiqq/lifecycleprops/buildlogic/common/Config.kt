package com.pubiqq.lifecycleprops.buildlogic.common

import org.jetbrains.kotlin.gradle.dsl.JvmTarget as GradleJvmTarget

object Config {
    const val MinSdk = 23
    const val CompileSdk = 36
    const val BuildTools = "36.0.0"

    val JvmTarget = GradleJvmTarget.JVM_17
}
