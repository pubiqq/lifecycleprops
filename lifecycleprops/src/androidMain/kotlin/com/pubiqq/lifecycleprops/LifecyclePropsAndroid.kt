package com.pubiqq.lifecycleprops

import androidx.annotation.MainThread
import com.pubiqq.lifecycleprops.internal.DefaultAnyLifecycleAwareReadOnlyConfiguration
import com.pubiqq.lifecycleprops.internal.DefaultAnyLifecycleAwareReadWriteConfiguration

/**
 * The entry point to configure Android-specific lifecycle-aware properties.
 */
@ExperimentalConfigurationApi
@MainThread
public object LifecyclePropsAndroid {

    private var _defaultViewLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>? = null
    private var _defaultViewLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>? = null

    internal val defaultViewLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>
        get() = _defaultViewLifecycleAwareReadOnlyConfiguration ?: DefaultAnyLifecycleAwareReadOnlyConfiguration

    internal val defaultViewLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>
        get() = _defaultViewLifecycleAwareReadWriteConfiguration ?: DefaultAnyLifecycleAwareReadWriteConfiguration

    /**
     * Sets the configurations to be used in `viewLifecycleAware` property delegates by default.
     *
     * @param readOnlyPropsConfiguration The configuration for read-only `viewLifecycleAware`
     *   property delegates.
     * @param readWritePropsConfiguration The configuration for read/write `viewLifecycleAware`
     *   property delegates.
     */
    public fun setDefaultViewLifecycleAwareConfigurations(
        readOnlyPropsConfiguration: LifecycleAwareReadOnlyConfiguration<Any>,
        readWritePropsConfiguration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        setDefaultViewLifecycleAwareReadOnlyConfiguration(readOnlyPropsConfiguration)
        setDefaultViewLifecycleAwareReadWriteConfiguration(readWritePropsConfiguration)
    }

    /**
     * Sets the configuration to be used in read-only `viewLifecycleAware` property delegates
     * by default.
     *
     * @param configuration The configuration to set.
     */
    public fun setDefaultViewLifecycleAwareReadOnlyConfiguration(
        configuration: LifecycleAwareReadOnlyConfiguration<Any>
    ) {
        _defaultViewLifecycleAwareReadOnlyConfiguration = configuration
    }

    /**
     * Sets the configuration to be used in read/write `viewLifecycleAware` property delegates
     * by default.
     *
     * @param configuration The configuration to set.
     */
    public fun setDefaultViewLifecycleAwareReadWriteConfiguration(
        configuration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        _defaultViewLifecycleAwareReadWriteConfiguration = configuration
    }

    /**
     * Resets the configurations for `viewLifecycleAware` property delegates to their defaults.
     */
    public fun resetDefaultViewLifecycleAwareConfigurations() {
        resetDefaultViewLifecycleAwareReadOnlyConfiguration()
        resetDefaultViewLifecycleAwareReadWriteConfiguration()
    }

    /**
     * Resets the configuration for read-only `viewLifecycleAware` property delegates to the default.
     */
    public fun resetDefaultViewLifecycleAwareReadOnlyConfiguration() {
        _defaultViewLifecycleAwareReadOnlyConfiguration = null
    }

    /**
     * Resets the configuration for read/write `viewLifecycleAware` property delegates to the default.
     */
    public fun resetDefaultViewLifecycleAwareReadWriteConfiguration() {
        _defaultViewLifecycleAwareReadWriteConfiguration = null
    }
}