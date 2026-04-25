package io.github.loskovdm.timetracker.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.loskovdm.timetracker.repository.model.Task as RepoTask
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity
data class Task(
    @PrimaryKey val id: Uuid,
    val name: String,
    val projectId: Uuid,
    val updatedAt: Instant,
    val isSynced: Boolean,
    val isArchived: Boolean,
    val isDelete: Boolean,
)

fun Task.toRepo() =
    RepoTask(
        id = id,
        name = name,
        projectId = projectId,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )

fun RepoTask.toEntity(isArchived: Boolean = false, isDelete: Boolean = false) =
    Task(
        id = id,
        name = name,
        projectId = projectId,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isArchived = isArchived,
        isDelete = isDelete,
    )