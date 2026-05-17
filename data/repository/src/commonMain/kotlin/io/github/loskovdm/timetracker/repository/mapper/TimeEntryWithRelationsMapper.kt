package io.github.loskovdm.timetracker.repository.mapper

import io.github.loskovdm.domain.model.TimeEntryWithRelations as DomainTimeEntryWithRelations
import io.github.loskovdm.timetracker.repository.model.TimeEntryWithRelations as RepoTimeEntryWithRelations

internal class TimeEntryWithRelationsMapper(
    private val projectMapper: ProjectMapper,
    private val taskMapper: TaskMapper,
    private val timeEntryMapper: TimeEntryMapper,
) {
    fun toDomain(repoTimeEntryWithRelations: RepoTimeEntryWithRelations): DomainTimeEntryWithRelations =
        DomainTimeEntryWithRelations(
            timeEntry = timeEntryMapper.toDomain(repoTimeEntryWithRelations.timeEntry),
            project = repoTimeEntryWithRelations.project?.let { projectMapper.toDomain(it) },
            task = repoTimeEntryWithRelations.task?.let { taskMapper.toDomain(it) },
        )

    fun toDomain(repoTimeEntriesWithRelations: List<RepoTimeEntryWithRelations>): List<DomainTimeEntryWithRelations> =
        repoTimeEntriesWithRelations.map { toDomain(it) }
}