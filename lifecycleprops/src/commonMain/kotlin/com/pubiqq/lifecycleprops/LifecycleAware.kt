package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.internal.LifecycleAwareReadOnlyProperty
import com.pubiqq.lifecycleprops.internal.LifecycleAwareReadWriteProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty

/**
 * Returns a property delegate for a read-only property that associates it with the [LifecycleOwner]
 * lifecycle.
 *
 * The delegate:
 * - Lazily initializes the property using the [initializer].
 * - Closes (if [AutoCloseable]) and nulls out the property when an [ON_DESTROY] event occurs.
 *
 * If the [initializer] throws an exception, it will attempt to reinitialize the value at next
 * access.
 *
 * @receiver The class whose lifecycle is observed.
 * @param initializer The property initialization function.
 * @param onCreate An optional callback invoked when an [ON_CREATE] event occurs.
 * @param onStart An optional callback invoked when an [ON_START] event occurs.
 * @param onResume An optional callback invoked when an [ON_RESUME] event occurs.
 * @param onPause An optional callback invoked when an [ON_PAUSE] event occurs.
 * @param onStop An optional callback invoked when an [ON_STOP] event occurs.
 * @param onDestroy An optional callback invoked when an [ON_DESTROY] event occurs.
 * @param onAny An optional callback invoked when any lifecycle event occurs.
 */
public fun <T : Any> LifecycleOwner.lifecycleAware(
    initializer: () -> T,
    onCreate: (T.() -> Unit)? = null,
    onStart: (T.() -> Unit)? = null,
    onResume: (T.() -> Unit)? = null,
    onPause: (T.() -> Unit)? = null,
    onStop: (T.() -> Unit)? = null,
    onDestroy: (T.() -> Unit)? = null,
    onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
): ReadOnlyProperty<LifecycleOwner, T> {
    return lifecycleAware(
        configuration = LifecycleProps.defaultLifecycleAwareReadOnlyConfiguration,
        initializer = initializer,
        onCreate = onCreate,
        onStart = onStart,
        onResume = onResume,
        onPause = onPause,
        onStop = onStop,
        onDestroy = onDestroy,
        onAny = onAny
    )
}

/**
 * Returns a property delegate for a read-only property that associates it with the [LifecycleOwner]
 * lifecycle.
 *
 * If the [initializer] throws an exception, it will attempt to reinitialize the value at next
 * access.
 *
 * @receiver The class whose lifecycle is observed.
 * @param configuration The configuration used for the delegate.
 * @param initializer The property initialization function.
 * @param onCreate An optional callback invoked when an [ON_CREATE] event occurs.
 * @param onStart An optional callback invoked when an [ON_START] event occurs.
 * @param onResume An optional callback invoked when an [ON_RESUME] event occurs.
 * @param onPause An optional callback invoked when an [ON_PAUSE] event occurs.
 * @param onStop An optional callback invoked when an [ON_STOP] event occurs.
 * @param onDestroy An optional callback invoked when an [ON_DESTROY] event occurs.
 * @param onAny An optional callback invoked when any lifecycle event occurs.
 */
@ExperimentalConfigurationApi
public fun <T : Any> LifecycleOwner.lifecycleAware(
    configuration: LifecycleAwareReadOnlyConfiguration<T>,
    initializer: () -> T,
    onCreate: (T.() -> Unit)? = null,
    onStart: (T.() -> Unit)? = null,
    onResume: (T.() -> Unit)? = null,
    onPause: (T.() -> Unit)? = null,
    onStop: (T.() -> Unit)? = null,
    onDestroy: (T.() -> Unit)? = null,
    onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
): ReadOnlyProperty<LifecycleOwner, T> {
    return LifecycleAwareReadOnlyProperty(
        lifecycleOwner = this,
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
}

/**
 * Returns a property delegate for a read/write property that associates it with the
 * [LifecycleOwner] lifecycle.
 *
 * The delegate:
 * - Ensures that a value will not be reassigned to an already initialized property. If you try to
 *   do this, an [IllegalStateException] will be thrown.
 * - Ensures that each provided event handler will be invoked for the property. If the property is
 *   not initialized at the time the handler is invoked, an [IllegalStateException] will be thrown.
 * - Closes (if [AutoCloseable]) and nulls out the property when an [ON_DESTROY] event occurs.
 *
 * @receiver The class whose lifecycle is observed.
 * @param onCreate An optional callback invoked when an [ON_CREATE] event occurs.
 * @param onStart An optional callback invoked when an [ON_START] event occurs.
 * @param onResume An optional callback invoked when an [ON_RESUME] event occurs.
 * @param onPause An optional callback invoked when an [ON_PAUSE] event occurs.
 * @param onStop An optional callback invoked when an [ON_STOP] event occurs.
 * @param onDestroy An optional callback invoked when an [ON_DESTROY] event occurs.
 * @param onAny An optional callback invoked when any lifecycle event occurs.
 */
public fun <T : Any> LifecycleOwner.lifecycleAware(
    onCreate: (T.() -> Unit)? = null,
    onStart: (T.() -> Unit)? = null,
    onResume: (T.() -> Unit)? = null,
    onPause: (T.() -> Unit)? = null,
    onStop: (T.() -> Unit)? = null,
    onDestroy: (T.() -> Unit)? = null,
    onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
): ReadWriteProperty<LifecycleOwner, T> {
    return lifecycleAware(
        configuration = LifecycleProps.defaultLifecycleAwareReadWriteConfiguration,
        onCreate = onCreate,
        onStart = onStart,
        onResume = onResume,
        onPause = onPause,
        onStop = onStop,
        onDestroy = onDestroy,
        onAny = onAny
    )
}

/**
 * Returns a property delegate for a read/write property that associates it with the
 * [LifecycleOwner] lifecycle.
 *
 * @receiver The class whose lifecycle is observed.
 * @param configuration The configuration used for the delegate.
 * @param onCreate An optional callback invoked when an [ON_CREATE] event occurs.
 * @param onStart An optional callback invoked when an [ON_START] event occurs.
 * @param onResume An optional callback invoked when an [ON_RESUME] event occurs.
 * @param onPause An optional callback invoked when an [ON_PAUSE] event occurs.
 * @param onStop An optional callback invoked when an [ON_STOP] event occurs.
 * @param onDestroy An optional callback invoked when an [ON_DESTROY] event occurs.
 * @param onAny An optional callback invoked when any lifecycle event occurs.
 */
@ExperimentalConfigurationApi
public fun <T : Any> LifecycleOwner.lifecycleAware(
    configuration: LifecycleAwareReadWriteConfiguration<T>,
    onCreate: (T.() -> Unit)? = null,
    onStart: (T.() -> Unit)? = null,
    onResume: (T.() -> Unit)? = null,
    onPause: (T.() -> Unit)? = null,
    onStop: (T.() -> Unit)? = null,
    onDestroy: (T.() -> Unit)? = null,
    onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
): ReadWriteProperty<LifecycleOwner, T> {
    return LifecycleAwareReadWriteProperty(
        lifecycleOwner = this,
        configuration = configuration,
        onCreate = onCreate,
        onStart = onStart,
        onResume = onResume,
        onPause = onPause,
        onStop = onStop,
        onDestroy = onDestroy,
        onAny = onAny
    )
}