package io.github.loskovdm.designsystem.system

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun ApplySystemNavigationBarColor(
    color: Color,
    darkIcons: Boolean,
) {
    val view = LocalView.current
    if (view.isInEditMode) return

    SideEffect {
        val window = view.context.findActivity().window
        window.navigationBarColor = color.toArgb()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = darkIcons
    }
}

private tailrec fun Context.findActivity(): Activity {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> error("Activity not found")
    }
}
