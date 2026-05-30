package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.timetracker.database.model.TimeEntry as EntityTimeEntry
import io.github.loskovdm.timetracker.database.sync.parseDbInstant
import io.github.loskovdm.timetracker.database.sync.parseDbInstantOrNull
import io.github.loskovdm.timetracker.repository.model.TimeEntry as RepoTimeEntry
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class TimeEntryMapper(
    private val currentUserIdProvider: CurrentUserIdProvider,
) {
    fun toEntityForInsert(repoTimeEntry: RepoTimeEntry): EntityTimeEntry =
        EntityTimeEntry(
            id = repoTimeEntry.id.toString(),
            userId = currentUserIdProvider.getUserIdForNewRecords(),
            startDateTime = repoTimeEntry.startDateTime.toString(),
            endDateTime = repoTimeEntry.endDateTime?.toString(),
            projectId = repoTimeEntry.projectId?.toString(),
            taskId = repoTimeEntry.taskId?.toString(),
        )

    fun toEntityForUpdate(repoTimeEntry: RepoTimeEntry, existing: EntityTimeEntry): EntityTimeEntry =
        EntityTimeEntry(
            id = repoTimeEntry.id.toString(),
            userId = existing.userId,
            startDateTime = repoTimeEntry.startDateTime.toString(),
            endDateTime = repoTimeEntry.endDateTime?.toString(),
            projectId = repoTimeEntry.projectId?.toString(),
            taskId = repoTimeEntry.taskId?.toString(),
        )

    fun toEntity(repoTimeEntry: RepoTimeEntry): EntityTimeEntry = toEntityForInsert(repoTimeEntry)

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
