package com.pubiqq.lifecycleprops.build_logic.common.utils

import org.gradle.api.provider.Property

infix fun <T> Property<T>.by(value: T) {
    set(value)
}