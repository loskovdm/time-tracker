package io.github.loskovdm.timetracker.database.mapper

import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.timetracker.database.model.Task as EntityTask
import io.github.loskovdm.timetracker.repository.model.Task as RepoTask

@OptIn(ExperimentalUuidApi::class)
internal class TaskMapper {
    fun toEntity(repoTask: RepoTask): EntityTask {
        return EntityTask(
            id = repoTask.id.toString(),
            name = repoTask.name,
            projectId = repoTask.projectId.toString(),
            isCompleted = repoTask.isCompleted,
            userId = ""
        )
    }

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
