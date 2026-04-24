package io.github.loskovdm.timer.model

import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import io.github.loskovdm.domain.model.TimeEntry as DomainTimeEntry

@OptIn(ExperimentalUuidApi::class)
data class TimeEntry(
    val id: Uuid,
    val startDateTime: Instant,
    val endDateTime: Instant?,
    val project: Project?,
    val task: Task?,
    val isSynced: Boolean,
)

@OptIn(ExperimentalUuidApi::class)
fun TimeEntry.toDomain() =
    DomainTimeEntry(
        id = id,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        projectId = project?.id,
        taskId = task?.id,
        isSynced = isSynced,
    )

@OptIn(ExperimentalUuidApi::class)
fun DomainTimeEntry.toView(project: Project?, task: Task?) =
    TimeEntry(
        id = id,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        project = project,
        task = task,
        isSynced = isSynced,
    )