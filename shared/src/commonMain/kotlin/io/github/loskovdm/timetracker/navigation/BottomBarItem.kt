package io.github.loskovdm.timetracker.navigation

import androidx.navigation3.runtime.NavKey
import org.jetbrains.compose.resources.DrawableResource
import timetracker.shared.generated.resources.Res
import timetracker.shared.generated.resources.calendar
import timetracker.shared.generated.resources.projects
import timetracker.shared.generated.resources.reports
import timetracker.shared.generated.resources.timer

data class BottomBarItem(
    val iconResource: DrawableResource,
    val title: String,
)

val TOP_LEVEL_DESTINATIONS = mapOf<NavKey, BottomBarItem>(
    Route.Timer to BottomBarItem(
        iconResource = Res.drawable.timer,
        title = "Timer"
    ),
    Route.Calendar to BottomBarItem(
        iconResource = Res.drawable.calendar,
        title = "Calendar"
    ),
    Route.Projects to BottomBarItem(
        iconResource = Res.drawable.projects,
        title = "Projects"
    ),
    Route.Reports to BottomBarItem(
        iconResource = Res.drawable.reports,
        title = "Report"
    ),
)