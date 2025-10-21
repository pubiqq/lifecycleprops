package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.utils.Event
import com.pubiqq.lifecycleprops.viewLifecycleAware

@Suppress("ClassName")
internal class TestFragment_ViewLifecycleAware_ReadWrite_AutoCloseable_OnDestroyWithError_WithEventTracking : TestFragment() {

    private val _events = mutableListOf<Event>()
    val events: List<Event>
        get() = _events

    @Suppress("unused")
    private var prop: AutoCloseable by viewLifecycleAware(
        onDestroy = {
            _events += Event.onDestroy
            throw RuntimeException()
        }
    )

    fun initializeProp() {
        _events += Event.initialize
        prop = AutoCloseable { _events += Event.onClose }
    }

    fun accessProp() {
        ::prop.get()
    }
}
