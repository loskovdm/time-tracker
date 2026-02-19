package io.github.loskovdm.timetracker

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import io.github.loskovdm.designsystem.DeviceConfiguration
import io.github.loskovdm.timetracker.navigation.RootNavigation

@Composable
@Preview
fun TimeTrackerApp() {
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(
        currentWindowAdaptiveInfo().windowSizeClass
    )

    MaterialTheme {
        RootNavigation(
            deviceConfiguration = deviceConfiguration,
        )
    }
}