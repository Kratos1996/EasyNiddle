package dev.ishant.easyniddle.di

import dev.ishant.easyniddle.navigation.NiddleNavigator

/*
 * This module is provided as a template for Hilt users.
 * Since this is a library, we don't include Hilt as a hard dependency,
 * but users can copy this or we can provide it if they have hilt in classpath.
 */

/**
 * A suggested Hilt module for providing [NiddleNavigator].
 * 
 * To use this, you would typically include it in your app module:
 * 
 * @Module
 * @InstallIn(SingletonComponent::class)
 * object NavigationModule {
 *     @Provides
 *     @Singleton
 *     fun provideNiddleNavigator(): NiddleNavigator = NiddleNavigator()
 * }
 */
object NiddleNavigationModule {
    fun provideNavigator(): NiddleNavigator = NiddleNavigator()
}
