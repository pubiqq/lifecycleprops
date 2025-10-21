package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.lifecycleAware
import com.pubiqq.lifecycleprops.utils.Event

@Suppress("ClassName")
internal class TestLifecycleOwner_LifecycleAware_ReadOnly_String_InitializerAndOnResume_WithEventTracking : TestLifecycleOwner() {

    private val _events = mutableListOf<Event>()
    val events: List<Event>
        get() = _events

    @Suppress("unused")
    private val prop: String by lifecycleAware(
        initializer = {
            _events += Event.initialize
            "Test value"
        },
        onResume = { _events += Event.onResume },
    )
}
