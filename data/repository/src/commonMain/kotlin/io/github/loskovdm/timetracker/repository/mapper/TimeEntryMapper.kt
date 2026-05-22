package io.github.loskovdm.timetracker.repository.mapper

import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.domain.model.TimeEntry as DomainTimeEntry
import io.github.loskovdm.timetracker.repository.model.TimeEntry as RepoTimeEntry

@OptIn(ExperimentalUuidApi::class)
internal class TimeEntryMapper {
    fun toRepo(domainTimeEntry: DomainTimeEntry): RepoTimeEntry =
        RepoTimeEntry(
            id = domainTimeEntry.id,
            startDateTime = domainTimeEntry.startDateTime,
            endDateTime = domainTimeEntry.endDateTime,
            projectId = domainTimeEntry.projectId,
            taskId = domainTimeEntry.taskId,
        )

    fun toDomain(repoTimeEntry: RepoTimeEntry): DomainTimeEntry =
        DomainTimeEntry(
            id = repoTimeEntry.id,
            startDateTime = repoTimeEntry.startDateTime,
            endDateTime = repoTimeEntry.endDateTime,
            projectId = repoTimeEntry.projectId,
            taskId = repoTimeEntry.taskId,
        )

    fun toDomain(repoTimeEntries: List<RepoTimeEntry>): List<DomainTimeEntry> =
        repoTimeEntries.map { toDomain(it) }
}