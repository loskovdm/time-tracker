package io.github.loskovdm.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.ic_menu
import timetracker.designsystem.generated.resources.ic_menu_open

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavigationRail(
    modifier: Modifier = Modifier,
    state: WideNavigationRailState,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
    iconFloutingActionButton: DrawableResource? = null,
    labelFloutingActionButton: StringResource? = null,
    onClickFloutingActionButton: () -> Unit,
) {
    val showFloatingActionButton =
        (iconFloutingActionButton != null) &&
                (labelFloutingActionButton != null)

    val scope = rememberCoroutineScope()

    val expanded = state.targetValue == WideNavigationRailValue.Expanded

    WideNavigationRail(
        modifier = modifier,
        state = state,
        header = {
            if (
                deviceConfiguration != DeviceConfiguration.MOBILE_PORTRAIT
                    && deviceConfiguration != DeviceConfiguration.MOBILE_LANDSCAPE
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        modifier = Modifier.padding(start = 24.dp),
                        onClick = {
                            scope.launch {
                                if (expanded) {
                                    state.collapse()
                                } else {
                                    state.expand()
                                }
                            }
                        }
                    ) {
                        if (expanded) {
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
                    if (showFloatingActionButton) {
                        if (expanded) {
                            ExtendedFloatingActionButton(
                                modifier = Modifier.padding(start = 20.dp),
                                text = {
                                    Text(stringResource(labelFloutingActionButton))
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(iconFloutingActionButton),
                                        contentDescription = stringResource(labelFloutingActionButton)
                                    )
                                },
                                onClick = onClickFloutingActionButton,
                            )
                        } else {
                            FloatingActionButton(
                                modifier = Modifier.padding(start = 20.dp),
                                onClick = {
                                    // TODO:
                                }
                            ) {
                                Icon(
                                    painter = painterResource(iconFloutingActionButton),
                                    contentDescription = stringResource(labelFloutingActionButton)
                                )
                            }
                        }
                    }
                }
            }
        },
        arrangement =
            if (deviceConfiguration == DeviceConfiguration.DESKTOP)
                Arrangement.Top
            else Arrangement.Center,
    ) {
        navigationItems.forEach { (item, data) ->
            val selected = item == selectedNavigationItem

            WideNavigationRailItem(
                selected = selected,
                onClick = {
                    onSelectedNavigationItem(item)
                },
                icon = {
                    Icon(
                        painter = painterResource(data.icon(selected)),
                        contentDescription = stringResource(data.title)
                    )
                },
                label = {
                    Text(stringResource(data.title))
                },
                railExpanded = expanded,
            )
        }
    }
}