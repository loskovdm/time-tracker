package io.github.loskovdm.timetracker.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.loskovdm.timetracker.repository.model.TimeEntry
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity
data class TimeEntryEntity(
    @PrimaryKey val id: Uuid,
    val startDateTime: Instant,
    val endDateTime: Instant?,
    val projectId: Uuid?,
    val taskId: Uuid?,
    val isSynced: Boolean,
    val updatedAt: Instant,
)

fun TimeEntryEntity.toRepo() =
    TimeEntry(
        id = id,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        projectId = projectId,
        taskId = taskId,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )

fun TimeEntry.toEntity() =
    TimeEntryEntity(
        id = id,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        projectId = projectId,
        taskId = taskId,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )