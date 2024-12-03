Change Log
==========

## 3.1.0

_2024-12-03_

### What's new

- Updated `compileSdk` to 35.

### Library dependency updates

| Dependency        | Previous version | New version |
|-------------------|------------------|-------------|
| Kotlin            | 2.0.0            | 2.1.0       |
| AndroidX Fragment | 1.7.1            | 1.8.0       |

## 3.0.0

_2024-06-10_

### What's new

- KMP support.
- Bumped `minSdk` to 21 due to the transition of AndroidX libraries to the default minimum supported Android API level
  21 (see [caution](http://web.archive.org/web/20240521102301/https://developer.android.com/jetpack/androidx/versions#version-table)).

## 2.2.0

_2024-05-28_

### What's new

- Bumped `minSdk` to 19 due to the transition of AndroidX libraries to the minimum supported Android API level 19 (see
  [blog post](https://android-developers.googleblog.com/2023/10/androidx-minsdkversion-19.html)).
- Updated `compileSdk` to 34.

### Library dependency updates

| Dependency         | Previous version | New version |
|--------------------|------------------|-------------|
| Kotlin             | 1.9.0            | 2.0.0       |
| AndroidX Fragment  | 1.6.0            | 1.7.1       |
| AndroidX Lifecycle | 2.6.1            | 2.8.0       |

## 2.1.0

_2023-07-07_

No changes except dependency updates.

### Library dependency updates

| Dependency         | Previous version | New version |
|--------------------|------------------|-------------|
| Kotlin             | 1.8.0            | 1.9.0       |
| AndroidX Fragment  | 1.5.7            | 1.6.0       |
| AndroidX Lifecycle | 2.5.1            | 2.6.1       |

## 2.0.0

_2023-06-13_

### What's new

- Added global configurations for lifecycle-aware properties (see
  [`LifecycleProps`](https://github.com/pubiqq/lifecycleprops/blob/2.0.0/lifecycleprops/src/main/kotlin/com/pubiqq/lifecycleprops/LifecycleProps.kt)).
- `lifecycleAware` and `viewLifecycleAware` delegates now throw an exception if the property value is not initialized
  when a lifecycle event with a handler occurs (before, the delegates didn't throw exceptions in this case). This change
  is made to ensure that all lifecycle event handlers will be invoked for the property.
- Read/write `lifecycleAware` and `viewLifecycleAware` delegates now throw an exception when trying to assign a value to
  an already initialized property (before, the delegates didn't throw exceptions in this case). This change is made to
  ensure that all lifecycle event handlers will be invoked for the same object within the lifecycle.
- All delegates now automatically close
  [`AutoCloseable`](https://docs.oracle.com/javase/7/docs/api/java/lang/AutoCloseable.html) properties when an
  `ON_DESTROY` event occurs.

### Library dependency updates

| Dependency         | Previous version | New version |
|--------------------|------------------|-------------|
| Kotlin             | 1.7.0            | 1.8.0       |
| AndroidX Activity  | 1.4.0            | - (removed) |
| AndroidX Fragment  | 1.4.1            | 1.5.7       |
| AndroidX Lifecycle | 2.4.1            | 2.5.1       |

## 1.1.0

_2022-06-27_

No changes except dependency updates.

### Library dependency updates

| Dependency | Previous version | New version |
|------------|------------------|-------------|
| Kotlin     | 1.6.10           | 1.7.0       |

## 1.0.0

_2022-04-12_

Initial release