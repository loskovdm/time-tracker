package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.timetracker.repository.model.TimeEntryWithRelations as RepoTimeEntryWithRelations
import io.github.loskovdm.timetracker.database.model.TimeEntryWithRelations as EntityTimeEntryWithRelations

internal class TimeEntryWithRelationsMapper(
    private val projectMapper: ProjectMapper,
    private val taskMapper: TaskMapper,
    private val timeEntryMapper: TimeEntryMapper,
) {
    fun toRepo(entityTimeEntryWithRelations: EntityTimeEntryWithRelations): RepoTimeEntryWithRelations =
        RepoTimeEntryWithRelations(
            timeEntry = timeEntryMapper.toRepo(entityTimeEntryWithRelations.timeEntry),
            project = entityTimeEntryWithRelations.project?.let {
                projectMapper.toRepo(it)
            },
            task = entityTimeEntryWithRelations.task?.let {
                taskMapper.toRepo(it)
            }
        )

    fun toRepo(entityTimeEntriesWithRelations: List<EntityTimeEntryWithRelations>): List<RepoTimeEntryWithRelations> =
        entityTimeEntriesWithRelations.map { toRepo(it) }
}