package dev.ishant.easyniddle.navigation

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CommonExtensionTest {

    class TestActivity : AppCompatActivity()

    @Test
    fun `findActivity should return activity when context is an activity`() {
        // Use a theme to avoid Resources$NotFoundException with AppCompatActivity
        val activity = Robolectric.buildActivity(TestActivity::class.java).create().get()
        assertEquals(activity, activity.findActivity())
    }

    @Test
    fun `findActivity should return activity when context is a ContextWrapper`() {
        val activity = Robolectric.buildActivity(TestActivity::class.java).create().get()
        val wrapper = ContextWrapper(activity)
        assertEquals(activity, wrapper.findActivity())
    }

    @Test
    fun `findActivity should return null when context is not an activity`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertNull(context.findActivity())
    }
}
