package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.viewLifecycleAware

@Suppress("ClassName")
internal class TestFragment_ViewLifecycleAware_ReadWrite_String_OnResume : TestFragment() {

    var prop: String by viewLifecycleAware(
        onResume = { /* Non-null event handler */ },
    )
}
