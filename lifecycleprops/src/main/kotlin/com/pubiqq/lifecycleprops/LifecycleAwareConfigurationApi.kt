package com.pubiqq.lifecycleprops

/**
 * Marks the API that provides configuration for lifecycle-aware delegates.
 *
 * Note that the annotated API is not stable and may be changed in the future.
 */
@RequiresOptIn(
    message = "This API is not stable and may be changed in the future.",
    level = RequiresOptIn.Level.WARNING
)
public annotation class LifecycleAwareConfigurationApi