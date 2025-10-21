package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.lifecycleAware
import com.pubiqq.lifecycleprops.utils.Event

@Suppress("ClassName")
internal class TestLifecycleOwner_LifecycleAware_ReadOnly_AutoCloseable_AllEvents_WithEventTracking : TestLifecycleOwner() {

    private val _events = mutableListOf<Event>()
    val events: List<Event>
        get() = _events

    @Suppress("unused")
    private val prop: AutoCloseable by lifecycleAware(
        initializer = {
            _events += Event.initialize
            AutoCloseable { _events += Event.onClose }
        },
        onCreate = { _events += Event.onCreate },
        onStart = { _events += Event.onStart },
        onResume = { _events += Event.onResume },
        onPause = { _events += Event.onPause },
        onStop = { _events += Event.onStop },
        onDestroy = { _events += Event.onDestroy },
        onAny = { event -> _events += Event.onAny(event) },
    )
}
