package io.github.loskovdm.domain.model

data class TimeEntryWithRelations(
    val timeEntry: TimeEntry,
    val project: Project?,
    val task: Task?
)
