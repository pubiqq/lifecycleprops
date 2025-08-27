package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.lifecycleAware
import com.pubiqq.lifecycleprops.utils.Event

@Suppress("ClassName")
internal class TestLifecycleOwner_LifecycleAware_ReadWrite_String_Empty_WithEventTracking : TestLifecycleOwner() {

    private val _events = mutableListOf<Event>()
    val events: List<Event>
        get() = _events

    @Suppress("unused")
    private var prop: String by lifecycleAware()

    fun initializeProp() {
        _events += Event.initialize
        prop = "Test value"
    }

    fun accessProp() {
        ::prop.get()
    }
}
