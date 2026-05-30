package io.github.loskovdm.timetracker.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class TimeEntryWithRelations(
    @Embedded
    val timeEntry: TimeEntry,

    @Relation(
        parentColumn = "project_id",
        entityColumn = "id",
    )
    val project: Project?,

    @Relation(
        parentColumn = "task_id",
        entityColumn = "id",
    )
    val task: Task?,
)
