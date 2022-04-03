package com.pubiqq.lifecycleprops.internal

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.pubiqq.lifecycleprops.LifecycleAwareDeinitializationStrategy

internal class ReadWritePropertyLifecycleObserver<T : Any>(
    private val deinitializationStrategy: LifecycleAwareDeinitializationStrategy,
    private val onCreate: (T.() -> Unit)? = null,
    private val onStart: (T.() -> Unit)? = null,
    private val onResume: (T.() -> Unit)? = null,
    private val onPause: (T.() -> Unit)? = null,
    private val onStop: (T.() -> Unit)? = null,
    private val onDestroy: (T.() -> Unit)? = null,
    private val onAny: (T.(event: Lifecycle.Event) -> Unit)? = null
) : DefaultLifecycleObserver, LifecycleEventObserver {

    private var _value: T? = null

    var value: T
        get() = _value ?: throw IllegalStateException("Value is not available")
        set(value) {
            _value = value
        }

    override fun onCreate(owner: LifecycleOwner) {
        onCreate?.let { onCreate ->
            _value?.let { onCreate(it) }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        onStart?.let { onStart ->
            _value?.let { onStart(it) }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        onResume?.let { onResume ->
            _value?.let { onResume(it) }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        onPause?.let { onPause ->
            _value?.let { onPause(it) }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        onStop?.let { onStop ->
            _value?.let { onStop(it) }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        onDestroy?.let { onDestroy ->
            _value?.let { onDestroy(it) }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        onAny?.let { onAny ->
            _value?.let { onAny(it, event) }
        }

        if (event == Lifecycle.Event.ON_DESTROY
            && deinitializationStrategy == LifecycleAwareDeinitializationStrategy.OnDestroy
        ) {
            _value = null
        }
    }
}