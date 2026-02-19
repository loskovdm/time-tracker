package io.github.loskovdm.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.DeviceConfiguration
import io.github.loskovdm.designsystem.HomeBottomBar
import io.github.loskovdm.designsystem.HomeNavigationRail
import io.github.loskovdm.designsystem.NavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_entry
import timetracker.designsystem.generated.resources.calendar
import timetracker.designsystem.generated.resources.ic_add_entry
import timetracker.designsystem.generated.resources.ic_settings_outlined
import timetracker.designsystem.generated.resources.settings

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    railState: WideNavigationRailState,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
    onSettings: () -> Unit,
) {
    val showBottomBar = deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT
    val showNavigationRail = !showBottomBar

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        if (showNavigationRail) {
            HomeNavigationRail(
                state = railState,
                deviceConfiguration = deviceConfiguration,
                navigationItems = navigationItems,
                selectedNavigationItem = selectedNavigationItem,
                onSelectedNavigationItem = onSelectedNavigationItem,
                iconFloutingActionButton = Res.drawable.ic_add_entry,
                labelFloutingActionButton = Res.string.add_entry,
                onClickFloutingActionButton = {
                    // TODO:
                }
            )
        }
        Scaffold(
            modifier = Modifier
                .consumeWindowInsets(WindowInsets.safeDrawing.only(WindowInsetsSides.Start)),
            topBar = {
                CalendarTopBar(
                    onSettings = onSettings,
                )
            },
            bottomBar = {
                if (showBottomBar) {
                    HomeBottomBar(
                        navigationItems = navigationItems,
                        selectedNavigationItem = selectedNavigationItem,
                        onSelectedNavigationItem = { item ->
                            onSelectedNavigationItem(item)
                        },
                    )
                }
            },
            floatingActionButton = {
                if (
                    deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT
                        || deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE
                ) {
                    FloatingActionButton(
                        onClick = {
                            // TODO:
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add_entry),
                            contentDescription = stringResource(Res.string.add_entry)
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(stringResource(Res.string.calendar))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopBar(
    modifier: Modifier = Modifier,
    onSettings: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(Res.string.calendar))
        },
        actions = {
            IconButton(
                onClick = onSettings,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_settings_outlined),
                    contentDescription = stringResource(Res.string.settings)
                )
            }
        }
    )
}