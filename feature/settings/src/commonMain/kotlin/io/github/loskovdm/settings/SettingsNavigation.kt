package io.github.loskovdm.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun SettingsNavigation(
    modifier: Modifier = Modifier,
    arrowBackIconResource: DrawableResource,
    onBack: () -> Unit,
) {
    SettingsScreen(
        modifier = modifier,
        arrowBackIconResource = arrowBackIconResource,
        onBack = { onBack() }
    )
}