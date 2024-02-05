package com.pubiqq.lifecycleprops.sample.utils

import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge as androidxEnableEdgeToEdge

// https://android.googlesource.com/platform/frameworks/support/+/27e7d52e8604a080133e8b842db10c89b4482598/activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt#38
private val DefaultLightScrim = Color.argb(0xe6, 0xff, 0xff, 0xff)

// https://android.googlesource.com/platform/frameworks/support/+/27e7d52e8604a080133e8b842db10c89b4482598/activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt#44
private val DefaultDarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)

/**
 * Enables the edge-to-edge display for the [ComponentActivity].
 *
 * This method does the same as [androidx.activity.enableEdgeToEdge], but also extends the window
 * to the display cutout areas if possible.
 */
fun ComponentActivity.enableEdgeToEdge(
    statusBarStyle: SystemBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
    navigationBarStyle: SystemBarStyle = SystemBarStyle.auto(DefaultLightScrim, DefaultDarkScrim)
) {
    androidxEnableEdgeToEdge(statusBarStyle, navigationBarStyle)

    if (Build.VERSION.SDK_INT >= 30) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
    } else if (Build.VERSION.SDK_INT >= 28) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}