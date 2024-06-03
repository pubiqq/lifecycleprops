package com.pubiqq.lifecycleprops.internal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.LifecycleAwareReadWriteConfiguration
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class LifecycleAwareReadWriteProperty<T : Any>(
    lifecycleOwner: LifecycleOwner,
    configuration: LifecycleAwareReadWriteConfiguration<T>,
    onCreate: (T.() -> Unit)? = null,
    onStart: (T.() -> Unit)? = null,
    onResume: (T.() -> Unit)? = null,
    onPause: (T.() -> Unit)? = null,
    onStop: (T.() -> Unit)? = null,
    onDestroy: (T.() -> Unit)? = null,
    onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
) : ReadWriteProperty<LifecycleOwner, T> {

    private val propertyLifecycleObserver = ReadWritePropertyLifecycleObserver(
        configuration = configuration,
        onCreate = onCreate,
        onStart = onStart,
        onResume = onResume,
        onPause = onPause,
        onStop = onStop,
        onDestroy = onDestroy,
        onAny = onAny
    )

    // Exists for testing purposes only
    internal var value: T
        get() = propertyLifecycleObserver.value
        set(value) {
            propertyLifecycleObserver.value = value
        }

    // Exists for testing purposes only
    internal val rawValue: T?
        get() = propertyLifecycleObserver.rawValue

    init {
        lifecycleOwner.lifecycle.addObserver(propertyLifecycleObserver)
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
        this.value = value
    }
}