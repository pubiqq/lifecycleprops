package com.pubiqq.lifecycleprops

/**
 * Specifies the strategy for clearing the property value.
 */
@LifecycleAwareOptions
public enum class LifecycleAwareDeinitializationStrategy {
    /**
     * No clearing actions will be carried out.
     */
    None,

    /**
     * The property value will be cleared when ON_DESTROY is reached.
     */
    OnDestroy
}