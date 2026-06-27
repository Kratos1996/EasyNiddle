package dev.ishant.easyniddle.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent

object TransitionSpecConstants {

    fun <T: NavKey> defaultTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
        ContentTransform(
            fadeIn(animationSpec = tween(500)),
            fadeOut(animationSpec = tween(500)),
        )
    }

    fun <T: NavKey> defaultPopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
        ContentTransform(
            fadeIn(animationSpec = tween(500)),
            fadeOut(animationSpec = tween(500)),
        )
    }

    fun <T: NavKey> defaultPredictivePopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform = {
        ContentTransform(
            fadeIn(animationSpec = tween(500)),
            fadeOut(animationSpec = tween(500)),
        )
    }

    val None = EnterTransition.None togetherWith ExitTransition.None
}