package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.timetracker.database.sync.parseDbInstant
import io.github.loskovdm.timetracker.database.sync.parseDbInstantOrNull
import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.timetracker.database.model.TimeEntry as EntityTimeEntry
import io.github.loskovdm.timetracker.repository.model.TimeEntry as RepoTimeEntry

@OptIn(ExperimentalUuidApi::class)
internal class TimeEntryMapper {
    fun toEntity(repoTimeEntry: RepoTimeEntry): EntityTimeEntry {
        return EntityTimeEntry(
            id = repoTimeEntry.id.toString(),
            startDateTime = repoTimeEntry.startDateTime.toString(),
            endDateTime = repoTimeEntry.endDateTime?.toString(),
            projectId = repoTimeEntry.projectId?.toString(),
            taskId = repoTimeEntry.taskId?.toString(),
            userId = ""
        )
    }

    fun toRepo(entityTimeEntry: EntityTimeEntry): RepoTimeEntry =
        RepoTimeEntry(
            id = kotlin.uuid.Uuid.parse(entityTimeEntry.id),
            startDateTime = entityTimeEntry.startDateTime.parseDbInstant(),
            endDateTime = entityTimeEntry.endDateTime.parseDbInstantOrNull(),
            projectId = entityTimeEntry.projectId?.let(kotlin.uuid.Uuid::parse),
            taskId = entityTimeEntry.taskId?.let(kotlin.uuid.Uuid::parse),
        )

    fun toRepo(entityTimeEntries: List<EntityTimeEntry>): List<RepoTimeEntry> =
        entityTimeEntries.map { toRepo(it) }
}
