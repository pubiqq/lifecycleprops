package com.pubiqq.lifecycleprops.internal

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
) : DefaultLifecycleObserver, LifecycleEventObserver {

    private var _value: T? = null
        set(value) {
            val oldValue = field
            if (oldValue !== value && oldValue != null) {
                configuration.onClear(oldValue)
            }

            field = value
        }

    val value: T
        get() {
            _value?.let { return it }

            return when (configuration.initializationStrategy) {
                LifecycleAwareInitializationStrategy.OnInit -> {
                    error("The property is not initialized")
                }
                LifecycleAwareInitializationStrategy.OnPropertyAccess,
                LifecycleAwareInitializationStrategy.OnAnyAccess -> {
                    initializer().also { _value = it }
                }
            }
        }

    private val valueForHandlers: T?
        get() {
            _value?.let { return it }

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
                    initializer().also { _value = it }
                }
            }
        }

    init {
        if (configuration.initializationStrategy == LifecycleAwareInitializationStrategy.OnInit) {
            _value = initializer()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        onCreate?.let { onCreate ->
            valueForHandlers?.let { value -> onCreate(value) }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        onStart?.let { onStart ->
            valueForHandlers?.let { value -> onStart(value) }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        onResume?.let { onResume ->
            valueForHandlers?.let { value -> onResume(value) }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        onPause?.let { onPause ->
            valueForHandlers?.let { value -> onPause(value) }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        onStop?.let { onStop ->
            valueForHandlers?.let { value -> onStop(value) }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy?.let { onDestroy ->
            valueForHandlers?.let { value -> onDestroy(value) }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        onAny?.let { onAny ->
            valueForHandlers?.let { value -> onAny(value, event) }
        }

        if (event == Lifecycle.Event.ON_DESTROY) {
            if (configuration.shouldNullOutTheProperty) {
                _value = null
            }
        }
    }
}