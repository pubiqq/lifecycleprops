package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import com.pubiqq.lifecycleprops.fixtures.*
import com.pubiqq.lifecycleprops.utils.Event
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Tests for read-only `lifecycleAware` property delegates.
 */
@Suppress("ClassName")
internal class LifecycleAware_ReadOnlyPropertyTest {

    @Test
    fun `Initializer is invoked lazily at the first direct access to the property`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadOnly_String_Initializer_WithEventTracking()
        lifecycleOwner.run {
            assertEquals(
                actual = events,
                expected = listOf()
            )

            accessProp()

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize
                )
            )
        }
    }

    @Test
    fun `Initializer is reinvoked when accessing the property if it threw an exception the previous time`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadOnly_String_InitializerWithError_WithEventTracking()
        lifecycleOwner.run {
            assertFailsWith<RuntimeException> {
                accessProp()
            }

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize
                )
            )

            assertFailsWith<RuntimeException> {
                accessProp()
            }

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                    Event.initialize
                )
            )
        }
    }

    @Test
    fun `Initializer is invoked lazily at the first call of the lifecycle event handler`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadOnly_String_InitializerAndOnResume_WithEventTracking()
        lifecycleOwner.run {
            handleLifecycleEvent(Lifecycle.Event.ON_START)
            assertEquals(
                actual = events,
                expected = listOf()
            )

            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                    Event.onResume
                )
            )
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for a simple type`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadOnly_String_AllEvents_WithEventTracking()
        lifecycleOwner.run {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                    Event.onCreate,
                    Event.onAny(Lifecycle.Event.ON_CREATE),
                    Event.onStart,
                    Event.onAny(Lifecycle.Event.ON_START),
                    Event.onResume,
                    Event.onAny(Lifecycle.Event.ON_RESUME),
                    Event.onPause,
                    Event.onAny(Lifecycle.Event.ON_PAUSE),
                    Event.onStop,
                    Event.onAny(Lifecycle.Event.ON_STOP),
                    Event.onDestroy,
                    Event.onAny(Lifecycle.Event.ON_DESTROY)
                )
            )
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for AutoCloseable`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadOnly_AutoCloseable_AllEvents_WithEventTracking()
        lifecycleOwner.run {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                    Event.onCreate,
                    Event.onAny(Lifecycle.Event.ON_CREATE),
                    Event.onStart,
                    Event.onAny(Lifecycle.Event.ON_START),
                    Event.onResume,
                    Event.onAny(Lifecycle.Event.ON_RESUME),
                    Event.onPause,
                    Event.onAny(Lifecycle.Event.ON_PAUSE),
                    Event.onStop,
                    Event.onAny(Lifecycle.Event.ON_STOP),
                    Event.onDestroy,
                    Event.onAny(Lifecycle.Event.ON_DESTROY),
                    Event.onClose
                )
            )
        }
    }

    @Test
    fun `lifecycleAware correctly clears a simple type property after the ON_DESTROY event`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadOnly_String_Initializer_WithEventTracking()
        lifecycleOwner.run {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            accessProp()
            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize
                )
            )

            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            accessProp()
            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                    Event.initialize
                )
            )
        }
    }

    @Test
    fun `lifecycleAware correctly clears the AutoCloseable property after the ON_DESTROY event`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadOnly_AutoCloseable_Initializer_WithEventTracking()
        lifecycleOwner.run {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            accessProp()
            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize
                )
            )

            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                    Event.onClose
                )
            )
        }
    }
}
