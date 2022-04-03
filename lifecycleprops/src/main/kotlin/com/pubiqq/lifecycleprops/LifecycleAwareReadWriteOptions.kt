package com.pubiqq.lifecycleprops

@LifecycleAwareOptions
public class LifecycleAwareReadWriteOptions(
    public val deinitializationStrategy: LifecycleAwareDeinitializationStrategy
)