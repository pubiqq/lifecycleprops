package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.internal.LifecycleAwareReadOnlyProperty
import com.pubiqq.lifecycleprops.internal.LifecycleAwareReadWriteProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty

/**
 * Returns a property delegate for a read-only property that associates it
 * with the [LifecycleOwner] lifecycle.
 *
 * The delegate lazily initializes the property value using the [initializer] and clears it
 * when the [Lifecycle.Event.ON_DESTROY] event is reached. If the [initializer] throws an exception,
 * it will attempt to reinitialize the value at next access.
 *
 * @receiver The class whose lifecycle is observed.
 * @param initializer The property initialization function.
 * @param onCreate An optional callback that is called when [Lifecycle.Event.ON_CREATE] event occurs.
 * @param onStart An optional callback that is called when [Lifecycle.Event.ON_START] event occurs.
 * @param onResume An optional callback that is called when [Lifecycle.Event.ON_RESUME] event occurs.
 * @param onPause An optional callback that is called when [Lifecycle.Event.ON_PAUSE] event occurs.
 * @param onStop An optional callback that is called when [Lifecycle.Event.ON_STOP] event occurs.
 * @param onDestroy An optional callback that is called when [Lifecycle.Event.ON_DESTROY] event occurs.
 * @param onAny An optional callback that is called when any lifecycle event occurs.
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
        options = LifecycleAwareReadOnlyOptions(
            initializationStrategy = LifecycleAwareInitializationStrategy.Lazy,
            deinitializationStrategy = LifecycleAwareDeinitializationStrategy.OnDestroy
        ),
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
 * Returns a property delegate for a read-only property that associates it
 * with the [LifecycleOwner] lifecycle.
 *
 * @receiver The class whose lifecycle is observed.
 * @param options The options used for `lifecycleAware` delegate.
 * @param initializer The property initialization function.
 * @param onCreate An optional callback that is called when [Lifecycle.Event.ON_CREATE] event occurs.
 * @param onStart An optional callback that is called when [Lifecycle.Event.ON_START] event occurs.
 * @param onResume An optional callback that is called when [Lifecycle.Event.ON_RESUME] event occurs.
 * @param onPause An optional callback that is called when [Lifecycle.Event.ON_PAUSE] event occurs.
 * @param onStop An optional callback that is called when [Lifecycle.Event.ON_STOP] event occurs.
 * @param onDestroy An optional callback that is called when [Lifecycle.Event.ON_DESTROY] event occurs.
 * @param onAny An optional callback that is called when any lifecycle event occurs.
 */
@LifecycleAwareOptions
public fun <T : Any> LifecycleOwner.lifecycleAware(
    options: LifecycleAwareReadOnlyOptions,
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
        options = options,
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
 * Returns a property delegate for a read/write property that associates it
 * with the [LifecycleOwner] lifecycle and clears the property value
 * when the [Lifecycle.Event.ON_DESTROY] event is reached.
 *
 * @receiver The class whose lifecycle is observed.
 * @param onCreate An optional callback that is called when [Lifecycle.Event.ON_CREATE] event occurs.
 * @param onStart An optional callback that is called when [Lifecycle.Event.ON_START] event occurs.
 * @param onResume An optional callback that is called when [Lifecycle.Event.ON_RESUME] event occurs.
 * @param onPause An optional callback that is called when [Lifecycle.Event.ON_PAUSE] event occurs.
 * @param onStop An optional callback that is called when [Lifecycle.Event.ON_STOP] event occurs.
 * @param onDestroy An optional callback that is called when [Lifecycle.Event.ON_DESTROY] event occurs.
 * @param onAny An optional callback that is called when any lifecycle event occurs.
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
        options = LifecycleAwareReadWriteOptions(
            deinitializationStrategy = LifecycleAwareDeinitializationStrategy.OnDestroy
        ),
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
 * Returns a property delegate for a read/write property that makes it
 * [aware](https://developer.android.com/topic/libraries/architecture/lifecycle)
 * of the [LifecycleOwner] lifecycle.
 *
 * @receiver The class whose lifecycle is observed.
 * @param options The options used for `lifecycleAware` delegate.
 * @param onCreate An optional callback that is called when [Lifecycle.Event.ON_CREATE] event occurs.
 * @param onStart An optional callback that is called when [Lifecycle.Event.ON_START] event occurs.
 * @param onResume An optional callback that is called when [Lifecycle.Event.ON_RESUME] event occurs.
 * @param onPause An optional callback that is called when [Lifecycle.Event.ON_PAUSE] event occurs.
 * @param onStop An optional callback that is called when [Lifecycle.Event.ON_STOP] event occurs.
 * @param onDestroy An optional callback that is called when [Lifecycle.Event.ON_DESTROY] event occurs.
 * @param onAny An optional callback that is called when any lifecycle event occurs.
 */
@LifecycleAwareOptions
public fun <T : Any> LifecycleOwner.lifecycleAware(
    options: LifecycleAwareReadWriteOptions,
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
        options = options,
        onCreate = onCreate,
        onStart = onStart,
        onResume = onResume,
        onPause = onPause,
        onStop = onStop,
        onDestroy = onDestroy,
        onAny = onAny
    )
}