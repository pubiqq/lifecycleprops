package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.lifecycleAware
import com.pubiqq.lifecycleprops.utils.Event

@Suppress("ClassName")
internal class TestLifecycleOwner_LifecycleAware_ReadWrite_AutoCloseable_OnDestroyWithError_WithEventTracking : TestLifecycleOwner() {

    private val _events = mutableListOf<Event>()
    val events: List<Event>
        get() = _events

    @Suppress("unused")
    private var prop: AutoCloseable by lifecycleAware(
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
