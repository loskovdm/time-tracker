package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.domain.model.TimeEntry as DomainTimeEntry
import io.github.loskovdm.timetracker.database.model.TimeEntry as EntityTimeEntry
import io.github.loskovdm.timetracker.database.util.parseDbInstant
import io.github.loskovdm.timetracker.database.util.parseDbInstantOrNull
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class TimeEntryMapper(
    private val currentUserIdProvider: CurrentUserIdProvider,
) {
    fun toEntityForInsert(domainTimeEntry: DomainTimeEntry): EntityTimeEntry =
        EntityTimeEntry(
            id = domainTimeEntry.id.toString(),
            userId = currentUserIdProvider.getUserIdForNewRecords(),
            startDateTime = domainTimeEntry.startDateTime.toString(),
            endDateTime = domainTimeEntry.endDateTime?.toString(),
            projectId = domainTimeEntry.projectId?.toString(),
            taskId = domainTimeEntry.taskId?.toString(),
        )

    fun toEntityForUpdate(domainTimeEntry: DomainTimeEntry, existing: EntityTimeEntry): EntityTimeEntry =
        EntityTimeEntry(
            id = domainTimeEntry.id.toString(),
            userId = existing.userId,
            startDateTime = domainTimeEntry.startDateTime.toString(),
            endDateTime = domainTimeEntry.endDateTime?.toString(),
            projectId = domainTimeEntry.projectId?.toString(),
            taskId = domainTimeEntry.taskId?.toString(),
        )

    fun toDomain(entityTimeEntry: EntityTimeEntry): DomainTimeEntry =
        DomainTimeEntry(
            id = kotlin.uuid.Uuid.parse(entityTimeEntry.id),
            startDateTime = entityTimeEntry.startDateTime.parseDbInstant(),
            endDateTime = entityTimeEntry.endDateTime.parseDbInstantOrNull(),
            projectId = entityTimeEntry.projectId?.let(kotlin.uuid.Uuid::parse),
            taskId = entityTimeEntry.taskId?.let(kotlin.uuid.Uuid::parse),
        )

    fun toDomain(entityTimeEntries: List<EntityTimeEntry>): List<DomainTimeEntry> =
        entityTimeEntries.map { toDomain(it) }
}
