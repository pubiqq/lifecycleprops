package com.pubiqq.lifecycleprops

/**
 * Specifies the strategy to initialize the property.
 */
@ExperimentalConfigurationApi
public enum class LifecycleAwareInitializationStrategy {
    /**
     * The property initializer will be called immediately when the delegate is initialized.
     */
    OnInit,

    /**
     * The property initializer will be called the first time the property is accessed _explicitly_.
     *
     * The initializer will not be called if the property access is initiated by the lifecycle event
     * handler when trying to get the receiver value. The behavior in this case is determined by the
     * [LifecycleAwareReadOnlyConfiguration.allowSkipHandlerAccessToUninitializedProperty] option.
     *
     * If the initializer throws an exception, the delegate will retry initializing the property
     * the next time it is accessed.
     */
    OnPropertyAccess,

    /**
     * The property initializer will be called the first time the property is accessed, including if
     * it's initiated by the lifecycle event handler to get the receiver value.
     *
     * If the initializer throws an exception, the delegate will retry initializing the property
     * the next time it is accessed.
     */
    OnAnyAccess
}