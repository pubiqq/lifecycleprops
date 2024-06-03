package com.pubiqq.lifecycleprops

/**
 * The entry point to configure lifecycle-aware properties.
 */
@LifecycleAwareConfigurationApi
public object LifecycleProps {

    internal var defaultLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any> =
        LifecycleAwareReadOnlyConfiguration.Default()
        private set

    internal var defaultLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any> =
        LifecycleAwareReadWriteConfiguration.Default()
        private set

    internal var defaultViewLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any> =
        LifecycleAwareReadOnlyConfiguration.Default()
        private set

    internal var defaultViewLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any> =
        LifecycleAwareReadWriteConfiguration.Default()
        private set

    /**
     * Set default configurations for lifecycle-aware property delegates.
     *
     * @param readOnlyConfiguration Default configuration for lifecycle-aware delegates of read-only
     *   properties.
     * @param readWriteConfiguration Default configuration for lifecycle-aware delegates of
     *   read/write properties.
     */
    public fun setDefaultConfigurations(
        readOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>,
        readWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        setDefaultConfigurations(
            lifecycleAwareReadOnlyConfiguration = readOnlyConfiguration,
            lifecycleAwareReadWriteConfiguration = readWriteConfiguration,
            viewLifecycleAwareReadOnlyConfiguration = readOnlyConfiguration,
            viewLifecycleAwareReadWriteConfiguration = readWriteConfiguration
        )
    }

    /**
     * Set default configurations for lifecycle-aware property delegates.
     *
     * @param lifecycleAwareReadOnlyConfiguration Default configuration for [lifecycleAware]
     *   delegate of read-only properties.
     * @param lifecycleAwareReadWriteConfiguration Default configuration for [lifecycleAware]
     *   delegate of read/write properties.
     * @param viewLifecycleAwareReadOnlyConfiguration Default configuration for [viewLifecycleAware]
     *   delegate of read-only properties.
     * @param viewLifecycleAwareReadWriteConfiguration Default configuration for
     *   [viewLifecycleAware] delegate of read/write properties.
     */
    public fun setDefaultConfigurations(
        lifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>,
        lifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>,
        viewLifecycleAwareReadOnlyConfiguration: LifecycleAwareReadOnlyConfiguration<Any>,
        viewLifecycleAwareReadWriteConfiguration: LifecycleAwareReadWriteConfiguration<Any>
    ) {
        defaultLifecycleAwareReadOnlyConfiguration = lifecycleAwareReadOnlyConfiguration
        defaultLifecycleAwareReadWriteConfiguration = lifecycleAwareReadWriteConfiguration
        defaultViewLifecycleAwareReadOnlyConfiguration = viewLifecycleAwareReadOnlyConfiguration
        defaultViewLifecycleAwareReadWriteConfiguration = viewLifecycleAwareReadWriteConfiguration
    }
}