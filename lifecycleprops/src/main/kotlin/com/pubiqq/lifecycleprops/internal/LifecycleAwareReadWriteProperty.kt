package com.pubiqq.lifecycleprops.internal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.LifecycleAwareReadWriteOptions
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class LifecycleAwareReadWriteProperty<T : Any>(
    lifecycleOwner: LifecycleOwner,
    options: LifecycleAwareReadWriteOptions,
    onCreate: (T.() -> Unit)? = null,
    onStart: (T.() -> Unit)? = null,
    onResume: (T.() -> Unit)? = null,
    onPause: (T.() -> Unit)? = null,
    onStop: (T.() -> Unit)? = null,
    onDestroy: (T.() -> Unit)? = null,
    onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
) : ReadWriteProperty<LifecycleOwner, T> {

    private val propertyLifecycleObserver = ReadWritePropertyLifecycleObserver(
        deinitializationStrategy = options.deinitializationStrategy,
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

    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
        propertyLifecycleObserver.value = value
    }
}