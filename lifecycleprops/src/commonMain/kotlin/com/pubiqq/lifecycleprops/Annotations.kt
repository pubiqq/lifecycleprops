package com.pubiqq.lifecycleprops

/**
 * Marks an experimental API related to configuring lifecycle-aware delegates.
 *
 * The API marked with this annotation does not provide any compatibility guarantees and may be changed or removed
 * in the near future.
 */
@MustBeDocumented
@RequiresOptIn(
    message = "This API is experimental and may be changed or removed in the future.",
    level = RequiresOptIn.Level.WARNING
)
@Retention(AnnotationRetention.BINARY)
public annotation class ExperimentalConfigurationApi