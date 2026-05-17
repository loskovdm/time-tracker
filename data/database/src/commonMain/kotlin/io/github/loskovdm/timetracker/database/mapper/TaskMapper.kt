package io.github.loskovdm.timetracker.database.mapper

import io.github.loskovdm.timetracker.database.model.Task as EntityTask
import io.github.loskovdm.timetracker.repository.model.Task as RepoTask

internal class TaskMapper {
    fun toEntity(
        repoTask: RepoTask,
        isArchived: Boolean = false,
    ): EntityTask =
        EntityTask(
            id = repoTask.id,
            name = repoTask.name,
            projectId = repoTask.projectId,
            isArchived = isArchived,
        )

    fun toRepo(entityTask: EntityTask): RepoTask =
        RepoTask(
            id = entityTask.id,
            name = entityTask.name,
            projectId = entityTask.projectId,
        )

    fun toRepo(entityTasks: List<EntityTask>): List<RepoTask> =
        entityTasks.map { toRepo(it) }
}