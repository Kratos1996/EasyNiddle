package dev.ishant.easyniddle.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NiddleNavGraphBuilderTest {

    interface TestRoute : SerializableRoute

    data object RouteA : TestRoute

    @Test
    fun `screen should add entry to entryMap`() {
        val builder = NiddleNavGraphBuilder<TestRoute>()
        builder.screen<RouteA> { /* content */ }

        assertNotNull(builder.entryMap[RouteA::class])
    }

    @Test
    fun `build should return a provider that returns NavEntry for registered routes`() {
        val builder = NiddleNavGraphBuilder<TestRoute>()
        builder.screen<RouteA> { /* content */ }

        val provider = builder.build()
        val entry = provider(RouteA)

        assertNotNull(entry)
        // Since key is private in NavEntry, we check contentKey which defaults to key.toString()
        assertEquals(RouteA.toString(), entry.contentKey)
    }

    @Test(expected = IllegalStateException::class)
    fun `build should throw exception for unregistered routes`() {
        val builder = NiddleNavGraphBuilder<TestRoute>()
        val provider = builder.build()
        provider(RouteA)
    }
}
