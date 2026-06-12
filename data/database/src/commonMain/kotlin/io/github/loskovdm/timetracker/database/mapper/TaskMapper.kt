package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.domain.model.Task as DomainTask
import io.github.loskovdm.timetracker.database.model.Task as EntityTask
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class TaskMapper(
    private val currentUserIdProvider: CurrentUserIdProvider,
) {
    fun toEntityForInsert(domainTask: DomainTask): EntityTask =
        EntityTask(
            id = domainTask.id.toString(),
            userId = currentUserIdProvider.getUserIdForNewRecords(),
            projectId = domainTask.projectId.toString(),
            name = domainTask.name,
            isCompleted = domainTask.isCompleted,
        )

    fun toEntityForUpdate(domainTask: DomainTask, existing: EntityTask): EntityTask =
        EntityTask(
            id = domainTask.id.toString(),
            userId = existing.userId,
            projectId = domainTask.projectId.toString(),
            name = domainTask.name,
            isCompleted = domainTask.isCompleted,
        )
    fun toDomain(entityTask: EntityTask): DomainTask =
        DomainTask(
            id = kotlin.uuid.Uuid.parse(entityTask.id),
            name = entityTask.name,
            projectId = kotlin.uuid.Uuid.parse(entityTask.projectId),
            isCompleted = entityTask.isCompleted,
        )

    fun toDomain(entityTasks: List<EntityTask>): List<DomainTask> =
        entityTasks.map { toDomain(it) }
}
