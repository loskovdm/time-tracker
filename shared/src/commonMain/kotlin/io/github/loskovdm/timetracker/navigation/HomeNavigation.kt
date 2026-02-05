package io.github.loskovdm.timetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.loskovdm.calendar.CalendarNavigation
import io.github.loskovdm.projects.navigation.ProjectsNavigation
import io.github.loskovdm.reports.ReportsNavigation
import io.github.loskovdm.timer.TimerNavigation
import io.github.loskovdm.timetracker.TimeTrackerBottomBar
import timetracker.shared.generated.resources.Res
import timetracker.shared.generated.resources.arrow_back
import timetracker.shared.generated.resources.back
import timetracker.shared.generated.resources.calendar
import timetracker.shared.generated.resources.projects
import timetracker.shared.generated.resources.reports
import timetracker.shared.generated.resources.settings
import timetracker.shared.generated.resources.timer

@Composable
fun HomeNavigation(
    modifier: Modifier = Modifier,
    onSettings: () -> Unit,
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.Home.Timer,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember {
        Navigator(navigationState)
    }

    NavDisplay(
        modifier = modifier,
        onBack = navigator::goBack,
        entries = navigationState.toEntries(
            entryProvider {
                entry<Route.Home.Timer> {
                    TimerNavigation(
                        appBottomBar = {
                            TimeTrackerBottomBar(
                                selectedItem = Route.Home.Timer,
                                onSelectedItem = {
                                    navigator.navigate(it)
                                }
                            )
                        },
                        settingsIconResource = Res.drawable.settings,
                        titleResource = Res.string.timer,
                        onSettings = {
                            onSettings()
                        },
                    )
                }
                entry<Route.Home.Calendar> {
                    CalendarNavigation(
                        appBottomBar = {
                            TimeTrackerBottomBar(
                                selectedItem = Route.Home.Calendar,
                                onSelectedItem = {
                                    navigator.navigate(it)
                                }
                            )
                        },
                        titleResource = Res.string.calendar,
                        settingsIconResource = Res.drawable.settings,
                        onSettings = {
                            onSettings()
                        },
                    )
                }
                entry<Route.Home.Projects> {
                    ProjectsNavigation(
                        appBottomBar = {
                            TimeTrackerBottomBar(
                                selectedItem = Route.Home.Projects,
                                onSelectedItem = {
                                    navigator.navigate(it)
                                }
                            )
                        },
                        titleResource = Res.string.projects,
                        settingsIconResource = Res.drawable.settings,
                        backIconDescriptionResource = Res.string.back,
                        backIconResource = Res.drawable.arrow_back,
                        onSettings = {
                            onSettings()
                        },
                    )
                }
                entry<Route.Home.Reports> {
                    ReportsNavigation(
                        appBottomBar = {
                            TimeTrackerBottomBar(
                                selectedItem = Route.Home.Reports,
                                onSelectedItem = {
                                    navigator.navigate(it)
                                }
                            )
                        },
                        settingsIconResource = Res.drawable.settings,
                        titleResource = Res.string.reports,
                        onSettings = {
                            onSettings()
                        },
                    )
                }
            }
        )
    )
}