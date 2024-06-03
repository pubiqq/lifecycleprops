package com.pubiqq.lifecycleprops.internal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.LifecycleAwareReadOnlyConfiguration
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class LifecycleAwareReadOnlyProperty<T : Any>(
    lifecycleOwner: LifecycleOwner,
    configuration: LifecycleAwareReadOnlyConfiguration<T>,
    initializer: () -> T,
    onCreate: (T.() -> Unit)? = null,
    onStart: (T.() -> Unit)? = null,
    onResume: (T.() -> Unit)? = null,
    onPause: (T.() -> Unit)? = null,
    onStop: (T.() -> Unit)? = null,
    onDestroy: (T.() -> Unit)? = null,
    onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
) : ReadOnlyProperty<LifecycleOwner, T> {

    private val propertyLifecycleObserver = ReadOnlyPropertyLifecycleObserver(
        configuration = configuration,
        initializer = initializer,
        onCreate = onCreate,
        onStart = onStart,
        onResume = onResume,
        onPause = onPause,
        onStop = onStop,
        onDestroy = onDestroy,
        onAny = onAny
    )

    // Exists for testing purposes only
    internal val value: T
        get() = propertyLifecycleObserver.value

    // Exists for testing purposes only
    internal val rawValue: T?
        get() = propertyLifecycleObserver.rawValue

    init {
        lifecycleOwner.lifecycle.addObserver(propertyLifecycleObserver)
    }

    // Exists for testing purposes only
    internal fun initialize() {
        propertyLifecycleObserver.initialize()
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        return value
    }
}