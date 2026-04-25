package io.github.loskovdm.timetracker.database.model

import androidx.room.Embedded
import androidx.room.Relation
import io.github.loskovdm.timetracker.repository.model.TimeEntryWithRelations as RepoTimeEntryWithRelations

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

fun TimeEntryWithRelations.toRepo() =
    RepoTimeEntryWithRelations(
        timeEntry = timeEntry.toRepo(),
        project = project?.toRepo(),
        task = task?.toRepo()
    )
