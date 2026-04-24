package io.github.loskovdm.timetracker

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.navigation.RootNavigation
import io.github.loskovdm.timetracker.util.isDesktopPlatform

@Composable
fun TimeTrackerApp() {
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(
        currentWindowAdaptiveInfo().windowSizeClass
    )

    MaterialTheme {
        RootNavigation(
            deviceConfiguration =
                if (isDesktopPlatform())
                    DeviceConfiguration.DESKTOP
                else deviceConfiguration,
        )
    }
}