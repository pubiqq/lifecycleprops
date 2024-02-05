package com.pubiqq.lifecycleprops.internal.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

// Declared internal for testing purposes only
internal object PlatformUtils {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.KITKAT)
    internal fun isAutoCloseableAvailable(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
}