package com.pubiqq.lifecycleprops

import com.pubiqq.lifecycleprops.internal.DefaultAnyLifecycleAwareReadOnlyConfiguration
import com.pubiqq.lifecycleprops.internal.DefaultAnyLifecycleAwareReadWriteConfiguration

/**
 * The entry point to configure lifecycle-aware properties.
 */
@ExperimentalConfigurationApi
public object LifecycleProps {

    private var _defaultLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>? = null
    private var _defaultLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>? = null

    internal val defaultLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>
        get() = _defaultLifecycleAwareReadOnlyConfiguration ?: DefaultAnyLifecycleAwareReadOnlyConfiguration

    internal val defaultLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>
        get() = _defaultLifecycleAwareReadWriteConfiguration ?: DefaultAnyLifecycleAwareReadWriteConfiguration

    /**
     * Sets the configurations to be used in `lifecycleAware` property delegates by default.
     *
     * @param readOnlyPropsConfiguration The configuration for read-only `lifecycleAware` property
     *   delegates.
     * @param readWritePropsConfiguration The configuration for read/write `lifecycleAware` property
     *   delegates.
     */
    public fun setDefaultLifecycleAwareConfigurations(
        readOnlyPropsConfiguration: LifecycleAwareReadOnlyConfiguration<Any>,
        readWritePropsConfiguration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        setDefaultLifecycleAwareReadOnlyConfiguration(readOnlyPropsConfiguration)
        setDefaultLifecycleAwareReadWriteConfiguration(readWritePropsConfiguration)
    }

    /**
     * Sets the configuration to be used in read-only `lifecycleAware` property delegates
     * by default.
     *
     * @param configuration The configuration to set.
     */
    public fun setDefaultLifecycleAwareReadOnlyConfiguration(
        configuration: LifecycleAwareReadOnlyConfiguration<Any>
    ) {
        _defaultLifecycleAwareReadOnlyConfiguration = configuration
    }

    /**
     * Sets the configuration to be used in read/write `lifecycleAware` property delegates
     * by default.
     *
     * @param configuration The configuration to set.
     */
    public fun setDefaultLifecycleAwareReadWriteConfiguration(
        configuration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        _defaultLifecycleAwareReadWriteConfiguration = configuration
    }

    /**
     * Resets the configurations for `lifecycleAware` property delegates to their defaults.
     */
    public fun resetDefaultLifecycleAwareConfigurations() {
        resetDefaultLifecycleAwareReadOnlyConfiguration()
        resetDefaultLifecycleAwareReadWriteConfiguration()
    }

    /**
     * Resets the configuration for read-only `lifecycleAware` property delegates to the default.
     */
    public fun resetDefaultLifecycleAwareReadOnlyConfiguration() {
        _defaultLifecycleAwareReadOnlyConfiguration = null
    }

    /**
     * Resets the configuration for read/write `lifecycleAware` property delegates to the default.
     */
    public fun resetDefaultLifecycleAwareReadWriteConfiguration() {
        _defaultLifecycleAwareReadWriteConfiguration = null
    }
}