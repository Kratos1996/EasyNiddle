package dev.ishant.easyniddle.navigation

import android.content.Context
import android.os.Build
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.ishant.easyniddle.constant.AppConstants
import dev.ishant.easyniddle.util.NiddleLogger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest

sealed class Navigator {
    // Navigates to a specific route
    data class NavigateRoute(
        val route: SerializableRoute
    ) : Navigator()

    // Pops the back stack up to a specific route, optionally including the given route
    data class NavigatePopBackStack(
        val route: SerializableRoute,
        val inclusive: Boolean,
        val saveState: Boolean = false
    ) : Navigator()

    // Navigates within the bottom bar, optionally saving and restoring state
    data class NavigateBottomBar(
        val route: SerializableRoute,
        val saveState: Boolean = true,
        val launchSingleTop: Boolean = true,
        val restoreState: Boolean = true
    ) : Navigator()

    data class NavigateWithPopUp(
        val route: SerializableRoute,
        val popUpToRoute: SerializableRoute,
        val inclusive: Boolean = false,
    ) : Navigator()

    data class NavigateToBackstackScreenAsSingleTop(
        val route: SerializableRoute,
    ) : Navigator()

    // Finishes the current activity
    data object FinishActivity : Navigator()

    // Navigates back in the stack
    data object NavigateBack : Navigator()

    // Navigates back with a result
    data class NavigateBackWithResult(
        val result: Any
    ) : Navigator()

    // For custom navigation
    data class NavigateCustom(
        val route: SerializableRoute
    ) : Navigator()
}
interface SerializableRoute : java.io.Serializable , NavKey {
    fun readResolve(): Any = this
}

fun Context.getStartDestinationRoute(): java.io.Serializable? {
    val activity = findActivity()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity?.intent?.getSerializableExtra(
            AppConstants.START_DESTINATION,
            java.io.Serializable::class.java
        )
    } else {
        @Suppress("DEPRECATION")
        activity?.intent?.getSerializableExtra(AppConstants.START_DESTINATION) as? java.io.Serializable
    }
}

fun Context.getSerializableIntentExtra(key: String): java.io.Serializable? {
    val activity = findActivity()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity?.intent?.getSerializableExtra(key, java.io.Serializable::class.java)
    } else {
        @Suppress("DEPRECATION")
        activity?.intent?.getSerializableExtra(key) as java.io.Serializable
    }
}

private val _navigationResult = MutableSharedFlow<Any>(extraBufferCapacity = 1)
val navigationResult = _navigationResult.asSharedFlow()

suspend fun <T : SerializableRoute> navigation(
    backStack: NavBackStack<T>,
    context: Context,
    navigator: NiddleNavigator,
    onNavigateCustom: (route: T) -> Unit = {}
) {
    navigator.navEvents.collectLatest { navigationData ->
        NiddleLogger.d("Received Navigation Event: $navigationData")
        @Suppress("UNCHECKED_CAST")
        navigationCases(
            navigationData = navigationData,
            backStack = backStack as NavBackStack<SerializableRoute>,
            context = context,
            onNavigateCustom = onNavigateCustom as (SerializableRoute) -> Unit
        )
    }
}
private fun navigationCases(
    navigationData: Navigator,
    backStack: NavBackStack<SerializableRoute>,
    context: Context,
    onNavigateCustom: (route: SerializableRoute) -> Unit
) {
    when (navigationData) {
        is Navigator.NavigateRoute -> {
            backStack.add(navigationData.route)
        }

        is Navigator.NavigatePopBackStack -> {
            val targetIndex = backStack.indexOf(navigationData.route)

            if (targetIndex != -1) {
                val removeFromIndex = if (navigationData.inclusive) targetIndex else targetIndex + 1

                if (removeFromIndex < backStack.size) {
                    backStack.subList(removeFromIndex, backStack.size).clear()
                }
            }
        }

        is Navigator.NavigateWithPopUp -> {
            val popUpToIndex = backStack.indexOf(navigationData.popUpToRoute)

            if (popUpToIndex != -1) {
                val removeFromIndex = if (navigationData.inclusive) popUpToIndex else popUpToIndex + 1

                if (removeFromIndex < backStack.size) {
                    backStack.subList(removeFromIndex, backStack.size).clear()
                }
            }

            backStack.add(navigationData.route)
        }

        is Navigator.NavigateToBackstackScreenAsSingleTop -> {
            val existingIndex = backStack.indexOf(navigationData.route)

            if (existingIndex != -1) {
                // Pop to existing destination
                if (existingIndex + 1 < backStack.size) {
                    backStack.subList(existingIndex + 1, backStack.size).clear()
                }
            } else {
                // launchSingleTop: only add if not already at top
                if (backStack.lastOrNull() != navigationData.route) {
                    backStack.add(navigationData.route)
                }
            }
        }

        is Navigator.NavigateBottomBar -> {
            // Pop to start destination (keep only first item)
            if (backStack.size > 1) {
                backStack.subList(1, backStack.size).clear()
            }

            // Navigate with launchSingleTop behavior
            if (navigationData.launchSingleTop) {
                if (backStack.lastOrNull() != navigationData.route) {
                    backStack.add(navigationData.route)
                }
            } else {
                backStack.add(navigationData.route)
            }
        }

        is Navigator.FinishActivity -> {
            context.navigateBack()
        }

        is Navigator.NavigateBack -> {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            } else {
                context.navigateBack()
            }
        }

        is Navigator.NavigateBackWithResult -> {
            _navigationResult.tryEmit(navigationData.result)
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            } else {
                context.navigateBack()
            }
        }

        is Navigator.NavigateCustom -> {
            onNavigateCustom(navigationData.route)
        }
    }
}
