package io.github.loskovdm.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.component.HomeScreen
import io.github.loskovdm.designsystem.navigation.NavigationItem
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.ic_settings_outlined
import timetracker.designsystem.generated.resources.ic_share_filled
import timetracker.designsystem.generated.resources.reports
import timetracker.designsystem.generated.resources.settings
import timetracker.designsystem.generated.resources.share

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    modifier: Modifier = Modifier,
    railState: WideNavigationRailState,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
    onSettings: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    HomeScreen(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        deviceConfiguration = deviceConfiguration,
        topBar = {
            ReportsTopBar(
                deviceConfiguration = deviceConfiguration,
                scrollBehavior = scrollBehavior,
                onSettings = onSettings,
            )
        },
        railState = railState,
        navigationItems = navigationItems,
        selectedNavigationItem = selectedNavigationItem,
        onSelectedNavigationItem = onSelectedNavigationItem,
        iconFloutingActionButton = Res.drawable.ic_share_filled,
        labelFloutingActionButton = Res.string.share,
        onClickFloutingActionButton = {
            // TODO:
        },
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(stringResource(Res.string.reports))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsTopBar(
    modifier: Modifier = Modifier,
    deviceConfiguration: DeviceConfiguration,
    scrollBehavior: TopAppBarScrollBehavior,
    onSettings: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(Res.string.reports))
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
        },
        colors = TopAppBarColors(
            containerColor = if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            subtitleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        scrollBehavior = scrollBehavior,
    )
}