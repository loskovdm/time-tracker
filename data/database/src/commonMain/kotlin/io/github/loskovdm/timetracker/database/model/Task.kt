package io.github.loskovdm.timetracker.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity
data class Task(
    @PrimaryKey val id: Uuid,
    val name: String,
    val projectId: Uuid,
    val isArchived: Boolean,
)