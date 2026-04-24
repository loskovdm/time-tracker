package io.github.loskovdm.timer.model

import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import io.github.loskovdm.domain.model.Task as DomainTask

data class Task @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    val name: String,
    val project: Project,
    val isSynced: Boolean,
)

@OptIn(ExperimentalUuidApi::class)
fun Task.toDomain() =
    DomainTask(
        id = id,
        name = name,
        projectId = project.id,
        isSynced = isSynced,
    )

@OptIn(ExperimentalUuidApi::class)
fun DomainTask.toView(project: Project) =
    Task(
        id = id,
        name = name,
        project = project,
        isSynced = isSynced,
    )