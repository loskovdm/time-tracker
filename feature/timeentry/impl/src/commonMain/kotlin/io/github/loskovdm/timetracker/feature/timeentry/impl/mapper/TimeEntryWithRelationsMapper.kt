package io.github.loskovdm.timetracker.feature.timeentry.impl.mapper

import io.github.loskovdm.timetracker.feature.timeentry.api.model.TimeEntryWithRelations as ViewTimeEntryWithRelation
import io.github.loskovdm.domain.model.TimeEntryWithRelations as DomainTimeEntryWithRelation

internal class TimeEntryWithRelationsMapper(
    private val projectMapper: ProjectMapper,
    private val taskMapper: TaskMapper,
    private val timeEntryMapper: TimeEntryMapper,
) {
    fun toView(domainTimeEntryWithRelation: DomainTimeEntryWithRelation): ViewTimeEntryWithRelation =
        ViewTimeEntryWithRelation(
            timeEntry = timeEntryMapper.toView(domainTimeEntryWithRelation.timeEntry),
            project = domainTimeEntryWithRelation.project?.let {
                projectMapper.toView(it)
            },
            task = domainTimeEntryWithRelation.task?.let {
                taskMapper.toView(it)
            }
        )

    fun toView(domainTimeEntryWithRelationList: List<DomainTimeEntryWithRelation>): List<ViewTimeEntryWithRelation> =
        domainTimeEntryWithRelationList.map { toView(it) }
}