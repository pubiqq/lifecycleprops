package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import com.pubiqq.lifecycleprops.fixtures.TestLifecycleOwner_LifecycleAware_ReadWrite_AutoCloseable_AllEvents_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestLifecycleOwner_LifecycleAware_ReadWrite_AutoCloseable_Empty_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestLifecycleOwner_LifecycleAware_ReadWrite_AutoCloseable_OnDestroyWithError_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestLifecycleOwner_LifecycleAware_ReadWrite_String_AllEvents_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestLifecycleOwner_LifecycleAware_ReadWrite_String_Empty
import com.pubiqq.lifecycleprops.fixtures.TestLifecycleOwner_LifecycleAware_ReadWrite_String_Empty_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestLifecycleOwner_LifecycleAware_ReadWrite_String_OnResume
import com.pubiqq.lifecycleprops.fixtures.TestLifecycleOwner_LifecycleAware_ReadWrite_String_OnStart
import com.pubiqq.lifecycleprops.utils.Event
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

/**
 * Tests for read-write `lifecycleAware` property delegates.
 */
@Suppress("ClassName")
internal class LifecycleAware_ReadWritePropertyTest {

    @Test
    fun `lifecycleAware throws IllegalStateException if the property is not initialized when attempting to invoke the lifecycle event handler`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadWrite_String_OnStart()
        lifecycleOwner.run {
            assertFailsWith<IllegalStateException> {
                handleLifecycleEvent(Lifecycle.Event.ON_START)
            }
        }
    }

    @Test
    fun `lifecycleAware does not throw exceptions if the property is initialized before the first call of the lifecycle event handler`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadWrite_String_OnResume()
        lifecycleOwner.run {
            handleLifecycleEvent(Lifecycle.Event.ON_START)
            prop = "Test value"
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for a simple type`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadWrite_String_AllEvents_WithEventTracking()
        lifecycleOwner.run {
            initializeProp()

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
                ),
            )
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for AutoCloseable`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadWrite_AutoCloseable_AllEvents_WithEventTracking()
        lifecycleOwner.run {
            initializeProp()

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
                    Event.onClose,
                ),
            )
        }
    }

    @Test
    fun `lifecycleAware does not allow the value to be reassigned by default`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadWrite_String_Empty()
        lifecycleOwner.run {
            prop = "Value 1"

            assertFailsWith<IllegalStateException> {
                prop = "Value 2"
            }
        }
    }

    @Test
    fun `lifecycleAware correctly clears a simple type property after the ON_DESTROY event`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadWrite_String_Empty_WithEventTracking()
        lifecycleOwner.run {
            initializeProp()

            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                ),
            )

            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                ),
            )

            assertFailsWith<IllegalStateException> {
                accessProp()
            }
        }
    }

    @Test
    fun `lifecycleAware correctly clears the AutoCloseable property after the ON_DESTROY event`() {
        val lifecycleOwner = TestLifecycleOwner_LifecycleAware_ReadWrite_AutoCloseable_Empty_WithEventTracking()
        lifecycleOwner.run {
            initializeProp()

            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                ),
            )

            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                    Event.onClose,
                ),
            )

            assertFailsWith<IllegalStateException> {
                accessProp()
            }
        }
    }

    @Test
    fun `lifecycleAware correctly clears the property when onDestroy throws an exception`() {
        val lifecycleOwner =
            TestLifecycleOwner_LifecycleAware_ReadWrite_AutoCloseable_OnDestroyWithError_WithEventTracking()
        lifecycleOwner.run {
            initializeProp()

            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                ),
            )

            assertFails {
                handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            }

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.initialize,
                    Event.onDestroy,
                    Event.onClose,
                ),
            )

            assertFailsWith<IllegalStateException> {
                accessProp()
            }
        }
    }
}
