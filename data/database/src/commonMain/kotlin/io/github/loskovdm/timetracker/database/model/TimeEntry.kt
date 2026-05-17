package io.github.loskovdm.timetracker.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity
data class TimeEntry(
    @PrimaryKey val id: Uuid,
    val startDateTime: Instant,
    val endDateTime: Instant?,
    val projectId: Uuid?,
    val taskId: Uuid?,
    val isArchived: Boolean,
)