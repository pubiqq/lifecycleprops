package com.pubiqq.lifecycleprops.internal

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.pubiqq.lifecycleprops.LifecycleAwareReadWriteOptions
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class ViewLifecycleAwareReadWriteProperty<T : Any>(
    fragment: Fragment,
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

    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
        propertyLifecycleObserver.value = value
    }
}