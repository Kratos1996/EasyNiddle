package dev.ishant.easyniddle.navigation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NiddleNavigatorTest {

    interface TestRoute : SerializableRoute
    data object RouteA : TestRoute

    @Test
    fun `navigate should emit NavigateRoute event`() = runTest {
        val navigator = NiddleNavigator()
        val results = mutableListOf<Navigator>()
        val job = launch(UnconfinedTestDispatcher()) {
            navigator.navEvents.collect { results.add(it) }
        }

        navigator.navigate(RouteA)

        assertEquals(1, results.size)
        assertTrue(results[0] is Navigator.NavigateRoute)
        assertEquals(RouteA, (results[0] as Navigator.NavigateRoute).route)
        job.cancel()
    }

    @Test
    fun `pop should emit NavigateBack event`() = runTest {
        val navigator = NiddleNavigator()
        val results = mutableListOf<Navigator>()
        val job = launch(UnconfinedTestDispatcher()) {
            navigator.navEvents.collect { results.add(it) }
        }

        navigator.pop()

        assertEquals(1, results.size)
        assertEquals(Navigator.NavigateBack, results[0])
        job.cancel()
    }

    @Test
    fun `popTo should emit NavigatePopBackStack event`() = runTest {
        val navigator = NiddleNavigator()
        val results = mutableListOf<Navigator>()
        val job = launch(UnconfinedTestDispatcher()) {
            navigator.navEvents.collect { results.add(it) }
        }

        navigator.popTo(RouteA, inclusive = true)

        assertEquals(1, results.size)
        assertTrue(results[0] is Navigator.NavigatePopBackStack)
        val popEvent = results[0] as Navigator.NavigatePopBackStack
        assertEquals(RouteA, popEvent.route)
        assertTrue(popEvent.inclusive)
        job.cancel()
    }

    @Test
    fun `finish should emit FinishActivity event`() = runTest {
        val navigator = NiddleNavigator()
        val results = mutableListOf<Navigator>()
        val job = launch(UnconfinedTestDispatcher()) {
            navigator.navEvents.collect { results.add(it) }
        }

        navigator.finish()

        assertEquals(1, results.size)
        assertEquals(Navigator.FinishActivity, results[0])
        job.cancel()
    }

    @Test
    fun `navigateBackWithResult should emit NavigateBackWithResult event`() = runTest {
        val navigator = NiddleNavigator()
        val results = mutableListOf<Navigator>()
        val job = launch(UnconfinedTestDispatcher()) {
            navigator.navEvents.collect { results.add(it) }
        }

        val result = "success"
        navigator.navigateBackWithResult(result)

        assertEquals(1, results.size)
        assertTrue(results[0] is Navigator.NavigateBackWithResult)
        assertEquals(result, (results[0] as Navigator.NavigateBackWithResult).result)
        job.cancel()
    }
}
