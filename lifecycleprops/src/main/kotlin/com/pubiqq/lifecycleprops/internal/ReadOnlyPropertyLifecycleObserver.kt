package com.pubiqq.lifecycleprops.internal

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.LifecycleAwareDeinitializationStrategy
import com.pubiqq.lifecycleprops.LifecycleAwareInitializationStrategy

internal class ReadOnlyPropertyLifecycleObserver<T : Any>(
    private val initializationStrategy: LifecycleAwareInitializationStrategy,
    private val deinitializationStrategy: LifecycleAwareDeinitializationStrategy,
    private val initializer: () -> T,
    private val onCreate: (T.() -> Unit)? = null,
    private val onStart: (T.() -> Unit)? = null,
    private val onResume: (T.() -> Unit)? = null,
    private val onPause: (T.() -> Unit)? = null,
    private val onStop: (T.() -> Unit)? = null,
    private val onDestroy: (T.() -> Unit)? = null,
    private val onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
) : DefaultLifecycleObserver, LifecycleEventObserver {

    private var _value: T? = null

    val value: T
        get() {
            _value?.let { return it }

            return when (initializationStrategy) {
                LifecycleAwareInitializationStrategy.Eager -> {
                    throw IllegalStateException("Value is not initialized")
                }
                LifecycleAwareInitializationStrategy.Lazy -> {
                    initializer().also { _value = it }
                }
            }
        }

    init {
        if (initializationStrategy == LifecycleAwareInitializationStrategy.Eager) {
            _value = initializer()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        onCreate?.let { onCreate -> onCreate(value) }
    }

    override fun onStart(owner: LifecycleOwner) {
        onStart?.let { onStart -> onStart(value) }
    }

    override fun onResume(owner: LifecycleOwner) {
        onResume?.let { onResume -> onResume(value) }
    }

    override fun onPause(owner: LifecycleOwner) {
        onPause?.let { onPause -> onPause(value) }
    }

    override fun onStop(owner: LifecycleOwner) {
        onStop?.let { onStop -> onStop(value) }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy?.let { onDestroy -> onDestroy(value) }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        onAny?.let { onAny -> onAny(value, event) }

        if (event == Lifecycle.Event.ON_DESTROY
            && deinitializationStrategy == LifecycleAwareDeinitializationStrategy.OnDestroy
        ) {
            _value = null
        }
    }
}