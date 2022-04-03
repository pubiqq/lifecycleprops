package com.pubiqq.lifecycleprops

@LifecycleAwareOptions
public class LifecycleAwareReadOnlyOptions(
    public val initializationStrategy: LifecycleAwareInitializationStrategy,
    public val deinitializationStrategy: LifecycleAwareDeinitializationStrategy
)