# ViewBinding delegates

In order to reduce boilerplate and simplify work with
[view binding](https://developer.android.com/topic/libraries/view-binding), it's often suggested to
use special delegates that provide a `ViewBinding` object and automatically clear the associated
property when the view is destroyed.

[There](https://github.com/Zhuinden/fragmentviewbindingdelegate-kt)
[are](https://github.com/yogacp/android-viewbinding)
[many](https://github.com/androidbroadcast/ViewBindingPropertyDelegate)
[libraries](https://github.com/hoc081098/ViewBindingDelegate)
[and](https://medium.com/default-to-open/handling-lifecycle-with-view-binding-in-fragments-a7f237c56832)
[articles](https://proandroiddev.com/viewbinding-with-kotlin-property-delegate-c907682e24c9)
that describe how to create them and all these solutions use a similar principle, which you
can easily implement using the `lifecycleAware` delegate (for activities) or the
`viewLifecycleAware` delegate (for fragments).

So, for example, you can implement `viewBinding` using reflection and the `ViewBinding.bind` method:

```kotlin
// Creates `ViewBinding` instance via `ViewBinding.bind` method (reflective)
inline fun <reified T : ViewBinding> Fragment.viewBinding() = viewLifecycleAware(
    initializer = {
        val bindMethod = T::class.java.getMethod("bind", View::class.java)
        bindMethod.invoke(null, requireView()) as T
    }
)

// Creates `ViewBinding` instance via `ViewBinding.bind` method (reflective)
inline fun <reified T : ViewBinding> ComponentActivity.viewBinding() = lifecycleAware(
    initializer = {
        val bindMethod = T::class.java.getMethod("bind", View::class.java)
        val view = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        bindMethod.invoke(null, view) as T
    }
)

// ...

class SampleFragment : Fragment(R.layout.sample_fragment) {

    private val binding: SampleFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vMessage.text = "Test message"
    }
}
```

And this is how the implementation of `viewBinding` for an activity can look like using
the `ViewBinding.inflate` method and without using reflection:

```kotlin
// Creates `ViewBinding` instance via `ViewBinding.inflate` method (non-reflective)
fun <T : ViewBinding> ComponentActivity.viewBinding(inflateMethod: (LayoutInflater) -> T) =
    lifecycleAware(
        initializer = { inflateMethod(layoutInflater) }
    )

// ...

class SampleActivity : AppCompatActivity() {

    private val binding by viewBinding(SampleActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
```

Choose what you think suits you best.