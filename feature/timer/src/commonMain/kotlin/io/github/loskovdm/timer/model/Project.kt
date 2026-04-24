package io.github.loskovdm.timer.model

import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import io.github.loskovdm.domain.model.Project as DomainProject

data class Project @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    val name: String,
    val color: Long,
    val isSynced: Boolean,
)

@OptIn(ExperimentalUuidApi::class)
fun Project.toDomain() =
    DomainProject(
        id = id,
        name = name,
        color = color,
        isSynced = isSynced,
    )

@OptIn(ExperimentalUuidApi::class)
fun DomainProject.toView() =
    Project(
        id = id,
        name = name,
        color = color,
        isSynced = isSynced,
    )