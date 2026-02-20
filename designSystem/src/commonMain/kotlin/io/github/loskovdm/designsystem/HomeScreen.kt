package io.github.loskovdm.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
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
    content: @Composable (PaddingValues) -> Unit
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
                            expanded = expandedFab,
                            onClick = onClickFloutingActionButton,
                        )
                }
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}