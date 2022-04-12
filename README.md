<h1 align="center">LifecycleProps</h1>
<p align="center">
    <a href="LICENSE.md"><img alt="Apache-2.0 License" src="https://img.shields.io/badge/License-Apache%202.0-blue?style=flat"/></a>
    <a href="https://developer.android.com/about/versions"><img src="https://img.shields.io/badge/API-14%2B-brightgreen?style=flat" alt="API 14+" /></a>
    <a href="https://search.maven.org/artifact/io.github.pubiqq/lifecycleprops"><img src="https://img.shields.io/maven-central/v/io.github.pubiqq/lifecycleprops?style=flat&label=Maven%20Central&color=orange" /></a>
</p>
<p align="center">Property delegates that enable you to associate properties with <a href="https://developer.android.com/topic/libraries/architecture/lifecycle">lifecycle-aware components</a>.</p>
<p align="center">
<img src="./assets/banner.svg" width="540" />
</p>

## Table of contents
- [Setup](#setup)
- [Overview](#overview)
  - [`lifecycleAware` delegate](#lifecycleaware-delegate)
  - [`viewLifecycleAware` delegate](#viewlifecycleaware-delegate)
  - [Custom options for lifecycle-aware delegates](#custom-options-for-lifecycle-aware-delegates)
- [Recipes](#recipes)
  - [AutoCleared](#autocleared)
  - [ViewBinding](#viewbinding)
- [License](#license)

## Setup
Add dependency to the module-level `build.gradle` file:
```kotlin
dependencies {
    implementation("io.github.pubiqq:lifecycleprops:latest.release")
}
```

Make sure you have `mavenCentral()` repository in the list of repositories.

## Overview
### `lifecycleAware` delegate
This delegate associates the property with the `LifecycleOwner` lifecycle (it can be `AppCompatActivity`, `Fragment`, `NavBackStackEntry` and so on).

For read-only property, the delegate lazily initializes the property value, associates it with the `LifecycleOwner` lifecycle, and clears it when the `ON_DESTROY` event is reached:

```kotlin
val locationService: MyLocationService by lifecycleAware(
    initializer = { MyLocationService(context, locationCallback) },
    onStart = { start() },
    onStop = { stop() }
)
```

For read/write property, the delegate associates the property with the `LifecycleOwner` lifecycle, and clears it when the `ON_DESTROY` event is reached. Initialization and modification of the property value in this case are carried out directly:

```kotlin
var player: MyPlayer by lifecycleAware(
    onDestroy = { release() }   // called only if the value is present
)

// ...

player = MyPlayer.Builder(context).build()
```

### `viewLifecycleAware` delegate
This is a special delegate for `Fragment` that associates the property with the fragment's view lifecycle. It's similar to `lifecycleAware` and ensures safe binding to `viewLifecycleOwner`:

```kotlin
val overlay: MyOverlay by viewLifecycleAware(
    initializer = { MyOverlay(context) },
    onCreate = { show() },
    onDestroy = { dismiss() }
)
```

Like `lifecycleAware`, the delegate can be used for read/write properties too:

```kotlin
val map: MyMap by viewLifecycleAware(
    onDestroy = { release() }
)

// ...

map = mapFragment.awaitMap()
```

### Custom options for lifecycle-aware delegates
> *The API that provides options support for lifecycle-aware delegates is marked with the `LifecycleAwareOptions` annotation.*
> 
> *Usages of such API will be reported as warnings unless an explicit opt-in with the `OptIn` annotation, e.g. `@OptIn(LifecycleAwareOptions::class)`, or with the `-opt-in=com.pubiqq.lifecycleprops.LifecycleAwareOptions` compiler option is given.*

By default, lifecycle-aware delegates lazily initialize the property value (if an initializer is present) and clear it when `ON_DESTROY` is reached. 
This behavior is the most appropriate for most cases, but you can change it by using the overloaded delegate creation function with the `options` argument:

```kotlin
val tooltip: MyTooltip by lifecycleAware(
    options = LifecycleAwareReadOnlyOptions(
        initializationStrategy = LifecycleAwareInitializationStrategy.Eager,  // use initializer immediately
        deinitializationStrategy = LifecycleAwareDeinitializationStrategy.None  // don't clear `tooltip` when ON_DESTROY is reached
    ),
    initializer = { MyTooltip.create(context) },
    onCreate = { bindTo(binding.button) },
    onDestroy = { unbindAll() }
)
```

## Recipes
### AutoCleared
[`autoCleared`](https://github.com/android/architecture-components-samples/blob/8f536f2b7012c3c4d7bf80fec0de62893d53edbc/GithubBrowserSample/app/src/main/java/com/android/example/github/util/AutoClearedValue.kt) is a special delegate for a read/write property that clears its value when the fragment's view is destroyed. 

`viewLifecycleAware` has the same behavior as `autoCleared` by default, so you can easily replace one with the other:
```kotlin
var binding: SampleFragmentBinding by viewLifecycleAware()

// Similar behavior can be achieved for Activity as well
var binding: SampleActivityBinding by lifecycleAware()
```

If you don't want to replace delegates in already written code or just want to keep the familiar name, you can write your own extension:
```kotlin
fun <T : ViewBinding> Fragment.autoCleared() = viewLifecycleAware<T>()
fun <T : ViewBinding> ComponentActivity.autoCleared() = lifecycleAware<T>()
```

In a similar way, you can implement the `autoCleared` extension with support for a callback that is called in response to a property cleanup event:
```kotlin
fun <T : Any> Fragment.autoCleared(
    beforeClearProperty: (property: T) -> Unit = { /* no-op */ }
) = viewLifecycleAware<T>(
    onDestroy = { beforeClearProperty(this) }
)

fun <T : Any> ComponentActivity.autoCleared(
    beforeClearProperty: (property: T) -> Unit = { /* no-op */ }
) = lifecycleAware<T>(
    onDestroy = { beforeClearProperty(this) }
)
```

### ViewBinding
[There](https://github.com/Zhuinden/fragmentviewbindingdelegate-kt)
[are](https://github.com/yogacp/android-viewbinding)
[many](https://github.com/androidbroadcast/ViewBindingPropertyDelegate)
[libraries](https://github.com/hoc081098/ViewBindingDelegate)
[and](https://medium.com/default-to-open/handling-lifecycle-with-view-binding-in-fragments-a7f237c56832)
[articles](https://proandroiddev.com/viewbinding-with-kotlin-property-delegate-c907682e24c9)
that describe how to create a delegate that provides a `ViewBinding` object without code boilerplate and correctly clears the associated property when the view is destroyed.

All these solutions use a similar principle, which you can easily implement using the `lifecycleAware` delegate (for Activity) or the `viewLifecycleAware` delegate (for Fragment).

So, for example, you can implement `viewBinding` using reflection and the `ViewBinding.bind` method:
```kotlin
// Creates `ViewBinding` instance via `ViewBinding.bind` method (reflective)
//
// Pitfalls:
//  - Requires calling the constructor with `contentLayoutId` parameter or explicitly implementing the `onCreateView` method
//  - Requires added proguard rule for `ViewBinding.bind` methods
//  - The property becomes available only after executing the `onCreateView` method
inline fun <reified T : ViewBinding> Fragment.viewBinding() = viewLifecycleAware(
    initializer = {
        val bindMethod = T::class.java.getMethod("bind", View::class.java)
        bindMethod.invoke(null, requireView()) as T
    }
)

// Creates `ViewBinding` instance via `ViewBinding.bind` method (reflective)
//
// Pitfalls:
//  - Requires calling the constructor with `contentLayoutId` parameter or explicitly calling the `setContentView` method
//  - Requires added proguard rule for `ViewBinding.bind` methods
//  - The property becomes available only after executing the `setContentView` method
inline fun <reified T : ViewBinding> ComponentActivity.viewBinding() = lifecycleAware(
    initializer = {
        val bindMethod = T::class.java.getMethod("bind", View::class.java)
        val view = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        bindMethod.invoke(null, view) as T
    }
)
```

```kotlin
// SampleFragment.kt

class SampleFragment : Fragment(R.layout.sample_fragment) {

    private val binding: SampleFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vMessage.text = "Test message"
    }
}
```

And this is how the implementation of `viewBinding` for an activity can look like using the `ViewBinding.inflate` method and without using reflection:
```kotlin
// Creates `ViewBinding` instance via `ViewBinding.inflate` method (non-reflective)
//
// Pitfalls:
//  - Requires manual invocation of the `setContentView(binding.root)` method
fun <T : ViewBinding> ComponentActivity.viewBinding(inflateMethod: (LayoutInflater) -> T) =
    lifecycleAware(
        initializer = { inflateMethod(layoutInflater) }
    )
```

```kotlin
// SampleActivity.kt

class SampleActivity : AppCompatActivity() {

    private val binding by viewBinding(SampleActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
```

Despite the fact that the use of such delegates is quite common, *I don't recommend using this approach*, because it doesn't significantly reduce the amount of code, but requires implicit conditions to be met. 
Instead, I recommend (but don't insist) using `autoCleared`/`lifecycleAware`/`viewLifecycleAware` delegate + manual initialization of the binding.
Also, in order to simplify the work with binding, I suggest considering alternative solutions - base activities/fragments, custom Android Studio templates, or code gen.

## License

    Copyright 2021 pubiqq
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.