package dev.ishant.easyniddlenavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.ishant.easyniddle.navigation.*
import dev.ishant.easyniddlenavigation.ui.theme.EasyNiddleNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyNiddleNavigationTheme {
                // Observe results globally
                val result by navigationResult.collectAsState(initial = null)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Use the minimal NiddleNavHost with DSL
                    NiddleNavHost<AppRoute>(
                        startDestination = AppRoute.Home,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Basic Screen (Home)
                        screen<AppRoute.Home> {
                            HomeScreen(result = result?.toString())
                        }

                        // 2. Screen with Data (Details) - Automatically receives the 'route' object
                        screen<AppRoute.Details> { route ->
                            DetailsScreen(id = route.itemId, name = route.itemName)
                        }

                        // 3. Screen with context data (Settings)
                        screen<AppRoute.Settings> { route ->
                            SettingsScreen(from = route.fromScreen)
                        }

                        // 4. Simple Profile Screen
                        screen<AppRoute.Profile> {
                            ProfileScreen()
                        }

                        // 5. Result Source Screen
                        screen<AppRoute.ResultScreen> {
                            ResultSourceScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(result: String?) {
    val nav = LocalNiddleNavigator.current
    Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
        
        if (result != null) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.Yellow)) {
                Text("Last Result: $result", Modifier.padding(8.dp))
            }
        }

        Button(onClick = { 
            // PASSING DATA: Simple data class route
            nav.navigate(AppRoute.Details(itemId = 101, itemName = "EasyNiddle Library")) 
        }) {
            Text("Navigate with Data (Details)")
        }

        Button(onClick = { nav.navigate(AppRoute.ResultScreen) }) {
            Text("Go to Result Screen")
        }

        Button(onClick = { nav.navigate(AppRoute.Settings("Home Screen")) }) {
            Text("Go to Settings")
        }
    }
}

@Composable
fun DetailsScreen(id: Int, name: String) {
    val nav = LocalNiddleNavigator.current
    Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Details Screen", style = MaterialTheme.typography.headlineMedium)
        Text("Item ID: $id")
        Text("Item Name: $name")

        Button(onClick = { 
            // POP UP TO: Navigate and clear the stack up to Home (inclusive)
            nav.navigate(AppRoute.Profile, popUpTo = AppRoute.Home, inclusive = true) 
        }) {
            Text("Navigate to Profile & Clear Home")
        }

        Button(onClick = { nav.pop() }) {
            Text("Back")
        }
    }
}

@Composable
fun SettingsScreen(from: String) {
    val nav = LocalNiddleNavigator.current
    Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Settings Screen", style = MaterialTheme.typography.headlineMedium)
        Text("Came from: $from")

        Button(onClick = {
            // SINGLE TOP: Return to an existing instance of Home instead of creating a new one
            nav.navigate(Navigator.NavigateToBackstackScreenAsSingleTop(AppRoute.Home))
        }) {
            Text("Go Home (Single Top)")
        }

        Button(onClick = { 
            // POP TO: Clear everything back to Home
            nav.popTo(AppRoute.Home) 
        }) {
            Text("Pop Back to Home")
        }
    }
}

@Composable
fun ProfileScreen() {
    val nav = LocalNiddleNavigator.current
    Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Profile Screen", style = MaterialTheme.typography.headlineMedium)
        Text("This screen replaced Home in the backstack.")

        Button(onClick = { 
            // FINISH: Close the current activity
            nav.finish() 
        }) {
            Text("Finish Activity")
        }

        Button(onClick = { nav.navigate(AppRoute.Home) }) {
            Text("Re-enter Home")
        }
    }
}

@Composable
fun ResultSourceScreen() {
    val nav = LocalNiddleNavigator.current
    Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Result Source Screen", style = MaterialTheme.typography.headlineMedium)
        
        Button(onClick = { 
            // RESULT: Pass data back to the previous observer
            nav.navigateBackWithResult("Success from ResultScreen!") 
        }) {
            Text("Finish with Result")
        }

        Button(onClick = { nav.pop() }) {
            Text("Cancel")
        }
    }
}
