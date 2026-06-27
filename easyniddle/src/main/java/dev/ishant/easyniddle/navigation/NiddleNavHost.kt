package dev.ishant.easyniddle.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigationevent.NavigationEvent

/**
 * A simplified NavHost for EasyNiddle navigation.
 * It automatically connects the [NiddleNavigator] to the [NavBackStack].
 */
@Composable
fun <T : SerializableRoute> NiddleNavHost(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<T>,
    navigator: NiddleNavigator,
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
    onNavigateCustom: (route: T) -> Unit = {},
    entryProvider: (key: T) -> NavEntry<out T>,
) {
    val context = LocalContext.current

    LaunchedEffect(navigator, backStack) {
        navigation(backStack, context, navigator, onNavigateCustom)
    }

    CompositionLocalProvider(LocalNiddleNavigator provides navigator) {
        CoreNavGraph(
            modifier = modifier,
            backStack = backStack,
            onBack = onBack,
            entryDecorators = entryDecorators,
            sceneStrategy = sceneStrategy,
            sizeTransform = sizeTransform,
            transitionSpec = transitionSpec,
            popTransitionSpec = popTransitionSpec,
            predictivePopTransitionSpec = predictivePopTransitionSpec,
            entryProvider = entryProvider
        )
    }
}

/**
 * The most minimal version of NavHost.
 * Use this for simple apps where you don't want to manage the navigator or backstack yourself.
 */
@Composable
fun <T : SerializableRoute> NiddleNavHost(
    startDestination: T,
    modifier: Modifier = Modifier,
    navigator: NiddleNavigator = remember { NiddleNavigator() },
    onBack: (NavBackStack<T>) -> Unit = { it.removeLastOrNull() },
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
    onNavigateCustom: (route: T) -> Unit = {},
    builder: NiddleNavGraphBuilder<T>.() -> Unit,
) {
    @Suppress("UNCHECKED_CAST")
    val backStack = rememberNavBackStack(startDestination) as NavBackStack<T>
    val entryProvider = remember(builder) { NiddleNavGraphBuilder<T>().apply(builder).build() }

    NiddleNavHost(
        modifier = modifier,
        backStack = backStack,
        navigator = navigator,
        onBack = { onBack(backStack) },
        entryDecorators = entryDecorators,
        sceneStrategy = sceneStrategy,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        onNavigateCustom = onNavigateCustom,
        entryProvider = entryProvider
    )
}
