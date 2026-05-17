package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.timetracker.database.model.TimeEntry as EntityTimeEntry
import io.github.loskovdm.timetracker.repository.model.TimeEntry as RepoTimeEntry

internal class TimeEntryMapper {
    fun toEntity(
        repoTimeEntry: RepoTimeEntry,
        isArchived: Boolean = false,
    ): EntityTimeEntry =
        EntityTimeEntry(
            id = repoTimeEntry.id,
            startDateTime = repoTimeEntry.startDateTime,
            endDateTime = repoTimeEntry.endDateTime,
            projectId = repoTimeEntry.projectId,
            taskId = repoTimeEntry.taskId,
            isArchived = isArchived,
        )

    fun toRepo(entityTimeEntry: EntityTimeEntry): RepoTimeEntry =
        RepoTimeEntry(
            id = entityTimeEntry.id,
            startDateTime = entityTimeEntry.startDateTime,
            endDateTime = entityTimeEntry.endDateTime,
            projectId = entityTimeEntry.projectId,
            taskId = entityTimeEntry.taskId,
        )
}