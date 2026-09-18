package dev.ishant.easyniddle.util

import android.util.Log
import dev.ishant.easyniddle.BuildConfig

/**
 * Centralized logger for EasyNiddle navigation.
 * By default, it only logs in Debug builds.
 */
object NiddleLogger {
    private const val TAG = "NiddleNavigation"
    
    /**
     * Set this to true if you want to see logs in Release builds as well.
     */
    var showLogsInRelease: Boolean = false

    fun d(message: String) {
        if (isDebug() || showLogsInRelease) {
            Log.d(TAG, message)
        }
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (isDebug() || showLogsInRelease) {
            Log.e(TAG, message, throwable)
        }
    }

    fun i(message: String) {
        if (isDebug() || showLogsInRelease) {
            Log.i(TAG, message)
        }
    }

    private fun isDebug(): Boolean {
        return try {
            BuildConfig.DEBUG
        } catch (_: Throwable) {
            false
        }
    }
}
