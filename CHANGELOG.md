Change Log
==========

## 2.0.0

_2023-06-13_

### What's new

- Added global configurations for lifecycle-aware properties (see
  [`LifecycleProps`](lifecycleprops/src/main/kotlin/com/pubiqq/lifecycleprops/LifecycleProps.kt)).
- `lifecycleAware` and `viewLifecycleAware` delegates now throw an exception if the property value
  is not initialized when a lifecycle event with a handler occurs (before, the delegates didn't
  throw exceptions in this case). This change is made to ensure that all lifecycle event handlers
  will be invoked for the property.
- Read/write `lifecycleAware` and `viewLifecycleAware` delegates now throw an exception when
  trying to assign a value to an already initialized property (before, the delegates didn't throw
  exceptions in this case). This change is made to ensure that all lifecycle event handlers will be
  invoked for the same object within the lifecycle.
- All delegates now automatically close associated properties that implement the
  [`AutoCloseable`](https://docs.oracle.com/javase/7/docs/api/java/lang/AutoCloseable.html)
  interface.

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