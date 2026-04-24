package io.github.loskovdm.timetracker.navigation

import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.navigation.NavigationItem
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.calendar
import timetracker.designsystem.generated.resources.ic_calendar_filled
import timetracker.designsystem.generated.resources.ic_calendar_outlined
import timetracker.designsystem.generated.resources.ic_projects_filled
import timetracker.designsystem.generated.resources.ic_projects_outlined
import timetracker.designsystem.generated.resources.ic_reports_filled
import timetracker.designsystem.generated.resources.ic_reports_outlined
import timetracker.designsystem.generated.resources.ic_timer_filled
import timetracker.designsystem.generated.resources.ic_timer_outlined
import timetracker.designsystem.generated.resources.projects
import timetracker.designsystem.generated.resources.reports
import timetracker.designsystem.generated.resources.timer

val TOP_LEVEL_DESTINATIONS = mapOf<NavKey, NavigationItem>(
    Route.Home.Timer to NavigationItem(
        title = Res.string.timer,
        iconOutlined = Res.drawable.ic_timer_outlined,
        iconFilled = Res.drawable.ic_timer_filled
    ),
    Route.Home.Calendar to NavigationItem(
        title = Res.string.calendar,
        iconOutlined = Res.drawable.ic_calendar_outlined,
        iconFilled = Res.drawable.ic_calendar_filled
    ),
    Route.Home.Projects to NavigationItem(
        title = Res.string.projects,
        iconOutlined = Res.drawable.ic_projects_outlined,
        iconFilled = Res.drawable.ic_projects_filled
    ),
    Route.Home.Reports to NavigationItem(
        title = Res.string.reports,
        iconOutlined = Res.drawable.ic_reports_outlined,
        iconFilled = Res.drawable.ic_reports_filled
    ),
)