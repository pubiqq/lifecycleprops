package com.pubiqq.lifecycleprops.fixtures

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle

internal inline fun <reified F : TestFragment> launchFixture(
    initialState: Lifecycle.State = Lifecycle.State.INITIALIZED,
    crossinline action: FragmentScenario<F>.() -> Unit,
) {
    launchFragment<F>(initialState = initialState).use { scenario ->
        scenario.action()
    }
}
