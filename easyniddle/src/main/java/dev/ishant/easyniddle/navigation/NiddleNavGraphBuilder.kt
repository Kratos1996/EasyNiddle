package dev.ishant.easyniddle.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import dev.ishant.easyniddle.util.NiddleLogger
import kotlin.reflect.KClass

/**
 * DSL Builder for Niddle Navigation Graph.
 */
class NiddleNavGraphBuilder<T : SerializableRoute> {
    @PublishedApi
    internal val entryMap = mutableMapOf<KClass<out T>, (T) -> NavEntry<out T>>()

    /**
     * Define a destination for a specific route type.
     */
    inline fun <reified R : T> screen(crossinline content: @Composable (R) -> Unit) {
        NiddleLogger.d("Registering screen for route: ${R::class.simpleName}")
        entryMap[R::class] = { route ->
            NavEntry(route) {
                content(route as R)
            }
        }
    }

    internal fun build(): (T) -> NavEntry<out T> = { route ->
        val provider = entryMap[route::class]
        if (provider == null) {
            error("No screen defined for route: ${route::class.qualifiedName}. Did you forget to add it to NiddleNavHost?")
        }
        provider(route)
    }
}


