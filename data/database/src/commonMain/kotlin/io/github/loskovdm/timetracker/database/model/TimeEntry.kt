package io.github.loskovdm.timetracker.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.loskovdm.timetracker.repository.model.TimeEntry as RepoTimeEntry
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity
data class TimeEntry(
    @PrimaryKey val id: Uuid,
    val startDateTime: Instant,
    val endDateTime: Instant?,
    val projectId: Uuid?,
    val taskId: Uuid?,
    val updatedAt: Instant,
    val isSynced: Boolean,
    val isArchived: Boolean,
    val isDelete: Boolean,
)

fun TimeEntry.toRepo() =
    RepoTimeEntry(
        id = id,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        projectId = projectId,
        taskId = taskId,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )

fun RepoTimeEntry.toEntity(isArchived: Boolean = false, isDelete: Boolean = false) =
    TimeEntry(
        id = id,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        projectId = projectId,
        taskId = taskId,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isArchived = isArchived,
        isDelete = isDelete,
    )