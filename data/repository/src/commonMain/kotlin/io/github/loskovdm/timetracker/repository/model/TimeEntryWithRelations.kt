package io.github.loskovdm.timetracker.repository.model

data class TimeEntryWithRelations(
    val timeEntry: TimeEntry,
    val project: Project?,
    val task: Task?
)