package com.pubiqq.lifecycleprops.internal

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.pubiqq.lifecycleprops.LifecycleAwareReadOnlyOptions
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class ViewLifecycleAwareReadOnlyProperty<T : Any>(
    fragment: Fragment,
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

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        return propertyLifecycleObserver.value
    }
}