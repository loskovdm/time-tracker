package io.github.loskovdm.timetracker.feature.navigation.impl.component.navbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailColors
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import io.github.loskovdm.timetracker.feature.navigation.impl.util.NavigationItem
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.ic_menu
import timetracker.designsystem.generated.resources.ic_menu_open

/** Matches Material3 extended FAB height used in rail header actions. */
private val RailFabSlotHeight = 56.dp

@Composable
fun NavigationRail(
    state: WideNavigationRailState,
    extendedFab: @Composable (isExpanded: Boolean) -> Unit,
    destinations: Map<TimeTrackerDestination, NavigationItem>,
    selectedDestination: TimeTrackerDestination,
    onSelectedDestination: (TimeTrackerDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val deviceConfiguration = LocalDeviceConfiguration.current

    val isExpanded = state.targetValue == WideNavigationRailValue.Expanded
    val isDesktop = deviceConfiguration == DeviceConfiguration.DESKTOP

    WideNavigationRail(
        modifier = modifier,
        state = state,
        header = {
            if (isDesktop) {
                DesktopNavRailHeader(
                    state = state,
                    isExpanded = isExpanded,
                    extendedFab = extendedFab,
                )
            }
        },
        arrangement = if (isDesktop) {
            Arrangement.Top
        } else {
            Arrangement.Center
        },
        colors = WideNavigationRailColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modalContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            modalScrimColor = MaterialTheme.colorScheme.surfaceContainer,
            modalContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
//        colors = WideNavigationRailDefaults.colors(
//
//        )
    ) {
        destinations.forEach { (destination, data) ->
            WideNavigationRailItem(
                selected = destination == selectedDestination,
                onClick = { onSelectedDestination(destination) },
                icon = {
                    Icon(
                        imageVector = vectorResource(data.icon(destination == selectedDestination)),
                        contentDescription = stringResource(data.title)
                    )
                },
                label = {
                    Text(stringResource(data.title))
                },
                railExpanded = isExpanded
            )
        }
    }
}

@Composable
fun DesktopNavRailHeader(
    state: WideNavigationRailState,
    isExpanded: Boolean,
    extendedFab: @Composable (isExpanded: Boolean) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RailMenu(
            state = state,
            isExpanded = isExpanded
        )
        RailFab(
            isExpanded = isExpanded,
            extendedFab = extendedFab,
        )
    }
}

@Composable
fun RailMenu(
    modifier: Modifier = Modifier,
    state: WideNavigationRailState,
    isExpanded: Boolean,
) {
    val scope = rememberCoroutineScope()

    IconButton(
        modifier = modifier.padding(start = 24.dp),
        onClick = {
            scope.launch {
                if (isExpanded) {
                    state.collapse()
                } else {
                    state.expand()
                }
            }
        }
    ) {
        if (isExpanded) {
            Icon(
                painter = painterResource(Res.drawable.ic_menu_open),
                contentDescription = "Collapse rail",
            )
        } else {
            Icon(
                painter = painterResource(Res.drawable.ic_menu),
                contentDescription = "Expand rail",
            )
        }
    }
}

@Composable
fun RailFab(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    extendedFab: @Composable (isExpanded: Boolean) -> Unit,
) {
    // Reserve FAB height so nav items stay aligned when a screen has no rail FAB (e.g. Reports).
    Box(
        modifier = modifier
            .padding(20.dp)
            .heightIn(min = RailFabSlotHeight),
        contentAlignment = Alignment.CenterStart,
    ) {
        if (isExpanded) {
            extendedFab(true)
        } else {
            extendedFab(false)
        }
    }
}