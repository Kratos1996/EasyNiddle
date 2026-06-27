package dev.ishant.easyniddle.navigation

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import kotlinx.serialization.Serializable
import org.junit.Rule
import org.junit.Test

class NiddleNavHostTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    interface TestRoute : SerializableRoute

    @Serializable
    data object ScreenA : TestRoute
    
    @Serializable
    data object ScreenB : TestRoute

    @Test
    fun niddleNavHost_displaysStartDestination() {
        composeTestRule.setContent {
            NiddleNavHost<TestRoute>(startDestination = ScreenA) {
                screen<ScreenA> { Text("Screen A") }
                screen<ScreenB> { Text("Screen B") }
            }
        }

        composeTestRule.onNodeWithText("Screen A").assertExists()
    }

    @Test
    fun niddleNavHost_navigatesToNewDestination() {
        val navigator = NiddleNavigator()
        composeTestRule.setContent {
            NiddleNavHost<TestRoute>(
                startDestination = ScreenA,
                navigator = navigator
            ) {
                screen<ScreenA> { Text("Screen A") }
                screen<ScreenB> { Text("Screen B") }
            }
        }

        composeTestRule.onNodeWithText("Screen A").assertExists()
        
        navigator.navigate(ScreenB)
        
        composeTestRule.onNodeWithText("Screen B").assertExists()
    }
}
