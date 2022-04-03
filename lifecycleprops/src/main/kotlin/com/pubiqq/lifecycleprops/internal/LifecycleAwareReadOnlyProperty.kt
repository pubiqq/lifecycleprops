package com.pubiqq.lifecycleprops.internal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.LifecycleAwareReadOnlyOptions
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class LifecycleAwareReadOnlyProperty<T : Any>(
    lifecycleOwner: LifecycleOwner,
    options: LifecycleAwareReadOnlyOptions,
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
        initializationStrategy = options.initializationStrategy,
        deinitializationStrategy = options.deinitializationStrategy,
        initializer = initializer,
        onCreate = onCreate,
        onStart = onStart,
        onResume = onResume,
        onPause = onPause,
        onStop = onStop,
        onDestroy = onDestroy,
        onAny = onAny
    )

    init {
        lifecycleOwner.lifecycle.addObserver(propertyLifecycleObserver)
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        return propertyLifecycleObserver.value
    }
}