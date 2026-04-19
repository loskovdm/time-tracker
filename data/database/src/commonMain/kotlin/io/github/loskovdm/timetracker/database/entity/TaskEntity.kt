package io.github.loskovdm.timetracker.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.loskovdm.timetracker.repository.model.Task
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity
data class TaskEntity(
    @PrimaryKey val id: Uuid,
    val name: String,
    val projectId: Uuid,
    val isSynced: Boolean,
    val updatedAt: Instant,
)

fun TaskEntity.toRepo() =
    Task(
        id = id,
        name = name,
        projectId = projectId,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )

fun Task.toEntity() =
    TaskEntity(
        id = id,
        name = name,
        projectId = projectId,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )