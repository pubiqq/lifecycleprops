package com.pubiqq.lifecycleprops.internal.utils

internal fun <T> T.closeIfPossible() {
    if (PlatformUtils.isAutoCloseableAvailable() && this is AutoCloseable) {
        close()
    }
}