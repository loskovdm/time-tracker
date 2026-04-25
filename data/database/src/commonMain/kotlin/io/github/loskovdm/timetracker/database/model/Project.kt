package io.github.loskovdm.timetracker.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.Uuid
import io.github.loskovdm.timetracker.repository.model.Project as RepoProject

@Entity
data class Project(
    @PrimaryKey val id: Uuid,
    val name: String,
    val color: Long,
    val updatedAt: Instant,
    val isSynced: Boolean,
    val isArchived: Boolean,
    val isDelete: Boolean,
)

fun Project.toRepo() =
    RepoProject(
        id = id,
        name = name,
        color = color,
        isSynced = isSynced,
        updatedAt = updatedAt,
    )

fun RepoProject.toEntity(isArchived: Boolean = false, isDelete: Boolean = false) =
    Project(
        id = id,
        name = name,
        color = color,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isArchived = isArchived,
        isDelete = isDelete,
    )