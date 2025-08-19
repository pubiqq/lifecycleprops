# ViewBinding delegates

To reduce boilerplate and simplify work with [view binding][1], it's often suggested to use special delegates that
provide a `ViewBinding` object and automatically clear the associated property when the view is destroyed.

The library does not provide specialized delegates for this purpose. [There][2] [are][3] [various][4] [ways][5]
to implement them and none of them is objectively the best, each has its own advantages and disadvantages.
Instead, it's suggested to create them yourself using `lifecycleAware` and `viewLifecycleAware` delegates and whatever 
way of creating a `ViewBinding` instance you find most suitable.

Here are some examples of different implementations of view binding delegates:

```kotlin
// Provides `ViewBinding` for the fragment using the `ViewBinding.bind` method (with reflection)
inline fun <reified T : ViewBinding> Fragment.viewBinding() = viewLifecycleAware(
    initializer = {
        val bindMethod = T::class.java.getMethod("bind", View::class.java)
        bindMethod.invoke(null, requireView()) as T
    }
)

// Provides `ViewBinding` for the activity using the `ViewBinding.bind` method (with reflection)
inline fun <reified T : ViewBinding> ComponentActivity.viewBinding() = lifecycleAware(
    initializer = {
        val bindMethod = T::class.java.getMethod("bind", View::class.java)
        val view = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        bindMethod.invoke(null, view) as T
    }
)

// Provides `ViewBinding` for the fragment using the `ViewBinding.bind` method (without reflection)
fun <T : ViewBinding> Fragment.viewBinding(bindMethod: (View) -> T) = viewLifecycleAware(
    initializer = { bindMethod(requireView()) }
)

// Provides `ViewBinding` for the activity using the `ViewBinding.inflate` method (without reflection)
fun <T : ViewBinding> ComponentActivity.viewBinding(inflateMethod: (LayoutInflater) -> T) =
    lifecycleAware(
        initializer = { inflateMethod(layoutInflater) }
    )
```

## Notes

- ViewBinding delegates typically do not bind views to activities or fragments, but only provide convenient access to a
  `ViewBinding` object. The actual binding is usually done separately, most often using a constructor with the
  `contentLayoutId` parameter:

  ```kotlin
  inline fun <reified T : ViewBinding> Fragment.viewBinding() = viewLifecycleAware(
      initializer = {
          val bindMethod = T::class.java.getMethod("bind", View::class.java)
          bindMethod.invoke(null, requireView()) as T
      }
  )

  // ...

  class SampleFragment : Fragment(R.layout.fragment_sample) {  // <- don't forget to pass `contentLayoutId` to the constructor

      private val binding: FragmentSampleBinding by viewBinding()

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
          binding.message.text = "Test message"
      }
  }
  ```

  However, it can be done with the `setContentView()` and `onCreateView()` methods too:

  ```kotlin
  fun <T : ViewBinding> ComponentActivity.viewBinding(inflateMethod: (LayoutInflater) -> T) =
      lifecycleAware(
          initializer = { inflateMethod(layoutInflater) }
      )

  // ...

  class SampleActivity : AppCompatActivity() {

      private val binding by viewBinding(ActivitySampleBinding::inflate)

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(binding.root)  // <- don't forget to call the `setContentView` method
      }
  }
  ```

- Accessing ViewBinding properties is only valid within the component's view lifecycle. Outside the lifecycle, accessing
  them is meaningless and should be avoided:

  ```kotlin
  inline fun <reified T : ViewBinding> Fragment.viewBinding() = viewLifecycleAware(
      initializer = {
          val bindMethod = T::class.java.getMethod("bind", View::class.java)
          bindMethod.invoke(null, requireView()) as T
      }
  )

  // ...

  class SampleFragment : Fragment(R.layout.fragment_sample) {

    private val binding: FragmentSampleBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.message.text = "onCreate"  // Don't do this, the view is not created here
    }

    override fun onStart() {
        super.onStart()
        binding.message.text = "onStart"  // Ok
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.message.text = "onDestroy"  // Don't do this, the view is destroyed here
    }
  }
  ```

- If you implement ViewBinding delegates via reflection, don't forget to add the appropriate R8/ProGuard rules. The
  specific set of rules depends on the methods that need to be accessed reflectively. For example, this is what it might
  look like for the `ViewBinding.bind` and `ViewBinding.inflate` methods:

  ```proguard
  -keepclassmembers class * implements androidx.viewbinding.ViewBinding {
      public static *** bind(android.view.View);
      public static *** inflate(android.view.LayoutInflater);
  }
  ```

[1]: https://developer.android.com/topic/libraries/view-binding
[2]: https://github.com/Zhuinden/fragmentviewbindingdelegate-kt
[3]: https://github.com/DylanCaiCoding/ViewBindingKTX
[4]: https://medium.com/default-to-open/handling-lifecycle-with-view-binding-in-fragments-a7f237c56832
[5]: https://proandroiddev.com/viewbinding-with-kotlin-property-delegate-c907682e24c9
