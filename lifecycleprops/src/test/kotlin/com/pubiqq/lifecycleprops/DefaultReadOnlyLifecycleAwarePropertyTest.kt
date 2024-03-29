package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import com.pubiqq.lifecycleprops.internal.LifecycleAwareReadOnlyProperty
import com.pubiqq.lifecycleprops.internal.utils.PlatformUtils
import com.pubiqq.lifecycleprops.utils.Event
import com.pubiqq.lifecycleprops.utils.TestLifecycleOwner
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class DefaultReadOnlyLifecycleAwarePropertyTest {

    @Before
    fun before() {
        mockkObject(PlatformUtils)
        every { PlatformUtils.isAutoCloseableAvailable() } returns true
    }

    @After
    fun after() {
        unmockkObject(PlatformUtils)
    }

    @Test
    fun `Initialization is invoked lazily at the first direct access to the property`() {
        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware(
                initializer = { /* Unit initializer */ }
            ) as LifecycleAwareReadOnlyProperty

            handleLifecycleEvent(Lifecycle.Event.ON_START)
            lifecycleAwareProp.rawValue shouldBe null
            lifecycleAwareProp.initialize()
            lifecycleAwareProp.rawValue shouldNotBe null
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
            lifecycleAwareProp.rawValue shouldBe null
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            lifecycleAwareProp.rawValue shouldNotBe null
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

        events shouldBe listOf(
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

        events shouldBe listOf(
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

        events shouldBe listOf(
            Event.onInitialize,
            Event("Property raw value is not null: ${true}"),
            Event("Right before onDestroy"),
            Event("Property raw value is not null: ${false}")
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

        events shouldBe listOf(
            Event.onInitialize,
            Event("Property raw value is not null: ${true}"),
            Event("Right before onDestroy"),
            Event.onClose,
            Event("Property raw value is not null: ${false}")
        )
    }

    @Test
    fun `Complex test #1`() {
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

            lifecycleAwareProp.rawValue shouldBe null
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            lifecycleAwareProp.rawValue shouldNotBe null
            handleLifecycleEvent(Lifecycle.Event.ON_START)
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            lifecycleAwareProp.rawValue shouldNotBe null
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            lifecycleAwareProp.rawValue shouldBe null
        }

        events shouldBe listOf(
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
    }

    @Test
    fun `Complex test #2`() {
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

            lifecycleAwareProp.rawValue shouldBe null
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            lifecycleAwareProp.rawValue shouldBe null
            handleLifecycleEvent(Lifecycle.Event.ON_START)
            lifecycleAwareProp.rawValue shouldNotBe null
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            lifecycleAwareProp.rawValue shouldNotBe null
            events += Event("Right before onDestroy")
            handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            lifecycleAwareProp.rawValue shouldBe null
        }

        events shouldBe listOf(
            Event.onInitialize,
            Event.onStart,
            Event.onStop,
            Event("Right before onDestroy"),
            Event.onClose
        )
    }
}