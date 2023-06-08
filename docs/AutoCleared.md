# AutoCleared

[`autoCleared`](https://github.com/android/architecture-components-samples/blob/8f536f2b7012c3c4d7bf80fec0de62893d53edbc/GithubBrowserSample/app/src/main/java/com/android/example/github/util/AutoClearedValue.kt)
is a delegate for a read/write property that clears its value when the fragment's view is destroyed.

`viewLifecycleAware` has the same behavior as `autoCleared` by default, so you can easily replace
one with the other:

```kotlin
var adapter by viewLifecycleAware<MyAdapter>()
```

If you don't want to replace delegates in already written code or just want to keep the familiar
name, you can write your own extension:

```kotlin
fun <T : Any> Fragment.autoCleared() = viewLifecycleAware<T>()
```

In a similar way, you can implement the `autoCleared` extension with support for a callback that is
called in response to a property cleanup event:

```kotlin
@OptIn(LifecycleAwareConfigurationApi::class)
private class AutoClearedConfiguration<T : Any>(
    private val beforeClearProperty: (property: T) -> Unit = { /* no-op */ }
) : LifecycleAwareReadWriteConfiguration<T> by LifecycleAwareReadWriteConfiguration.Default() {

    override fun onClear(value: T) {
        beforeClearProperty(value)
    }
}

@OptIn(LifecycleAwareConfigurationApi::class)
fun <T : Any> Fragment.autoCleared(
    beforeClearProperty: (property: T) -> Unit = { /* no-op */ }
) = viewLifecycleAware(
    configuration = AutoClearedConfiguration(beforeClearProperty)
)
```