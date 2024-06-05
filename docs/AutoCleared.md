# AutoCleared

Many codebases that use fragments often have a special [`autoCleared`][1] delegate that clears the property value when
the fragment's view is destroyed.

With LifecycleProps, you can use the `viewLifecycleAware` delegate to achieve this:

```kotlin
var adapter by viewLifecycleAware<MyAdapter>()
```

This delegate not only clears the value when the fragment's view is destroyed, but also:

- Throws an exception if you try to assign a value to an already initialized property.
- Before clearing, calls the `close()` method if the class implements the `AutoCloseable` interface.

If you don't need these guarantees, you can implement a delegate that doesn't have them using the configuration:

```kotlin
@file:OptIn(ExperimentalConfigurationApi::class)

private class AutoClearedConfiguration<T : Any> :
    LifecycleAwareReadWriteConfiguration<T> by LifecycleAwareReadWriteConfiguration.Default() {

    override val allowReassign: Boolean = true

    override fun onClear(value: T) { /* no-op */ }
}

fun <T : Any> Fragment.autoCleared() = viewLifecycleAware(
    configuration = AutoClearedConfiguration<T>()
)
```

[1]: https://github.com/android/architecture-components-samples/blob/8f536f2b7012c3c4d7bf80fec0de62893d53edbc/GithubBrowserSample/app/src/main/java/com/android/example/github/util/AutoClearedValue.kt
