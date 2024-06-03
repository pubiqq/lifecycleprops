package com.pubiqq.lifecycleprops.internal.utils

internal fun <T> T.closeIfPossible() {
    if (this is AutoCloseable) {
        close()
    }
}