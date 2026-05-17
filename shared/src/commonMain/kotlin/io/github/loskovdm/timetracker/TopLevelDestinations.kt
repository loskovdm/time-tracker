package io.github.loskovdm.timetracker
import io.github.loskovdm.timetracker.feature.navigation.impl.util.NavigationItem
import io.github.loskovdm.timetracker.feature.projects.api.destination.ProjectsListDestination
import io.github.loskovdm.timetracker.feature.reports.api.ReportsDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntriesListDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntryCalendarDestination
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

val TOP_LEVEL_DESTINATIONS = mapOf(
    TimeEntriesListDestination to NavigationItem(
        title = Res.string.timer,
        iconOutlined = Res.drawable.ic_timer_outlined,
        iconFilled = Res.drawable.ic_timer_filled
    ),
    TimeEntryCalendarDestination to NavigationItem(
        title = Res.string.calendar,
        iconOutlined = Res.drawable.ic_calendar_outlined,
        iconFilled = Res.drawable.ic_calendar_filled
    ),
    ProjectsListDestination to NavigationItem(
        title = Res.string.projects,
        iconOutlined = Res.drawable.ic_projects_outlined,
        iconFilled = Res.drawable.ic_projects_filled
    ),
    ReportsDestination to NavigationItem(
        title = Res.string.reports,
        iconOutlined = Res.drawable.ic_reports_outlined,
        iconFilled = Res.drawable.ic_reports_filled
    )
)