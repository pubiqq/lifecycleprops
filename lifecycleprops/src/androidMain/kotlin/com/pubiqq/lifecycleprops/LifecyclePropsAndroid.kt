package com.pubiqq.lifecycleprops

/**
 * The entry point to configure android-specific lifecycle-aware properties.
 */
@ExperimentalConfigurationApi
public object LifecyclePropsAndroid {

    private var _defaultViewLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>? = null
    private var _defaultViewLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>? = null

    internal val defaultViewLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>
        get() = _defaultViewLifecycleAwareReadOnlyConfiguration ?: LifecycleAwareReadOnlyConfiguration.Default()

    internal val defaultViewLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>
        get() = _defaultViewLifecycleAwareReadWriteConfiguration ?: LifecycleAwareReadWriteConfiguration.Default()

    /**
     * Sets the configurations to be used in `viewLifecycleAware` property delegates by default.
     *
     * @param readOnlyPropsConfiguration The configuration for read-only `viewLifecycleAware`
     *   property delegates.
     * @param readWritePropsConfiguration The configuration for read/write `viewLifecycleAware`
     *   property delegates.
     */
    public fun setViewLifecycleAwareConfigurations(
        readOnlyPropsConfiguration: LifecycleAwareReadOnlyConfiguration<Any>,
        readWritePropsConfiguration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        setReadOnlyViewLifecycleAwareConfiguration(readOnlyPropsConfiguration)
        setReadWriteViewLifecycleAwareConfiguration(readWritePropsConfiguration)
    }

    /**
     * Sets the configuration to be used in read-only `viewLifecycleAware` property delegates
     * by default.
     *
     * @param configuration The configuration to set.
     */
    public fun setReadOnlyViewLifecycleAwareConfiguration(
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
    public fun setReadWriteViewLifecycleAwareConfiguration(
        configuration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        _defaultViewLifecycleAwareReadWriteConfiguration = configuration
    }

    /**
     * Sets the default configurations for `viewLifecycleAware` property delegates.
     */
    public fun resetViewLifecycleAwareConfigurations() {
        resetReadOnlyViewLifecycleAwareConfigurations()
        resetReadWriteViewLifecycleAwareConfigurations()
    }

    /**
     * Sets the default configuration for read-only `viewLifecycleAware` property delegates.
     */
    public fun resetReadOnlyViewLifecycleAwareConfigurations() {
        _defaultViewLifecycleAwareReadOnlyConfiguration = null
    }

    /**
     * Sets the default configuration for read/write `viewLifecycleAware` property delegates.
     */
    public fun resetReadWriteViewLifecycleAwareConfigurations() {
        _defaultViewLifecycleAwareReadWriteConfiguration = null
    }
}