package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.domain.model.TimeEntryWithRelations as DomainTimeEntryWithRelation
import io.github.loskovdm.timetracker.database.model.TimeEntryWithRelations as EntityTimeEntryWithRelations

internal class TimeEntryWithRelationsMapper(
    private val projectMapper: ProjectMapper,
    private val taskMapper: TaskMapper,
    private val timeEntryMapper: TimeEntryMapper,
) {
    fun toDomain(entityTimeEntryWithRelations: EntityTimeEntryWithRelations): DomainTimeEntryWithRelation =
        DomainTimeEntryWithRelation(
            timeEntry = timeEntryMapper.toDomain(entityTimeEntryWithRelations.timeEntry),
            project = entityTimeEntryWithRelations.project?.let {
                projectMapper.toDomain(it)
            },
            task = entityTimeEntryWithRelations.task?.let {
                taskMapper.toDomain(it)
            }
        )

    fun toDomain(entityTimeEntriesWithRelations: List<EntityTimeEntryWithRelations>): List<DomainTimeEntryWithRelation> =
        entityTimeEntriesWithRelations.map { toDomain(it) }
}