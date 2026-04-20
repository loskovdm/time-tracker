package io.github.loskovdm.timetracker.repository.model

import kotlin.time.Instant
import io.github.loskovdm.domain.model.Task as DomainTask
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Task @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    val name: String,
    val projectId: Uuid,
    val isSynced: Boolean,
    val updatedAt: Instant,
)

@OptIn(ExperimentalUuidApi::class)
fun Task.toDomain() =
    DomainTask(
        id = id,
        name = name,
        projectId = projectId,
        isSynced = isSynced,
    )

@OptIn(ExperimentalUuidApi::class)
fun DomainTask.toRepo(updatedAt: Instant) =
    Task(
        id = id,
        name = name,
        projectId = projectId,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )