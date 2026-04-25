package io.github.loskovdm.timetracker.repository.model

import kotlin.time.Instant
import io.github.loskovdm.domain.model.TimeEntryWithRelations as DomainTimeEntryWithRelations

data class TimeEntryWithRelations(
    val timeEntry: TimeEntry,
    val project: Project?,
    val task: Task?
)

fun TimeEntryWithRelations.toDomain() =
    DomainTimeEntryWithRelations(
        timeEntry = timeEntry.toDomain(),
        project = project?.toDomain(),
        task = task?.toDomain()
    )

fun DomainTimeEntryWithRelations.toRepo(updatedAt: Instant) =
    TimeEntryWithRelations(
        timeEntry = timeEntry.toRepo(updatedAt),
        project = project?.toRepo(updatedAt),
        task = task?.toRepo(updatedAt)
    )