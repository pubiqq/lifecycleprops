package com.pubiqq.lifecycleprops

import androidx.lifecycle.Lifecycle
import com.pubiqq.lifecycleprops.internal.LifecycleAwareReadWriteProperty
import com.pubiqq.lifecycleprops.utils.Event
import com.pubiqq.lifecycleprops.utils.TestLifecycleOwner
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class DefaultReadWriteLifecycleAwarePropertyTest {

    @Test(expected = IllegalStateException::class)
    fun `The property delegate throws IllegalStateException if the property is not initialized when attempting to invoke the lifecycle event handler`() {
        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            lifecycleAware<String>(
                onStart = { /* Some non-null event handler */ },
            )

            handleLifecycleEvent(Lifecycle.Event.ON_START)
        }
    }

    @Test
    fun `The property delegate does not throw exceptions if the property is initialized before the first call of the lifecycle event handler`() {
        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware<String>(
                onResume = { /* Some non-null event handler */ },
            ) as LifecycleAwareReadWriteProperty

            handleLifecycleEvent(Lifecycle.Event.ON_START)
            lifecycleAwareProp.value = "Test value"
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    @Test
    fun `Initializer and lifecycle event handlers are invoked in the correct order for a simple type`() {
        val events = mutableListOf<Event>()

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.run {
            val lifecycleAwareProp = lifecycleAware<String>(
                onCreate = { events += Event.onCreate },
                onStart = { events += Event.onStart },
                onResume = { events += Event.onResume },
                onPause = { events += Event.onPause },
                onStop = { events += Event.onStop },
                onDestroy = { events += Event.onDestroy },
                onAny = { event -> events += Event.onAny(event) }
            ) as LifecycleAwareReadWriteProperty

            lifecycleAwareProp.value = "Test value"
            events += Event.onInitialize

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
            val lifecycleAwareProp = lifecycleAware<AutoCloseable>(
                onCreate = { events += Event.onCreate },
                onStart = { events += Event.onStart },
                onResume = { events += Event.onResume },
                onPause = { events += Event.onPause },
                onStop = { events += Event.onStop },
                onDestroy = { events += Event.onDestroy },
                onAny = { event -> events += Event.onAny(event) }
            ) as LifecycleAwareReadWriteProperty

            lifecycleAwareProp.value = AutoCloseable { events += Event.onClose }
            events += Event.onInitialize

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
            val lifecycleAwareProp = lifecycleAware<String>() as LifecycleAwareReadWriteProperty

            lifecycleAwareProp.value = "Test value"
            events += Event.onInitialize

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
            val lifecycleAwareProp =
                lifecycleAware<AutoCloseable>() as LifecycleAwareReadWriteProperty

            lifecycleAwareProp.value = AutoCloseable { events += Event.onClose }
            events += Event.onInitialize

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
}