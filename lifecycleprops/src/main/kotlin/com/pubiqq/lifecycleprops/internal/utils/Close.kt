package com.pubiqq.lifecycleprops.internal.utils

import android.os.Build

internal fun <T> T.closeIfPossible() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && this is AutoCloseable) {
        close()
    }
}