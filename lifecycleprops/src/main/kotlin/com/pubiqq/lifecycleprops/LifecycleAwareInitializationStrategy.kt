package com.pubiqq.lifecycleprops

/**
 * Specifies the strategy to initialize the property.
 */
@LifecycleAwareConfigurationApi
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
     * [LifecycleAwareReadWriteConfiguration.allowSkipHandlerAccessToUninitializedProperty] option.
     *
     * If the initializer throws an exception, it will attempt to reinitialize the property value at
     * next access.
     */
    OnPropertyAccess,

    /**
     * The property initializer will be called the first time the property is accessed, including if
     * it's initiated by the lifecycle event handler to get the receiver value.
     *
     * If the initializer throws an exception, it will attempt to reinitialize the property value at
     * next access.
     */
    OnAnyAccess
}