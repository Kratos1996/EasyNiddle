package dev.ishant.easyniddlenavigation

import dev.ishant.easyniddle.navigation.SerializableRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute : SerializableRoute {
    @Serializable
    data object Home : AppRoute()
    
    @Serializable
    data class Details(val itemId: Int, val itemName: String) : AppRoute()
    
    @Serializable
    data object Profile : AppRoute()
    
    @Serializable
    data class Settings(val fromScreen: String) : AppRoute()
    
    @Serializable
    data object ResultScreen : AppRoute()
}
