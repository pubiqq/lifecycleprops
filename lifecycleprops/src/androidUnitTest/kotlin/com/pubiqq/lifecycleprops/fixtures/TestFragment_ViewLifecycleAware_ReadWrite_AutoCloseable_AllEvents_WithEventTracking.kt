package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.utils.Event
import com.pubiqq.lifecycleprops.viewLifecycleAware

@Suppress("ClassName")
internal class TestFragment_ViewLifecycleAware_ReadWrite_AutoCloseable_AllEvents_WithEventTracking : TestFragment() {

    private val _events = mutableListOf<Event>()
    val events: List<Event>
        get() = _events

    @Suppress("unused")
    private var prop: AutoCloseable by viewLifecycleAware(
        onCreate = { _events += Event.onCreate },
        onStart = { _events += Event.onStart },
        onResume = { _events += Event.onResume },
        onPause = { _events += Event.onPause },
        onStop = { _events += Event.onStop },
        onDestroy = { _events += Event.onDestroy },
        onAny = { event -> _events += Event.onAny(event) },
    )

    fun initializeProp() {
        _events += Event.initialize
        prop = AutoCloseable { _events += Event.onClose }
    }
}
