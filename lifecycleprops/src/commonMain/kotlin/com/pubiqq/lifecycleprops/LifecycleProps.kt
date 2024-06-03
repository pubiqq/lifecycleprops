package com.pubiqq.lifecycleprops

/**
 * The entry point to configure lifecycle-aware properties.
 */
@LifecycleAwareConfigurationApi
public object LifecycleProps {

    private var _defaultLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>? = null
    private var _defaultLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>? = null

    internal val defaultLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>
        get() = _defaultLifecycleAwareReadOnlyConfiguration ?: LifecycleAwareReadOnlyConfiguration.Default()

    internal val defaultLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>
        get() = _defaultLifecycleAwareReadWriteConfiguration ?: LifecycleAwareReadWriteConfiguration.Default()

    /**
     * Sets the configurations to be used in `lifecycleAware` property delegates by default.
     *
     * @param readOnlyPropsConfiguration The configuration for read-only `lifecycleAware` property
     *   delegates.
     * @param readWritePropsConfiguration The configuration for read/write `lifecycleAware` property
     *   delegates.
     */
    public fun setLifecycleAwareConfigurations(
        readOnlyPropsConfiguration: LifecycleAwareReadOnlyConfiguration<Any>,
        readWritePropsConfiguration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        setReadOnlyLifecycleAwareConfiguration(readOnlyPropsConfiguration)
        setReadWriteLifecycleAwareConfiguration(readWritePropsConfiguration)
    }

    /**
     * Sets the configuration to be used in read-only `lifecycleAware` property delegates
     * by default.
     *
     * @param configuration The configuration to set.
     */
    public fun setReadOnlyLifecycleAwareConfiguration(
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
    public fun setReadWriteLifecycleAwareConfiguration(
        configuration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        _defaultLifecycleAwareReadWriteConfiguration = configuration
    }

    /**
     * Sets the default configurations for `lifecycleAware` property delegates.
     */
    public fun resetLifecycleAwareConfigurations() {
        resetReadOnlyLifecycleAwareConfiguration()
        resetReadWriteLifecycleAwareConfiguration()
    }

    /**
     * Sets the default configuration for read-only `lifecycleAware` property delegates.
     */
    public fun resetReadOnlyLifecycleAwareConfiguration() {
        _defaultLifecycleAwareReadOnlyConfiguration = null
    }

    /**
     * Sets the default configuration for read/write `lifecycleAware` property delegates.
     */
    public fun resetReadWriteLifecycleAwareConfiguration() {
        _defaultLifecycleAwareReadWriteConfiguration = null
    }
}