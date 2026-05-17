package io.github.loskovdm.timetracker.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class TimeEntryWithRelations(
    @Embedded
    val timeEntry: TimeEntry,

    @Relation(
        parentColumn = "projectId",
        entityColumn = "id"
    )
    val project: Project?,

    @Relation(
        parentColumn = "taskId",
        entityColumn = "id"
    )
    val task: Task?
)
