package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.lifecycleAware

@Suppress("ClassName")
internal class TestLifecycleOwner_LifecycleAware_ReadWrite_String_OnResume : TestLifecycleOwner() {

    var prop: String by lifecycleAware(
        onResume = { /* Non-null event handler */ },
    )
}
