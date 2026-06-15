package io.github.loskovdm.designsystem.system

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

@Composable
expect fun ApplySystemNavigationBarColor(
    color: Color,
    darkIcons: Boolean,
)

@Composable
fun ApplyNavigationChromeSystemBarColor() {
    val color = MaterialTheme.colorScheme.surfaceContainer
    ApplySystemNavigationBarColor(
        color = color,
        darkIcons = color.luminance() > 0.5f,
    )
}
