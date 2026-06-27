package dev.ishant.easyniddle.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * A standalone navigator that can be injected via DI or provided via CompositionLocal.
 * It is not tied to any ViewModel.
 */
class NiddleNavigator {
    private val _navEvents = MutableSharedFlow<Navigator>(extraBufferCapacity = 64)
    val navEvents = _navEvents.asSharedFlow()

    /**
     * Navigate to a new route.
     */
    fun navigate(route: SerializableRoute) {
        _navEvents.tryEmit(Navigator.NavigateRoute(route))
    }

    /**
     * Navigate to a route and pop up to another route.
     */
    fun navigate(route: SerializableRoute, popUpTo: SerializableRoute, inclusive: Boolean = false) {
        _navEvents.tryEmit(Navigator.NavigateWithPopUp(route, popUpTo, inclusive))
    }

    /**
     * Navigate to a route with singleTop behavior.
     */
    fun navigateSingleTop(route: SerializableRoute) {
        _navEvents.tryEmit(Navigator.NavigateToBackstackScreenAsSingleTop(route))
    }

    /**
     * Navigate to a route as a bottom bar destination.
     * Pops to start destination and uses singleTop.
     */
    fun navigateBottomBar(route: SerializableRoute) {
        _navEvents.tryEmit(Navigator.NavigateBottomBar(route))
    }

    /**
     * Pop the back stack.
     */
    fun pop() {
        _navEvents.tryEmit(Navigator.NavigateBack)
    }

    /**
     * Pop back to a specific route.
     */
    fun popTo(route: SerializableRoute, inclusive: Boolean = false) {
        _navEvents.tryEmit(Navigator.NavigatePopBackStack(route, inclusive))
    }

    /**
     * Finish the current activity.
     */
    fun finish() {
        _navEvents.tryEmit(Navigator.FinishActivity)
    }

    /**
     * Navigate with a result.
     */
    fun navigateBackWithResult(result: Any) {
        _navEvents.tryEmit(Navigator.NavigateBackWithResult(result))
    }

    /**
     * Internal generic navigate method for custom commands.
     */
    fun navigate(command: Navigator) {
        _navEvents.tryEmit(command)
    }
}

/**
 * CompositionLocal to provide the navigator throughout the UI tree.
 */
val LocalNiddleNavigator = staticCompositionLocalOf<NiddleNavigator> {
    error("No NiddleNavigator provided")
}
