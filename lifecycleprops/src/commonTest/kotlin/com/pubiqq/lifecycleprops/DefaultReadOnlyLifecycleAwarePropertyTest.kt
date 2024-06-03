package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import com.pubiqq.lifecycleprops.internal.LifecycleAwareReadOnlyProperty
import com.pubiqq.lifecycleprops.utils.Event
import com.pubiqq.lifecycleprops.utils.TestLifecycleOwner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class DefaultReadOnlyLifecycleAwarePropertyTest {

    @Test
    fun `Initialization is invoked lazily at the first direct access to the property`() {
        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware(
                initializer = { /* Unit initializer */ }
            ) as LifecycleAwareReadOnlyProperty

            handleLifecycleEvent(Lifecycle.Event.ON_START)
            assertNull(lifecycleAwareProp.rawValue)
            lifecycleAwareProp.initialize()
            assertNotNull(lifecycleAwareProp.rawValue)
        }
    }

    @Test
    fun `Initialization is invoked lazily at the first call of the lifecycle event handler`() {
        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware(
                initializer = { /* Unit initializer */ },
                onResume = { /* Some non-null event handler */ },
            ) as LifecycleAwareReadOnlyProperty

            handleLifecycleEvent(Lifecycle.Event.ON_START)
            assertNull(lifecycleAwareProp.rawValue)
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            assertNotNull(lifecycleAwareProp.rawValue)
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for a simple type`() {
        val events = mutableListOf<Event>()

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            lifecycleAware(
                initializer = { events += Event.onInitialize; },
                onCreate = { events += Event.onCreate },
                onStart = { events += Event.onStart },
                onResume = { events += Event.onResume },
                onPause = { events += Event.onPause },
                onStop = { events += Event.onStop },
                onDestroy = { events += Event.onDestroy },
                onAny = { event -> events += Event.onAny(event) }
            )

            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            handleLifecycleEvent(Lifecycle.Event.ON_START)
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }

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

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for AutoCloseable`() {
        val events = mutableListOf<Event>()

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            lifecycleAware(
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
            )

            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            handleLifecycleEvent(Lifecycle.Event.ON_START)
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }

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

    @Test
    fun `The property delegate clears a simple type property after the ON_DESTROY event`() {
        val events = mutableListOf<Event>()

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware(
                initializer = { events += Event.onInitialize }
            ) as LifecycleAwareReadOnlyProperty

            lifecycleAwareProp.initialize()
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            events += Event("Property raw value is not null: ${lifecycleAwareProp.rawValue != null}")
            events += Event("Right before onDestroy")
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            events += Event("Property raw value is not null: ${lifecycleAwareProp.rawValue != null}")
        }

        assertEquals(
            actual = events,
            expected = listOf(
                Event.onInitialize,
                Event("Property raw value is not null: ${true}"),
                Event("Right before onDestroy"),
                Event("Property raw value is not null: ${false}")
            )
        )
    }

    @Test
    fun `The property delegate correctly clears the AutoCloseable property after the ON_DESTROY event`() {
        val events = mutableListOf<Event>()

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware(
                initializer = {
                    events += Event.onInitialize
                    AutoCloseable { events += Event.onClose }
                }
            ) as LifecycleAwareReadOnlyProperty

            lifecycleAwareProp.initialize()
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            events += Event("Property raw value is not null: ${lifecycleAwareProp.rawValue != null}")
            events += Event("Right before onDestroy")
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            events += Event("Property raw value is not null: ${lifecycleAwareProp.rawValue != null}")
        }

        assertEquals(
            actual = events,
            expected = listOf(
                Event.onInitialize,
                Event("Property raw value is not null: ${true}"),
                Event("Right before onDestroy"),
                Event.onClose,
                Event("Property raw value is not null: ${false}")
            )
        )
    }

    @Test
    fun `Complex test 1`() {
        val events = mutableListOf<Event>()

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware(
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
            ) as LifecycleAwareReadOnlyProperty

            assertNull(lifecycleAwareProp.rawValue)
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            assertNotNull(lifecycleAwareProp.rawValue)
            handleLifecycleEvent(Lifecycle.Event.ON_START)
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            assertNotNull(lifecycleAwareProp.rawValue)
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            assertNull(lifecycleAwareProp.rawValue)
        }

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

    @Test
    fun `Complex test 2`() {
        val events = mutableListOf<Event>()

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware(
                initializer = {
                    events += Event.onInitialize
                    AutoCloseable { events += Event.onClose }
                },
                onStart = { events += Event.onStart },
                onStop = { events += Event.onStop }
            ) as LifecycleAwareReadOnlyProperty

            assertNull(lifecycleAwareProp.rawValue)
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            assertNull(lifecycleAwareProp.rawValue)
            handleLifecycleEvent(Lifecycle.Event.ON_START)
            assertNotNull(lifecycleAwareProp.rawValue)
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            assertNotNull(lifecycleAwareProp.rawValue)
            events += Event("Right before onDestroy")
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            assertNull(lifecycleAwareProp.rawValue)
        }

        assertEquals(
            actual = events,
            expected = listOf(
                Event.onInitialize,
                Event.onStart,
                Event.onStop,
                Event("Right before onDestroy"),
                Event.onClose
            )
        )
    }
}