package io.github.loskovdm.timer.navigation

import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.navigation.NavigationItem
import io.github.loskovdm.timer.TimerScreen

@Composable
fun TimerNavigation(
    modifier: Modifier = Modifier,
    railState: WideNavigationRailState,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
    onSettings: () -> Unit,
) {
    TimerScreen(
        modifier = modifier,
        railState = railState,
        deviceConfiguration = deviceConfiguration,
        navigationItems = navigationItems,
        selectedNavigationItem = selectedNavigationItem,
        onSelectedNavigationItem = { item ->
            onSelectedNavigationItem(item)
        },
        onSettings = {
            onSettings()
        },
    )
}