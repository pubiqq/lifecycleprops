package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.lifecycleAware

@Suppress("ClassName")
internal class TestLifecycleOwner_LifecycleAware_ReadWrite_String_OnStart : TestLifecycleOwner() {

    var prop: String by lifecycleAware(
        onStart = { /* Non-null event handler */ }
    )
}
