package io.github.loskovdm.timetracker.feature.timeentry.impl.mapper

import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task as ViewTask
import io.github.loskovdm.domain.model.Task as DomainTask

@OptIn(ExperimentalUuidApi::class)
internal class TaskMapper {
    fun toView(domainTask: DomainTask): ViewTask =
        ViewTask(
            id = domainTask.id,
            name = domainTask.name,
            projectId = domainTask.projectId,
            isCompleted = domainTask.isCompleted,
        )
}