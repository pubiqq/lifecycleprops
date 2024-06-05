<h1 align="center">LifecycleProps</h1>
<p align="center">
    <a href="LICENSE.txt"><img src="https://img.shields.io/badge/License-Apache%202.0-blue?style=flat" alt="Apache-2.0 License" /></a>
    <a href="https://developer.android.com/about/versions/lollipop"><img src="https://img.shields.io/badge/Android-21%2B-brightgreen?style=flat" alt="API 21+" /></a>
    <a href="https://search.maven.org/artifact/io.github.pubiqq/lifecycleprops"><img src="https://img.shields.io/maven-central/v/io.github.pubiqq/lifecycleprops?style=flat&label=Maven%20Central&color=orange" alt="Available on Maven Central" /></a>
    <a href="https://www.jetbrains.com/kotlin-multiplatform/"><img src="https://img.shields.io/badge/Kotlin-Multiplatform-%237f52ff?logo=kotlin" alt="Kotlin Multiplatform" /></a>
</p>
<p align="center">Property delegates that enable you to associate properties with <a href="https://developer.android.com/topic/libraries/architecture/lifecycle">lifecycle-aware components</a>.</p>
<p align="center">
<img src="./assets/banner.svg" width="540" alt="" />
</p>

## Setup

Add dependency to the module-level `build.gradle` file:

```kotlin
dependencies {
    implementation("io.github.pubiqq:lifecycleprops:latest.release")
}
```

Make sure you have `mavenCentral()` repository in the list of repositories.

## Usage

### Lifecycle-aware delegates

To associate a property to the lifecycle of a `LifecycleOwner` object (such as `AppCompatActivity`, `Fragment`,
`NavBackStackEntry`, etc.), use the `lifecycleAware` function:

```kotlin
class MyActivity : AppCompatActivity() {

    // Associates the read-only property with the `MyActivity` lifecycle
    val locationService: MyLocationService by lifecycleAware(
        initializer = { MyLocationService(context, locationCallback) },
        onStart = { start() },
        onStop = { stop() }
    )

    // Associates the read/write property with the `MyActivity` lifecycle
    var banner: MyBanner by lifecycleAware(
        onStart() = { start() },
        onResume() = { resume() },
        onPause() = { pause() },
        onStop() = { stop() }
    )

    // ...

    banner = MyBanner.Builder(context).build()  // manual initialization of the read/write property
}
```

To associate a property with the `Fragment`'s view lifecycle, use `viewLifecycleAware`:

```kotlin
class MyFragment : Fragment() {

    // Associates the read-only property with the `MyFragment`'s view lifecycle
    val locationService: MyLocationService by viewLifecycleAware(
        initializer = { MyLocationService(context, locationCallback) },
        onStart = { start() },
        onStop = { stop() }
    )

    // Associates the read/write property with the `MyFragment`'s view lifecycle
    var banner: MyBanner by viewLifecycleAware(
        onStart() = { start() },
        onResume() = { resume() },
        onPause() = { pause() },
        onStop() = { stop() }
    )

    // ...

    banner = MyBanner.Builder(context).build()  // manual initialization of a read/write property
}
```

### Custom configurations

> [!IMPORTANT]
> The API that provides configurations support for lifecycle-aware delegates is marked with the
> `ExperimentalConfigurationApi` annotation.
>
> Usages of such API will be reported as warnings unless an explicit opt-in with the `OptIn` annotation, e.g.
> `@OptIn(ExperimentalConfigurationApi::class)`, or with the
> `-opt-in=com.pubiqq.lifecycleprops.ExperimentalConfigurationApi` compiler option is given.

By default, lifecycle-aware delegates for read-only properties:

- Lazily initialize the associated property.
- Close (if [`AutoCloseable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-auto-closeable/#autocloseable)) and
  null out the property value when an `ON_DESTROY` event occurs.

Lifecycle-aware delegates for read/write properties:

- Ensure that a value will not be reassigned to an already initialized property (otherwise an
  [`IllegalStateException`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/#illegalstateexception)
  will be thrown).
- Ensure that each provided event handler will be invoked for the property (otherwise an
  [`IllegalStateException`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/#illegalstateexception)
  will be thrown).
- Close (if [`AutoCloseable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-auto-closeable/#autocloseable)) and
  null out the property value when an `ON_DESTROY` event occurs.

If you want to change the behavior of the lifecycle-aware property, you can specify your own custom configuration:

```kotlin
@file:OptIn(ExperimentalConfigurationApi::class)

// Custom configuration for read-only properties
class MyLifecycleAwareReadOnlyConfiguration<in T : Any> : LifecycleAwareReadOnlyConfiguration<T> {

    override val initializationStrategy: LifecycleAwareInitializationStrategy =
        LifecycleAwareInitializationStrategy.OnAnyAccess

    override val allowSkipHandlerAccessToUninitializedProperty: Boolean = false

    override val shouldNullOutTheProperty: Boolean = true

    override fun onClear(value: T) = Unit
}

// Custom configuration for read/write properties
class MyLifecycleAwareReadWriteConfiguration<in T : Any> : LifecycleAwareReadWriteConfiguration<T> {

    override val allowReassign: Boolean = true

    override val allowSkipHandlerAccessToUninitializedProperty: Boolean = true

    override val shouldNullOutTheProperty: Boolean = true

    override fun onClear(value: T) = Unit
}
```

and apply it to the target property:

```kotlin
@file:OptIn(ExperimentalConfigurationApi::class)

class MyActivity : AppCompatActivity() {

    // Associates the read-only property with the `MyActivity` lifecycle (`MyLifecycleAwareReadOnlyConfiguration` is used)
    val locationService: MyLocationService by lifecycleAware(
        configuration = MyLifecycleAwareReadOnlyConfiguration(),
        initializer = { MyLocationService(context, locationCallback) },
        onStart = { start() },
        onStop = { stop() }
    )

    // Associates the read/write property with the `MyActivity` lifecycle (`MyLifecycleAwareReadWriteConfiguration` is used)
    var banner: MyBanner by lifecycleAware(
        configuration = MyLifecycleAwareReadWriteConfiguration(),
        onStart() = { start() },
        onResume() = { resume() },
        onPause() = { pause() },
        onStop() = { stop() }
    )

    // ...

    banner = MyBanner.Builder(context).build()  // manual initialization of the read/write property
}
```

Also, you can set configurations globally, in which case they will be applied to lifecycle-aware properties by default:

```kotlin
@file:OptIn(ExperimentalConfigurationApi::class)

with(LifecycleProps) {
    // Sets default configurations for lifecycle-aware properties
    setLifecycleAwareConfigurations(
        readOnlyPropsConfiguration = MyLifecycleAwareReadOnlyConfiguration(),
        readWritePropsConfiguration = MyLifecycleAwareReadWriteConfiguration()
    )
}

with(LifecyclePropsAndroid) {
    // Sets default configurations for Android-specific lifecycle-aware properties
    setViewLifecycleAwareConfigurations(
        readOnlyPropsConfiguration = MyLifecycleAwareReadOnlyConfiguration(),
        readWritePropsConfiguration = MyLifecycleAwareReadWriteConfiguration()
    )
}
```

## Samples

Check out the [sample](https://github.com/pubiqq/lifecycleprops/tree/main/sample) project to see the library in action.
Also see:

- [AutoCleared](./docs/AutoCleared.md)
- [ViewBinding delegates](./docs/ViewBindingDelegates.md)

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