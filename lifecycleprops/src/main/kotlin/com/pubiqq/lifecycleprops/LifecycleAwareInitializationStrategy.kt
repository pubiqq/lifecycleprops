package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle

/**
 * Specifies the initialization strategy for the property value.
 */
@LifecycleAwareOptions
public enum class LifecycleAwareInitializationStrategy {
    /**
     * The property initializer will be called immediately when the delegate is initialized.
     */
    Eager,

    /**
     * The property initializer will be called the first time the property is accessed.
     * If the initializer throws an exception, it will attempt to reinitialize the value
     * at next access.
     *
     * Note that handling lifecycle methods also requires accessing the property.
     * It follows that, for example, if we access the property value when
     * [Lifecycle.Event.ON_RESUME] event is reached, but we also have [Lifecycle.Event.ON_START]
     * handler, the initializer will be called when the [Lifecycle.Event.ON_START] event
     * is reached.
     */
    Lazy
}