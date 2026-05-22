package io.github.loskovdm.timetracker.repository.mapper

import kotlin.uuid.ExperimentalUuidApi
import io.github.loskovdm.domain.model.Task as DomainTask
import io.github.loskovdm.timetracker.repository.model.Task as RepoTask

@OptIn(ExperimentalUuidApi::class)
internal class TaskMapper {
    fun toRepo(domainTask: DomainTask): RepoTask =
        RepoTask(
            id = domainTask.id,
            name = domainTask.name,
            projectId = domainTask.projectId,
            isCompleted = domainTask.isCompleted,
        )

    fun toDomain(repoTask: RepoTask): DomainTask =
        DomainTask(
            id = repoTask.id,
            name = repoTask.name,
            projectId = repoTask.projectId,
            isCompleted = repoTask.isCompleted,
        )

    fun toDomain(repoTasks: List<RepoTask>): List<DomainTask> =
        repoTasks.map { toDomain(it) }
}