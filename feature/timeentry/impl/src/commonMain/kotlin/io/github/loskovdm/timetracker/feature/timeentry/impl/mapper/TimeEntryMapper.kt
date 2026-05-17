package io.github.loskovdm.timetracker.feature.timeentry.impl.mapper

import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.timetracker.feature.timeentry.api.model.TimeEntry as ViewTimeEntry
import io.github.loskovdm.domain.model.TimeEntry as DomainTimeEntry

@OptIn(ExperimentalUuidApi::class)
internal class TimeEntryMapper {
    fun toView(domainTimeEntry: DomainTimeEntry): ViewTimeEntry =
        ViewTimeEntry(
            id = domainTimeEntry.id,
            startDateTime = domainTimeEntry.startDateTime,
            endDateTime = domainTimeEntry.endDateTime,
            projectId = domainTimeEntry.projectId,
            taskId = domainTimeEntry.taskId,
        )
}