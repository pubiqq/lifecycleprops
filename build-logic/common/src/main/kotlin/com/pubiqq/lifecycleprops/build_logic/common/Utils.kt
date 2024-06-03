package com.pubiqq.lifecycleprops.build_logic.common

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

fun JvmTarget.toJavaVersion(): JavaVersion {
    return JavaVersion.toVersion(target)
}