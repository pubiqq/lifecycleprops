package com.pubiqq.lifecycleprops.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pubiqq.lifecycleprops.*
import com.pubiqq.lifecycleprops.sample.databinding.SampleFragmentBinding

class SampleFragment : Fragment() {

    companion object {
        private const val TAG = "SampleFragment"
    }

    private var binding: SampleFragmentBinding by viewLifecycleAware()

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

    private val prop2: String by viewLifecycleAware(
        initializer = { Log.d(TAG, "[viewLifecycleAware] initialize"); "Prop 2" },
        onCreate = { Log.d(TAG, "[viewLifecycleAware] onCreate") },
        onStart = { Log.d(TAG, "[viewLifecycleAware] onStart") },
        onResume = { Log.d(TAG, "[viewLifecycleAware] onResume") },
        onPause = { Log.d(TAG, "[viewLifecycleAware] onPause") },
        onStop = { Log.d(TAG, "[viewLifecycleAware] onStop") },
        onDestroy = { Log.d(TAG, "[viewLifecycleAware] onDestroy") },
        onAny = { event -> Log.d(TAG, "[viewLifecycleAware] onAny($event)") }
    )

    @OptIn(LifecycleAwareOptions::class)
    private val prop3: String by lifecycleAware(
        options = LifecycleAwareReadOnlyOptions(
            initializationStrategy = LifecycleAwareInitializationStrategy.Eager,
            deinitializationStrategy = LifecycleAwareDeinitializationStrategy.None
        ),
        initializer = { Log.d(TAG, "[Custom lifecycleAware] initialize"); "Prop 3" },
        onCreate = { Log.d(TAG, "[Custom lifecycleAware] onCreate") },
        onStart = { Log.d(TAG, "[Custom lifecycleAware] onStart") },
        onResume = { Log.d(TAG, "[Custom lifecycleAware] onResume") },
        onPause = { Log.d(TAG, "[Custom lifecycleAware] onPause") },
        onStop = { Log.d(TAG, "[Custom lifecycleAware] onStop") },
        onDestroy = { Log.d(TAG, "[Custom lifecycleAware] onDestroy") },
        onAny = { event -> Log.d(TAG, "[Custom lifecycleAware] onAny($event)") }
    )

    @OptIn(LifecycleAwareOptions::class)
    private val prop4: String by viewLifecycleAware(
        options = LifecycleAwareReadOnlyOptions(
            initializationStrategy = LifecycleAwareInitializationStrategy.Eager,
            deinitializationStrategy = LifecycleAwareDeinitializationStrategy.None
        ),
        initializer = { Log.d(TAG, "[Custom viewLifecycleAware] initialize"); "Prop 4" },
        onCreate = { Log.d(TAG, "[Custom viewLifecycleAware] onCreate") },
        onStart = { Log.d(TAG, "[Custom viewLifecycleAware] onStart") },
        onResume = { Log.d(TAG, "[Custom viewLifecycleAware] onResume") },
        onPause = { Log.d(TAG, "[Custom viewLifecycleAware] onPause") },
        onStop = { Log.d(TAG, "[Custom viewLifecycleAware] onStop") },
        onDestroy = { Log.d(TAG, "[Custom viewLifecycleAware] onDestroy") },
        onAny = { event -> Log.d(TAG, "[Custom viewLifecycleAware] onAny($event)") }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "[Fragment] onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SampleFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        Log.d(TAG, "[Fragment] onCreateView")

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "[Fragment] onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "[Fragment] onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "[Fragment] onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "[Fragment] onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "[Fragment] onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "[Fragment] onDestroy")
    }
}