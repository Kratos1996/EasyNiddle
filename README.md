# EasyNiddleNavigation

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kratos1996/easyniddle.svg?label=maven-central)](https://central.sonatype.com/artifact/io.github.kratos1996/easyniddle)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.3-blue)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)

A lightweight, DSL-based navigation library for Jetpack Compose, built on top of Navigation 3. It simplifies navigation by removing the need for string-based routes and complex XML configurations.

## Features

- **Type-Safe Routes**: Use Kotlin objects and classes as routes.
- **DSL Builder**: Easily define your navigation graph with a clean DSL.
- **Standalone Navigator**: `NiddleNavigator` can be injected via DI (Hilt/Koin) or provided via `CompositionLocal`.
- **Built-in Result API**: Send data back to the previous screen easily with `navigateBackWithResult`.
- **Advanced Navigation Options**: Supports `popUpTo`, `inclusive`, `singleTop`, and Bottom Bar navigation patterns.
- **Centralized Logging**: Debug your navigation flow with built-in, build-type aware logging.
- **Seamless Compose Integration**: Provides `NiddleNavHost` and `LocalNiddleNavigator`.
- **Navigation 3 Support**: Built on the latest `androidx.navigation3` APIs.
- **ProGuard Ready**: Includes consumer rules to protect your routes in minified builds.

## Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.kratos1996:easyniddle:1.0.1")
}
```

## Quick Start

### 1. Define your Routes

All routes must implement `SerializableRoute` and be marked with `@Serializable`.

```kotlin
@Serializable
sealed interface AppRoute : SerializableRoute {
    @Serializable
    data object Home : AppRoute
    
    @Serializable
    data class Details(val id: String) : AppRoute
}
```

### 2. Set up the NavHost

```kotlin
NiddleNavHost<AppRoute>(startDestination = AppRoute.Home) {
    screen<AppRoute.Home> {
        HomeScreen()
    }
    screen<AppRoute.Details> { route ->
        DetailsScreen(route.id)
    }
}
```

### 3. Navigate

Use `LocalNiddleNavigator` to get the navigator in your Composables.

```kotlin
val navigator = LocalNiddleNavigator.current

Button(onClick = { navigator.navigate(AppRoute.Details("123")) }) {
    Text("Go to Details")
}
```

## Features in Depth

### Navigation with Results

**Screen A**:
```kotlin
val navigator = LocalNiddleNavigator.current
// Listen for results
LaunchedEffect(Unit) {
    navigationResult.collect { result ->
        if (result is String) { /* Handle result */ }
    }
}
```

**Screen B**:
```kotlin
navigator.navigateBackWithResult("Task Completed!")
```

### DI Support (Hilt)

Provide `NiddleNavigator` in your `SingletonComponent`:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    @Singleton
    fun provideNiddleNavigator(): NiddleNavigator = NiddleNavigator()
}
```

Then inject it in your ViewModels:

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val navigator: NiddleNavigator
) : ViewModel() {
    fun onUserClick() {
        navigator.navigate(AppRoute.Details("456"))
    }
}
```

### Centralized Logging System

EasyNiddle includes a centralized logger (`NiddleLogger`) that tracks all navigation events.

- **Default Behavior**: Logs are shown **only in Debug builds** using the tag `NiddleNavigation`.
- **Release Logging**: If you need to see logs in a Release build for troubleshooting, enable it via:
  ```kotlin
  NiddleLogger.showLogsInRelease = true
  ```

### Advanced Commands

- `navigator.pop()`: Go back.
- `navigator.popTo(route, inclusive)`: Pop back to a specific route.
- `navigator.navigate(route, popUpTo, inclusive)`: Navigate and clear part of the stack.
- `navigator.navigateSingleTop(route)`: Navigate with singleTop behavior.
- `navigator.navigateBottomBar(route)`: Specialized command for bottom bar switches.
- `navigator.finish()`: Finish the current Activity.

### ProGuard / R8 Configuration

The library includes **Consumer Rules**, meaning you don't need to add anything to your app's `proguard-rules.pro`. It automatically protects:
- All `SerializableRoute` implementations.
- Kotlin Serialization companion objects and serializers.
- Public library APIs.

## Testing

The library is designed to be testable. You can use `runTest` to verify navigation events:

```kotlin
@Test
fun testNavigation() = runTest {
    val navigator = NiddleNavigator()
    val results = mutableListOf<Navigator>()
    val job = launch(UnconfinedTestDispatcher()) {
        navigator.navEvents.collect { results.add(it) }
    }

    navigator.navigate(AppRoute.Home)
    
    assertTrue(results[0] is Navigator.NavigateRoute)
    job.cancel()
}
```

## License

```
Copyright 2026 Ishant

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
