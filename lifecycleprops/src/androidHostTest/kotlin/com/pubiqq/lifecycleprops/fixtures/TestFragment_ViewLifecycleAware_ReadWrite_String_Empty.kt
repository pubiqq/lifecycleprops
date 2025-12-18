package com.pubiqq.lifecycleprops.fixtures

import com.pubiqq.lifecycleprops.viewLifecycleAware

@Suppress("ClassName")
internal class TestFragment_ViewLifecycleAware_ReadWrite_String_Empty : TestFragment() {

    var prop: String by viewLifecycleAware()
}
