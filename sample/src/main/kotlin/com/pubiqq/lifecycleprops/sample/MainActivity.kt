package com.pubiqq.lifecycleprops.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.pubiqq.lifecycleprops.ExperimentalConfigurationApi
import com.pubiqq.lifecycleprops.LifecycleAwareReadOnlyConfiguration
import com.pubiqq.lifecycleprops.lifecycleAware
import com.pubiqq.lifecycleprops.sample.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private var binding: MainActivityBinding by lifecycleAware()

    private val prop1: String by lifecycleAware(
        initializer = { Log.d(TAG, "[lifecycleAware] initialize"); "Prop 1" },
        onCreate = { Log.d(TAG, "[lifecycleAware] onCreate") },
        onStart = { Log.d(TAG, "[lifecycleAware] onStart") },
        onResume = { Log.d(TAG, "[lifecycleAware] onResume") },
        onPause = { Log.d(TAG, "[lifecycleAware] onPause") },
        onStop = { Log.d(TAG, "[lifecycleAware] onStop") },
        onDestroy = { Log.d(TAG, "[lifecycleAware] onDestroy") },
        onAny = { event -> Log.d(TAG, "[lifecycleAware] onAny($event)") }
    )

    @OptIn(ExperimentalConfigurationApi::class)
    private val prop2: String by lifecycleAware(
        configuration = LifecycleAwareReadOnlyConfiguration.Legacy(),
        initializer = { Log.d(TAG, "[Custom lifecycleAware] initialize"); "Prop 2" },
        onCreate = { Log.d(TAG, "[Custom lifecycleAware] onCreate") },
        onStart = { Log.d(TAG, "[Custom lifecycleAware] onStart") },
        onResume = { Log.d(TAG, "[Custom lifecycleAware] onResume") },
        onPause = { Log.d(TAG, "[Custom lifecycleAware] onPause") },
        onStop = { Log.d(TAG, "[Custom lifecycleAware] onStop") },
        onDestroy = { Log.d(TAG, "[Custom lifecycleAware] onDestroy") },
        onAny = { event -> Log.d(TAG, "[Custom lifecycleAware] onAny($event)") }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        Log.d(TAG, "[Activity] onCreate")

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "[Activity] onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "[Activity] onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "[Activity] onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "[Activity] onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "[Activity] onDestroy")
    }
}