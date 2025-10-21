package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import com.pubiqq.lifecycleprops.fixtures.TestFragment_ViewLifecycleAware_ReadOnly_AutoCloseable_All_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestFragment_ViewLifecycleAware_ReadOnly_AutoCloseable_InitializerAndOnDestroyWithError_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestFragment_ViewLifecycleAware_ReadOnly_AutoCloseable_Initializer_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestFragment_ViewLifecycleAware_ReadOnly_String_AllEvents_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestFragment_ViewLifecycleAware_ReadOnly_String_InitializerAndOnResume_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestFragment_ViewLifecycleAware_ReadOnly_String_InitializerWithError_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.TestFragment_ViewLifecycleAware_ReadOnly_String_Initializer_WithEventTracking
import com.pubiqq.lifecycleprops.fixtures.launchFixture
import com.pubiqq.lifecycleprops.utils.Event
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

/**
 * Tests for read-only `viewLifecycleAware` property delegates.
 */
@Suppress("ClassName")
@RunWith(RobolectricTestRunner::class)
internal class ViewLifecycleAware_ReadOnlyPropertyTest {

    @Test
    fun `Initializer is invoked lazily at the first direct access to the property`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadOnly_String_Initializer_WithEventTracking> {
            onFragment { fragment ->
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(),
                )

                fragment.accessProp()

                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                    ),
                )
            }
        }
    }

    @Test
    fun `Initializer is reinvoked when accessing the property if it threw an exception the previous time`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadOnly_String_InitializerWithError_WithEventTracking> {
            onFragment { fragment ->
                assertFailsWith<RuntimeException> {
                    fragment.accessProp()
                }

                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                    ),
                )

                assertFailsWith<RuntimeException> {
                    fragment.accessProp()
                }

                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                        Event.initialize,
                    ),
                )
            }
        }
    }

    @Test
    fun `Initializer is invoked lazily at the first call of the lifecycle event handler`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadOnly_String_InitializerAndOnResume_WithEventTracking> {
            moveToState(Lifecycle.State.STARTED)
            onFragment { fragment ->
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(),
                )
            }

            moveToState(Lifecycle.State.RESUMED)
            onFragment { fragment ->
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                        Event.onResume,
                    ),
                )
            }
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for a simple type`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadOnly_String_AllEvents_WithEventTracking> {
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
                    ),
                )
            }
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for AutoCloseable`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadOnly_AutoCloseable_All_WithEventTracking> {
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
                        Event.onClose,
                    ),
                )
            }
        }
    }

    @Test
    fun `viewLifecycleAware reinitializes a simple type property after the fragment's view is recreated`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadOnly_String_Initializer_WithEventTracking> {
            moveToState(Lifecycle.State.RESUMED)
            onFragment { fragment ->
                fragment.accessProp()
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                    ),
                )
            }

            moveToState(Lifecycle.State.CREATED)
            onFragment { fragment ->
                fragment.accessProp()
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                        Event.initialize,
                    ),
                )
            }
        }
    }

    @Test
    fun `viewLifecycleAware correctly clears and reinitializes the AutoCloseable property after the fragment's view is recreated`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadOnly_AutoCloseable_Initializer_WithEventTracking> {
            moveToState(Lifecycle.State.RESUMED)
            onFragment { fragment ->
                fragment.accessProp()
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                    ),
                )
            }

            moveToState(Lifecycle.State.CREATED)
            onFragment { fragment ->
                fragment.accessProp()
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                        Event.onClose,
                        Event.initialize,
                    ),
                )
            }
        }
    }

    @Test
    fun `viewLifecycleAware correctly clears and reinitializes the property when onDestroy throws an exception`() {
        launchFixture<TestFragment_ViewLifecycleAware_ReadOnly_AutoCloseable_InitializerAndOnDestroyWithError_WithEventTracking> {
            moveToState(Lifecycle.State.RESUMED)
            onFragment { fragment ->
                fragment.accessProp()
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
                fragment.accessProp()
                assertEquals(
                    actual = fragment.events,
                    expected = listOf(
                        Event.initialize,
                        Event.onDestroy,
                        Event.onClose,
                        Event.initialize,
                    ),
                )
            }
        }
    }
}
