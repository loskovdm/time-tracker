package io.github.loskovdm.timetracker.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.loskovdm.timetracker.repository.model.Project
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity
data class ProjectEntity (
    @PrimaryKey val id: Uuid,
    val name: String,
    val color: Long,
    val isSynced: Boolean,
    val updatedAt: Instant,
)

fun ProjectEntity.toRepo() =
    Project(
        id = id,
        name = name,
        color = color,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )

fun Project.toEntity() =
    ProjectEntity(
        id = id,
        name = name,
        color = color,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )