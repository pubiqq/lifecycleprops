package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import com.pubiqq.lifecycleprops.fixtures.*
import com.pubiqq.lifecycleprops.utils.Event
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

/**
 * Tests for read-write `viewLifecycleAware` property delegates.
 */
@Suppress("ClassName")
@RunWith(RobolectricTestRunner::class)
internal class ViewLifecycleAware_ReadWritePropertyTest {

    @Test
    fun `viewLifecycleAware throws IllegalStateException if the property is not initialized when attempting to invoke the lifecycle event handler`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadWrite_String_OnStart> {
            assertFailsWith<IllegalStateException> {
                moveToState(Lifecycle.State.STARTED)
            }
        }
    }

    @Test
    fun `viewLifecycleAware does not throw exceptions if the property is initialized before the first call of the lifecycle event handler`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadWrite_String_OnResume> {
            onFragment { fragment ->
                fragment.prop = "Test value"
            }
            moveToState(Lifecycle.State.RESUMED)
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for a simple type`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadWrite_String_AllEvents_WithEventTracking> {
            onFragment { fragment ->
                fragment.initializeProp()
            }

            moveToState(Lifecycle.State.RESUMED)
            moveToState(Lifecycle.State.CREATED)

            onFragment { fragment ->
                assertEquals(
                    actual = fragment.events,
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
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for AutoCloseable`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadWrite_AutoCloseable_AllEvents_WithEventTracking> {
            onFragment { fragment ->
                fragment.initializeProp()
            }

            moveToState(Lifecycle.State.RESUMED)
            moveToState(Lifecycle.State.CREATED)

            onFragment { fragment ->
                assertEquals(
                    actual = fragment.events,
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
    }

    @Test
    fun `viewLifecycleAware does not allow the value to be reassigned by default`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadWrite_String_Empty> {
            onFragment { fragment ->
                fragment.prop = "Value 1"

                assertFailsWith<IllegalStateException> {
                    fragment.prop = "Value 2"
                }
            }
        }
    }

    @Test
    fun `viewLifecycleAware correctly clears a simple type property after the fragment's view is recreated`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadWrite_String_Empty_WithEventTracking> {
            moveToState(Lifecycle.State.RESUMED)
            onFragment { fragment ->
                fragment.initializeProp()
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize
                    )
                )
            }

            moveToState(Lifecycle.State.CREATED)
            onFragment { fragment ->
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize
                    )
                )

                assertFailsWith<IllegalStateException> {
                    fragment.accessProp()
                }
            }
        }
    }

    @Test
    fun `viewLifecycleAware correctly clears the AutoCloseable property after the fragment's view is recreated`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadWrite_AutoCloseable_Empty_WithEventTracking> {
            moveToState(Lifecycle.State.RESUMED)
            onFragment { fragment ->
                fragment.initializeProp()
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize
                    )
                )
            }

            moveToState(Lifecycle.State.CREATED)
            onFragment { fragment ->
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                        Event.onClose
                    )
                )

                assertFailsWith<IllegalStateException> {
                    fragment.accessProp()
                }
            }
        }
    }

    @Test
    fun `viewLifecycleAware correctly clears the property when onDestroy throws an exception`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadWrite_AutoCloseable_OnDestroyWithError_WithEventTracking> {
            moveToState(Lifecycle.State.RESUMED)
            onFragment { fragment ->
                fragment.initializeProp()
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                    ),
                )
            }

            assertFails {
                moveToState(Lifecycle.State.CREATED)
            }

            onFragment { fragment ->
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                        Event.onDestroy,
                        Event.onClose,
                    ),
                )

                assertFailsWith<IllegalStateException> {
                    fragment.accessProp()
                }
            }
        }
    }
}
