package io.github.loskovdm.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.loskovdm.designsystem.DeviceConfiguration

@Composable
fun SettingsNavigation(
    modifier: Modifier = Modifier,
    deviceConfiguration: DeviceConfiguration,
    onBack: () -> Unit,
) {
    SettingsScreen(
        modifier = modifier,
        onBack = { onBack() }
    )
}