package io.github.loskovdm.timetracker.repository.model

import kotlin.time.Instant
import io.github.loskovdm.domain.model.Project as DomainProject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Project @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    val name: String,
    val color: Long,
    val isSynced: Boolean,
    val updatedAt: Instant,
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
fun DomainProject.toRepo(updatedAt: Instant) =
    Project(
        id = id,
        name = name,
        color = color,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )