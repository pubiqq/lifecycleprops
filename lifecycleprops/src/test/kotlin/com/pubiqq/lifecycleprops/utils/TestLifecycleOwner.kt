package com.pubiqq.lifecycleprops.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * An implementation of [LifecycleOwner] that enables changing the internal lifecycle state manually.
 *
 * @param initialState The initial [Lifecycle.State].
 */
internal class TestLifecycleOwner(
    initialState: Lifecycle.State = Lifecycle.State.INITIALIZED
) : LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry.createUnsafe(this).apply {
        currentState = initialState
    }

    override val lifecycle: LifecycleRegistry
        get() = lifecycleRegistry

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        lifecycleRegistry.handleLifecycleEvent(event)
    }
}