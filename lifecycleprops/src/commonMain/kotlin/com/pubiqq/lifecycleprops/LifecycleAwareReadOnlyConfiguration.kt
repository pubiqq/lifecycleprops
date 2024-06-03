package com.pubiqq.lifecycleprops

import com.pubiqq.lifecycleprops.internal.utils.closeIfPossible

/**
 * A configuration of lifecycle-aware delegates for read-only properties.
 */
@ExperimentalConfigurationApi
public interface LifecycleAwareReadOnlyConfiguration<in T : Any> {

    /**
     * A strategy to initialize the property.
     */
    public val initializationStrategy: LifecycleAwareInitializationStrategy

    /**
     * Whether to allow a call to the lifecycle event handler to be skipped if the property value is
     * not initialized. If `false`, an [IllegalStateException] will be thrown when trying to invoke
     * the handler.
     */
    public val allowSkipHandlerAccessToUninitializedProperty: Boolean

    /**
     * Whether to null out the property value at the end of the lifecycle.
     */
    public val shouldNullOutTheProperty: Boolean

    /**
     * Called at the end of the lifecycle.
     */
    public fun onClear(value: T)

    @Suppress("FunctionName")
    @ExperimentalConfigurationApi
    public companion object {

        /**
         * Creates a default configuration of lifecycle-aware delegates for read-only properties.
         *
         * Delegate with this configuration:
         * - Lazily initializes the property value.
         * - Closes (if [AutoCloseable]) and nulls out the property value when an `ON_DESTROY` event
         *   occurs.
         */
        public fun <T : Any> Default(): LifecycleAwareReadOnlyConfiguration<T> =
            DefaultLifecycleAwareReadOnlyConfiguration()

        /**
         * Creates a configuration of lifecycle-aware delegates for read-only properties, compatible
         * with LifecycleProps v1.
         *
         * Delegate with this configuration:
         * - Lazily initializes the property value.
         * - Nulls out the property value when an `ON_DESTROY` event occurs (without auto-closing
         *   [AutoCloseable] properties).
         */
        public fun <T : Any> Legacy(): LifecycleAwareReadOnlyConfiguration<T> =
            LegacyLifecycleAwareReadOnlyConfiguration()
    }
}

internal class DefaultLifecycleAwareReadOnlyConfiguration<in T : Any> :
    LifecycleAwareReadOnlyConfiguration<T> {

    override val initializationStrategy: LifecycleAwareInitializationStrategy =
        LifecycleAwareInitializationStrategy.OnAnyAccess

    override val allowSkipHandlerAccessToUninitializedProperty: Boolean = false

    override val shouldNullOutTheProperty: Boolean = true

    override fun onClear(value: T) {
        value.closeIfPossible()
    }
}

internal class LegacyLifecycleAwareReadOnlyConfiguration<in T : Any> :
    LifecycleAwareReadOnlyConfiguration<T> {

    override val initializationStrategy: LifecycleAwareInitializationStrategy =
        LifecycleAwareInitializationStrategy.OnAnyAccess

    override val allowSkipHandlerAccessToUninitializedProperty: Boolean = true

    override val shouldNullOutTheProperty: Boolean = true

    override fun onClear(value: T) = Unit
}