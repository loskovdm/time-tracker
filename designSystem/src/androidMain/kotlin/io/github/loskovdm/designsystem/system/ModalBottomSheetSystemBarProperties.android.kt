package io.github.loskovdm.designsystem.system

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.luminance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun rememberNavigationChromeModalBottomSheetProperties(): ModalBottomSheetProperties {
    val color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainer
    val darkIcons = color.luminance() > 0.5f
    return remember(darkIcons) {
        ModalBottomSheetProperties(
            isAppearanceLightStatusBars = darkIcons,
            isAppearanceLightNavigationBars = darkIcons,
        )
    }
}
