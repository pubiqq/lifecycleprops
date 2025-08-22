package com.pubiqq.lifecycleprops.internal

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.LifecycleAwareInitializationStrategy
import com.pubiqq.lifecycleprops.LifecycleAwareReadOnlyConfiguration

internal class ReadOnlyPropertyLifecycleObserver<T : Any>(
    private val configuration: LifecycleAwareReadOnlyConfiguration<T>,
    private val initializer: () -> T,
    private val onCreate: (T.() -> Unit)? = null,
    private val onStart: (T.() -> Unit)? = null,
    private val onResume: (T.() -> Unit)? = null,
    private val onPause: (T.() -> Unit)? = null,
    private val onStop: (T.() -> Unit)? = null,
    private val onDestroy: (T.() -> Unit)? = null,
    private val onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
) : DefaultLifecycleObserver {

    // Declared internal for testing purposes only
    internal var rawValue: T? = null
        private set(value) {
            val oldValue = field
            if (oldValue !== value && oldValue != null) {
                configuration.onClear(oldValue)
            }

            field = value
        }

    internal val value: T
        get() {
            rawValue?.let { return it }

            return when (configuration.initializationStrategy) {
                LifecycleAwareInitializationStrategy.OnInit -> {
                    error("The property is not initialized")
                }
                LifecycleAwareInitializationStrategy.OnPropertyAccess,
                LifecycleAwareInitializationStrategy.OnAnyAccess -> {
                    initializer().also { rawValue = it }
                }
            }
        }

    private val valueForHandlers: T?
        get() {
            rawValue?.let { return it }

            return when (configuration.initializationStrategy) {
                LifecycleAwareInitializationStrategy.OnInit -> {
                    error("The property is not initialized")
                }
                LifecycleAwareInitializationStrategy.OnPropertyAccess -> {
                    if (configuration.allowSkipHandlerAccessToUninitializedProperty) {
                        null
                    } else {
                        error("The property is not initialized")
                    }
                }
                LifecycleAwareInitializationStrategy.OnAnyAccess -> {
                    initializer().also { rawValue = it }
                }
            }
        }

    init {
        if (configuration.initializationStrategy == LifecycleAwareInitializationStrategy.OnInit) {
            rawValue = initializer()
        }
    }

    // Exists for testing purposes only
    internal fun initialize() {
        if (rawValue == null) {
            rawValue = initializer()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        invokeCallback(onCreate)
        invokeCallback(onAny, Lifecycle.Event.ON_CREATE)
    }

    override fun onStart(owner: LifecycleOwner) {
        invokeCallback(onStart)
        invokeCallback(onAny, Lifecycle.Event.ON_START)
    }

    override fun onResume(owner: LifecycleOwner) {
        invokeCallback(onResume)
        invokeCallback(onAny, Lifecycle.Event.ON_RESUME)
    }

    override fun onPause(owner: LifecycleOwner) {
        invokeCallback(onPause)
        invokeCallback(onAny, Lifecycle.Event.ON_PAUSE)
    }

    override fun onStop(owner: LifecycleOwner) {
        invokeCallback(onStop)
        invokeCallback(onAny, Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        invokeCallback(onDestroy)
        invokeCallback(onAny, Lifecycle.Event.ON_DESTROY)

        if (configuration.shouldNullOutTheProperty) {
            rawValue = null
        }
    }

    private fun invokeCallback(callback: (T.() -> Unit)?) {
        callback?.let { callback ->
            valueForHandlers?.let { value -> callback(value) }
        }
    }

    private fun invokeCallback(callback: (T.(event: Lifecycle.Event) -> Unit)?, event: Lifecycle.Event) {
        callback?.let { callback ->
            valueForHandlers?.let { value -> callback(value, event) }
        }
    }
}