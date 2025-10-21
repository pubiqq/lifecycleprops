package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.utils.Event
import com.pubiqq.lifecycleprops.viewLifecycleAware

@Suppress("ClassName")
internal class TestFragment_ViewLifecycleAware_ReadOnly_AutoCloseable_Initializer_WithEventTracking : TestFragment() {

    private val _events = mutableListOf<Event>()
    val events: List<Event>
        get() = _events

    @Suppress("unused")
    private val prop: AutoCloseable by viewLifecycleAware(
        initializer = {
            _events += Event.initialize
            AutoCloseable { _events += Event.onClose }
        },
    )

    fun accessProp() {
        ::prop.get()
    }
}
