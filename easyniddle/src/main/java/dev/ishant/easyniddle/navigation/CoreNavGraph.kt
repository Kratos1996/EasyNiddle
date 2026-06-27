package dev.ishant.easyniddle.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEvent

@Composable
fun <T : NavKey> CoreNavGraph(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<T>,
    onBack: () -> Unit = { backStack.removeLastOrNull() },
    entryDecorators: List<NavEntryDecorator<T>> = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    ),
    sceneStrategy: SceneStrategy<T> = SinglePaneSceneStrategy(),
    sizeTransform: SizeTransform? = null,
    transitionSpec: AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform =
        TransitionSpecConstants.defaultTransitionSpec(),
    popTransitionSpec: AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform =
        TransitionSpecConstants.defaultPopTransitionSpec(),
    predictivePopTransitionSpec:
    AnimatedContentTransitionScope<Scene<T>>.(
        @NavigationEvent.SwipeEdge Int
    ) -> ContentTransform =
        TransitionSpecConstants.defaultPredictivePopTransitionSpec(),
    entryProvider: (key: T) -> NavEntry<out T>,
) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        sceneStrategies = listOf(sceneStrategy),
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryDecorators = entryDecorators,
        entryProvider = entryProvider as (T) -> NavEntry<T>
    )
}
