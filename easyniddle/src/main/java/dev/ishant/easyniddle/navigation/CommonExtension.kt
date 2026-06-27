package dev.ishant.easyniddle.navigation

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner


fun Context.getActivityContext(): Context {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return this
}

fun Context.lifecycleOwner(): LifecycleOwner? {
    var currentContext = this
    var maxDepth = 20
    while (maxDepth-- > 0 && currentContext !is LifecycleOwner) {
        currentContext = (currentContext as ContextWrapper).baseContext
    }
    return currentContext as? LifecycleOwner
}
fun Context.findActivity(): AppCompatActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

fun Context.navigateBack() {
    val activity = findActivity()
    activity?.finish()
}


