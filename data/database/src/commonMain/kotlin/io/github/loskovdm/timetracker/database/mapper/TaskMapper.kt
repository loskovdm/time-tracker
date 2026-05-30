package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.timetracker.database.model.Task as EntityTask
import io.github.loskovdm.timetracker.repository.model.Task as RepoTask
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
internal class TaskMapper(
    private val currentUserIdProvider: CurrentUserIdProvider,
) {
    fun toEntityForInsert(repoTask: RepoTask): EntityTask =
        EntityTask(
            id = repoTask.id.toString(),
            userId = currentUserIdProvider.getUserIdForNewRecords(),
            projectId = repoTask.projectId.toString(),
            name = repoTask.name,
            isCompleted = repoTask.isCompleted,
        )

    fun toEntityForUpdate(repoTask: RepoTask, existing: EntityTask): EntityTask =
        EntityTask(
            id = repoTask.id.toString(),
            userId = existing.userId,
            projectId = repoTask.projectId.toString(),
            name = repoTask.name,
            isCompleted = repoTask.isCompleted,
        )

    fun toEntity(repoTask: RepoTask): EntityTask = toEntityForInsert(repoTask)

    fun toRepo(entityTask: EntityTask): RepoTask =
        RepoTask(
            id = kotlin.uuid.Uuid.parse(entityTask.id),
            name = entityTask.name,
            projectId = kotlin.uuid.Uuid.parse(entityTask.projectId),
            isCompleted = entityTask.isCompleted,
        )

    fun toRepo(entityTasks: List<EntityTask>): List<RepoTask> =
        entityTasks.map { toRepo(it) }
}
