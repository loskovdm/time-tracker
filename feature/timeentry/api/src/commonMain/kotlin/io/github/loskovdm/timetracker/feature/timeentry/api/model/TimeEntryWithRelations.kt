package io.github.loskovdm.timetracker.feature.timeentry.api.model

import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task

data class TimeEntryWithRelations(
    val timeEntry: TimeEntry,
    val project: Project?,
    val task: Task?
)