package io.github.loskovdm.timetracker.navigation

import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.loskovdm.calendar.CalendarNavigation
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.navigation.NavigationItem
import io.github.loskovdm.projects.navigation.ProjectsNavigation
import io.github.loskovdm.reports.ReportsNavigation
import io.github.loskovdm.timer.navigation.TimerNavigation

@Composable
fun HomeNavigation(
    modifier: Modifier = Modifier,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    onSettings: () -> Unit,
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.Home.Timer,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys // TODO: Исправить на navigationItems – если, конечно будет работать))
    )
    val navigator = remember {
        Navigator(navigationState)
    }

    val railState = rememberWideNavigationRailState()

    NavDisplay(
        modifier = modifier,
        onBack = navigator::goBack,
        entries = navigationState.toEntries(
            entryProvider {
                entry<Route.Home.Timer> {
                    TimerNavigation(
                        railState = railState,
                        navigationItems = navigationItems,
                        deviceConfiguration = deviceConfiguration,
                        selectedNavigationItem = Route.Home.Timer,
                        onSelectedNavigationItem = {
                            navigator.navigate(it)
                        },
                        onSettings = {
                            onSettings()
                        },
                    )
                }
                entry<Route.Home.Calendar> {
                    CalendarNavigation(
                        railState = railState,
                        navigationItems = navigationItems,
                        deviceConfiguration = deviceConfiguration,
                        selectedNavigationItem = Route.Home.Calendar,
                        onSelectedNavigationItem = {
                            navigator.navigate(it)
                        },
                        onSettings = {
                            onSettings()
                        },
                    )
                }
                entry<Route.Home.Projects> {
                    ProjectsNavigation(
                        railState = railState,
                        navigationItems = navigationItems,
                        deviceConfiguration = deviceConfiguration,
                        selectedNavigationItem = Route.Home.Projects,
                        onSelectedNavigationItem = {
                            navigator.navigate(it)
                        },
                        onSettings = {
                            onSettings()
                        },
                    )
                }
                entry<Route.Home.Reports> {
                    ReportsNavigation(
                        railState = railState,
                        navigationItems = navigationItems,
                        deviceConfiguration = deviceConfiguration,
                        selectedNavigationItem = Route.Home.Reports,
                        onSelectedNavigationItem = {
                            navigator.navigate(it)
                        },
                        onSettings = {
                            onSettings()
                        },
                    )
                }
            }
        )
    )
}