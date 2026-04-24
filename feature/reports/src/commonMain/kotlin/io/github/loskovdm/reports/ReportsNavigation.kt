package io.github.loskovdm.reports

import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.navigation.NavigationItem

@Composable
fun ReportsNavigation(
    modifier: Modifier = Modifier,
    railState: WideNavigationRailState,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
    onSettings: () -> Unit,
) {
    ReportsScreen(
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