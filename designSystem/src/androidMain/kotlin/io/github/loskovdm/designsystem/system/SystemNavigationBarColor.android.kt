package io.github.loskovdm.designsystem.system

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat

@Composable
actual fun ApplySystemNavigationBarColor(
    color: Color,
    darkIcons: Boolean,
) {
    val view = LocalView.current
    if (view.isInEditMode) return

    DisposableEffect(view, color, darkIcons) {
        val window = view.findHostWindow()
        val decorView = window.decorView

        fun apply() {
            applyNavigationBarColor(window, color, darkIcons)
        }

        val listener = ViewTreeObserver.OnPreDrawListener {
            apply()
            true
        }

        decorView.viewTreeObserver.addOnPreDrawListener(listener)
        apply()

        onDispose {
            decorView.viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }
}

private fun applyNavigationBarColor(
    window: Window,
    color: Color,
    darkIcons: Boolean,
) {
    window.navigationBarColor = color.toArgb()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
    }
    WindowCompat.getInsetsController(window, window.decorView)
        .isAppearanceLightNavigationBars = darkIcons
}

private fun View.findHostWindow(): Window {
    var current: Any? = this
    while (current != null) {
        when (current) {
            is DialogWindowProvider -> return current.window
            is View -> current = current.parent
            else -> break
        }
    }
    return context.findActivity().window
}

private tailrec fun Context.findActivity(): Activity {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> error("Activity not found")
    }
}
