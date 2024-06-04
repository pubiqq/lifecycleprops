package com.pubiqq.lifecycleprops.internal

import com.pubiqq.lifecycleprops.LifecycleAwareReadOnlyConfiguration
import com.pubiqq.lifecycleprops.LifecycleAwareReadWriteConfiguration

internal object DefaultAnyLifecycleAwareReadOnlyConfiguration :
    LifecycleAwareReadOnlyConfiguration<Any> by LifecycleAwareReadOnlyConfiguration.Default()

internal object DefaultAnyLifecycleAwareReadWriteConfiguration :
    LifecycleAwareReadWriteConfiguration<Any> by LifecycleAwareReadWriteConfiguration.Default()