package com.pubiqq.lifecycleprops.utils

import androidx.lifecycle.Lifecycle
import kotlin.jvm.JvmInline

@JvmInline
internal value class Event(private val name: String) {

    override fun toString(): String {
        return "Event: $name"
    }

    companion object {

        // === Lifecycle events

        val onCreate = Event("onCreate")
        val onStart = Event("onStart")
        val onResume = Event("onResume")
        val onPause = Event("onPause")
        val onStop = Event("onStop")
        val onDestroy = Event("onDestroy")

        fun onAny(event: Lifecycle.Event): Event =
            Event("onAny($event)")

        // === Other events

        val onInitialize = Event("onInitialize")
        val onClose = Event("onClose")
    }
}