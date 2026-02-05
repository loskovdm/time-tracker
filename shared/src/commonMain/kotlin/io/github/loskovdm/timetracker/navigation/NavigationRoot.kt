package io.github.loskovdm.timetracker.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.loskovdm.calendar.CalendarScreen
import io.github.loskovdm.projects.ProjectsScreen
import io.github.loskovdm.reports.ReportsScreen
import io.github.loskovdm.timer.TimerScreen
import io.github.loskovdm.timetracker.component.TimeTrackerBottomBar

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val navigationState = rememberNavigationState(
        startRoute = Route.Timer,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember {
        Navigator(navigationState)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            TimeTrackerBottomBar(
                selectedItem = navigationState.topLevelRoute,
                onSelectedItem = navigator::navigate,
            )
        },
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onBack = navigator::goBack,
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Timer> { TimerScreen() }
                    entry<Route.Calendar> { CalendarScreen() }
                    entry<Route.Reports> { ReportsScreen() }
                    entry<Route.Projects> { ProjectsScreen() }
                }
            )
        )
    }
}