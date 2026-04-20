package io.github.loskovdm.timetracker.repository.model

import io.github.loskovdm.domain.model.TimeEntry as DomainTimeEntry
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TimeEntry(
    val id: Uuid,
    val startDateTime: Instant,
    val endDateTime: Instant?,
    val projectId: Uuid?,
    val taskId: Uuid?,
    val isSynced: Boolean,
    val updatedAt: Instant,
)

@OptIn(ExperimentalUuidApi::class)
fun TimeEntry.toDomain() =
    DomainTimeEntry(
        id = id,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        projectId = projectId,
        taskId = taskId,
        isSynced = isSynced,
    )

@OptIn(ExperimentalUuidApi::class)
fun DomainTimeEntry.toRepo(updatedAt: Instant) =
    TimeEntry(
        id = id,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        projectId = projectId,
        taskId = taskId,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )