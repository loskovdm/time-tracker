package io.github.loskovdm.timetracker.navigation

import androidx.navigation3.runtime.NavKey
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import timetracker.shared.generated.resources.Res
import timetracker.shared.generated.resources.calendar
import timetracker.shared.generated.resources.projects
import timetracker.shared.generated.resources.reports
import timetracker.shared.generated.resources.timer

data class BottomBarItem(
    val iconResource: DrawableResource,
    val titleResource: StringResource,
)

val TOP_LEVEL_DESTINATIONS = mapOf<NavKey, BottomBarItem>(
    Route.Home.Timer to BottomBarItem(
        iconResource = Res.drawable.timer,
        titleResource = Res.string.timer
    ),
    Route.Home.Calendar to BottomBarItem(
        iconResource = Res.drawable.calendar,
        titleResource = Res.string.calendar
    ),
    Route.Home.Projects to BottomBarItem(
        iconResource = Res.drawable.projects,
        titleResource = Res.string.projects
    ),
    Route.Home.Reports to BottomBarItem(
        iconResource = Res.drawable.reports,
        titleResource = Res.string.reports
    ),
)