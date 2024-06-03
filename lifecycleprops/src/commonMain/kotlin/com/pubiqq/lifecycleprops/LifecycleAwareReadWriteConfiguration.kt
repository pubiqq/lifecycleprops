package com.pubiqq.lifecycleprops

import com.pubiqq.lifecycleprops.internal.utils.closeIfPossible

/**
 * A configuration of lifecycle-aware delegates for read/write properties.
 */
@ExperimentalConfigurationApi
public interface LifecycleAwareReadWriteConfiguration<in T : Any> {

    /**
     * Whether to allow reassigning a value to an already initialized property. If `false`, an
     * [IllegalStateException] will be thrown when trying to reassign the value.
     */
    public val allowReassign: Boolean

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
     * Called at the end of the lifecycle, and also when reassigning a new value to the property (if
     * [allowReassign] is `true`)
     */
    public fun onClear(value: T)

    @Suppress("FunctionName")
    @ExperimentalConfigurationApi
    public companion object {

        /**
         * Creates a default configuration of lifecycle-aware delegates for read/write properties.
         * Delegate with this configuration:
         * - Throws an exception when trying to reassign a value to an already initialized property.
         * - Throws an exception when trying to invoke a lifecycle event handler with uninitialized
         *   property.
         * - Closes (if [AutoCloseable]) and nulls out the property value when an `ON_DESTROY` event
         *   occurs.
         */
        public fun <T : Any> Default(): LifecycleAwareReadWriteConfiguration<T> =
            DefaultLifecycleAwareReadWriteConfiguration()

        /**
         * Creates a configuration of lifecycle-aware delegates for read/write properties,
         * compatible with LifecycleProps v1. Delegate with this configuration:
         * - Allows reassigning a value to an already initialized property.
         * - Skips the call to the lifecycle handler if the property has not been initialized.
         * - Nulls out the property value when an `ON_DESTROY` event occurs (without auto-closing
         *   [AutoCloseable] properties).
         */
        public fun <T : Any> Legacy(): LifecycleAwareReadWriteConfiguration<T> =
            LegacyLifecycleAwareReadWriteConfiguration()
    }
}

private class DefaultLifecycleAwareReadWriteConfiguration<in T : Any> :
    LifecycleAwareReadWriteConfiguration<T> {

    override val allowReassign: Boolean = false

    override val allowSkipHandlerAccessToUninitializedProperty: Boolean = false

    override val shouldNullOutTheProperty: Boolean = true

    override fun onClear(value: T) {
        value.closeIfPossible()
    }
}

private class LegacyLifecycleAwareReadWriteConfiguration<in T : Any> :
    LifecycleAwareReadWriteConfiguration<T> {

    override val allowReassign: Boolean = true

    override val allowSkipHandlerAccessToUninitializedProperty: Boolean = true

    override val shouldNullOutTheProperty: Boolean = true

    override fun onClear(value: T) = Unit
}