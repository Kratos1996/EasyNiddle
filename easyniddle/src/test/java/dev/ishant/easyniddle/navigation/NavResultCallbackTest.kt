package dev.ishant.easyniddle.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class NavResultCallbackTest {

    @Test
    fun `setNavResultCallback should store callback in currentBackStackEntry savedStateHandle`() {
        val navController = mockk<NavController>()
        val backStackEntry = mockk<NavBackStackEntry>()
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        
        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.savedStateHandle } returns savedStateHandle
        
        val callback: NavResultCallback<String> = { }
        navController.setNavResultCallback(callback)
        
        verify { savedStateHandle.set("NavResultCallbackKey", callback) }
    }

    @Test
    fun `getNavResultCallback should remove and return callback from previousBackStackEntry`() {
        val navController = mockk<NavController>()
        val backStackEntry = mockk<NavBackStackEntry>()
        val savedStateHandle = mockk<SavedStateHandle>()
        
        val callback: NavResultCallback<String> = { }
        
        every { navController.previousBackStackEntry } returns backStackEntry
        every { backStackEntry.savedStateHandle } returns savedStateHandle
        every { savedStateHandle.remove<NavResultCallback<String>>("NavResultCallbackKey") } returns callback
        
        val result = navController.getNavResultCallback<String>()
        
        assertEquals(callback, result)
        verify { savedStateHandle.remove<NavResultCallback<String>>("NavResultCallbackKey") }
    }

    @Test
    fun `popBackStackWithResult should invoke callback and pop back stack`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val savedStateHandle = mockk<SavedStateHandle>()
        
        val result = "success"
        var callbackResult: String? = null
        val callback: NavResultCallback<String> = {
            callbackResult = it
        }
        
        every { navController.previousBackStackEntry } returns backStackEntry
        every { backStackEntry.savedStateHandle } returns savedStateHandle
        every { savedStateHandle.remove<NavResultCallback<String>>("NavResultCallbackKey") } returns callback
        
        navController.popBackStackWithResult(result)
        
        assertEquals(result, callbackResult)
        verify { navController.popBackStack() }
    }
}
