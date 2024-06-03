package com.pubiqq.lifecycleprops.internal

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.pubiqq.lifecycleprops.LifecycleAwareReadOnlyConfiguration
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class ViewLifecycleAwareReadOnlyProperty<T : Any>(
    fragment: Fragment,
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
    @Suppress("MemberVisibilityCanBePrivate")
    internal val value: T
        get() = propertyLifecycleObserver.value

    // Exists for testing purposes only
    @Suppress("unused")
    internal val rawValue: T?
        get() = propertyLifecycleObserver.rawValue

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerObserver = Observer<LifecycleOwner?> { lifecycleOwner ->
                val viewLifecycleOwner = lifecycleOwner ?: return@Observer

                viewLifecycleOwner.lifecycle.addObserver(propertyLifecycleObserver)
            }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerObserver)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerObserver)
            }
        })
    }

    // Exists for testing purposes only
    @Suppress("unused")
    internal fun initialize() {
        propertyLifecycleObserver.initialize()
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        return value
    }
}