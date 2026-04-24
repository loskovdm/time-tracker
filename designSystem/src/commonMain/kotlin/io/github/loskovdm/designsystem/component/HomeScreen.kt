package io.github.loskovdm.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.navigation.NavigationItem
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    deviceConfiguration: DeviceConfiguration,
    topBar: @Composable () -> Unit,
    railState: WideNavigationRailState,
    lazyListState: LazyListState = rememberLazyListState(),
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
    iconFloutingActionButton: DrawableResource? = null,
    labelFloutingActionButton: StringResource? = null,
    onClickFloutingActionButton: () -> Unit,
    content: @Composable () -> Unit
) {
    val expandedFab by remember { derivedStateOf { !lazyListState.lastScrolledForward } }

    val showBottomBar = deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT
    val showNavigationRail = !showBottomBar
    val showFloatingActionButton =
        (iconFloutingActionButton != null) &&
                (labelFloutingActionButton != null)

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        if (showNavigationRail) {
            HomeNavigationRail(
                state = railState,
                isDesktop = deviceConfiguration == DeviceConfiguration.DESKTOP,
                navigationItems = navigationItems,
                selectedNavigationItem = selectedNavigationItem,
                onSelectedNavigationItem = onSelectedNavigationItem,
                iconFloutingActionButton = iconFloutingActionButton,
                labelFloutingActionButton = labelFloutingActionButton,
                onClickFloutingActionButton = onClickFloutingActionButton,
            )
        }
        Scaffold(
            modifier = Modifier
                .consumeWindowInsets(WindowInsets.safeDrawing.only(WindowInsetsSides.Start)),
            topBar = topBar,
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
                    showFloatingActionButton && deviceConfiguration != DeviceConfiguration.DESKTOP
                ) {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(stringResource(labelFloutingActionButton))
                        },
                        icon = {
                            Icon(
                                painter = painterResource(iconFloutingActionButton),
                                contentDescription = stringResource(labelFloutingActionButton)
                            )
                        },
                        expanded =
                            if (deviceConfiguration == DeviceConfiguration.TABLET_PORTRAIT ||
                                deviceConfiguration == DeviceConfiguration.TABLET_LANDSCAPE
                            ) {
                                true
                            } else {
                                expandedFab
                            },
                        onClick = onClickFloutingActionButton,
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(
                        end = if (deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE) {
                            8.dp
                        } else {
                            0.dp
                        }
                    )
                    .fillMaxSize()
                    .clip(
                        shape = if (showNavigationRail) {
                            when (deviceConfiguration) {
                                DeviceConfiguration.DESKTOP -> RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                                DeviceConfiguration.TABLET_PORTRAIT, DeviceConfiguration.TABLET_LANDSCAPE -> RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 0.dp
                                )
                                DeviceConfiguration.MOBILE_PORTRAIT -> RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                )
                                else -> RoundedCornerShape(16.dp)
                            }

                        } else {
                            RectangleShape
                        }
                    )
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                content()
            }
        }
    }
}