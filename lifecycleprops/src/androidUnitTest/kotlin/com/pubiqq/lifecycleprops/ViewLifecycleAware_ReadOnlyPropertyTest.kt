package com.pubiqq.lifecycleprops

import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import com.pubiqq.lifecycleprops.internal.ViewLifecycleAwareReadOnlyProperty
import com.pubiqq.lifecycleprops.utils.Event
import com.pubiqq.lifecycleprops.utils.TestFragment
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Tests for read-only `viewLifecycleAware` property delegates.
 */
@RunWith(RobolectricTestRunner::class)
internal class ViewLifecycleAware_ReadOnlyPropertyTest {

    @Test
    fun `Initializer is invoked lazily at the first direct access to the property`() {
        launchFragment<TestFragment>(initialState = Lifecycle.State.INITIALIZED).use { scenario ->
            val viewLifecycleAwareProp = scenario.withFragment {
                viewLifecycleAware(
                    initializer = { "Test value" }
                ) as ViewLifecycleAwareReadOnlyProperty
            }

            scenario.moveToState(Lifecycle.State.STARTED)
            assertNull(viewLifecycleAwareProp.rawValue)
            viewLifecycleAwareProp.initialize()
            assertNotNull(viewLifecycleAwareProp.rawValue)
        }
    }

    @Test
    fun `Initializer is invoked lazily at the first call of the lifecycle event handler`() {
        launchFragment<TestFragment>(initialState = Lifecycle.State.INITIALIZED).use { scenario ->
            val viewLifecycleAwareProp = scenario.withFragment {
                viewLifecycleAware(
                    initializer = { "Test value" },
                    onResume = { /* Non-null event handler */ }
                ) as ViewLifecycleAwareReadOnlyProperty
            }

            scenario.moveToState(Lifecycle.State.STARTED)
            assertNull(viewLifecycleAwareProp.rawValue)
            scenario.moveToState(Lifecycle.State.RESUMED)
            assertNotNull(viewLifecycleAwareProp.rawValue)
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for a simple type`() {
        launchFragment<TestFragment>().use { scenario ->
            val events = mutableListOf<Event>()

            scenario.withFragment {
                viewLifecycleAware(
                    initializer = {
                        events += Event.onInitialize
                        "Test value"
                    },
                    onCreate = { events += Event.onCreate },
                    onStart = { events += Event.onStart },
                    onResume = { events += Event.onResume },
                    onPause = { events += Event.onPause },
                    onStop = { events += Event.onStop },
                    onDestroy = { events += Event.onDestroy },
                    onAny = { event -> events += Event.onAny(event) }
                ) as ViewLifecycleAwareReadOnlyProperty
            }

            scenario.moveToState(Lifecycle.State.DESTROYED)

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.onInitialize,
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
        launchFragment<TestFragment>().use { scenario ->
            val events = mutableListOf<Event>()

            scenario.withFragment {
                viewLifecycleAware(
                    initializer = {
                        events += Event.onInitialize
                        AutoCloseable { events += Event.onClose }
                    },
                    onCreate = { events += Event.onCreate },
                    onStart = { events += Event.onStart },
                    onResume = { events += Event.onResume },
                    onPause = { events += Event.onPause },
                    onStop = { events += Event.onStop },
                    onDestroy = { events += Event.onDestroy },
                    onAny = { event -> events += Event.onAny(event) }
                ) as ViewLifecycleAwareReadOnlyProperty
            }

            scenario.moveToState(Lifecycle.State.DESTROYED)

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.onInitialize,
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
    fun `viewLifecycleAware correctly clears a simple type property after the fragment's view is recreated`() {
        launchFragment<TestFragment>().use { scenario ->
            val events = mutableListOf<Event>()

            val viewLifecycleAwareProp = scenario.withFragment {
                viewLifecycleAware(
                    initializer = {
                        events += Event.onInitialize
                        "Test value"
                    }
                ) as ViewLifecycleAwareReadOnlyProperty
            }

            viewLifecycleAwareProp.initialize()
            events += Event("Property raw value is not null: ${viewLifecycleAwareProp.rawValue != null}")
            events += Event("Right before recreating the view")
            scenario.moveToState(Lifecycle.State.CREATED)
            events += Event("Property raw value is not null: ${viewLifecycleAwareProp.rawValue != null}")

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.onInitialize,
                    Event("Property raw value is not null: ${true}"),
                    Event("Right before recreating the view"),
                    Event("Property raw value is not null: ${false}")
                )
            )
        }
    }

    @Test
    fun `viewLifecycleAware correctly clears the AutoCloseable property after the fragment's view is recreated`() {
        launchFragment<TestFragment>().use { scenario ->
            val events = mutableListOf<Event>()

            val viewLifecycleAwareProp = scenario.withFragment {
                viewLifecycleAware(
                    initializer = {
                        events += Event.onInitialize
                        AutoCloseable { events += Event.onClose }
                    }
                ) as ViewLifecycleAwareReadOnlyProperty
            }

            viewLifecycleAwareProp.initialize()
            events += Event("Property raw value is not null: ${viewLifecycleAwareProp.rawValue != null}")
            events += Event("Right before recreating the view")
            scenario.moveToState(Lifecycle.State.CREATED)
            events += Event("Property raw value is not null: ${viewLifecycleAwareProp.rawValue != null}")

            assertEquals(
                actual = events,
                expected = listOf(
                    Event.onInitialize,
                    Event("Property raw value is not null: ${true}"),
                    Event("Right before recreating the view"),
                    Event.onClose,
                    Event("Property raw value is not null: ${false}")
                )
            )
        }
    }
}
