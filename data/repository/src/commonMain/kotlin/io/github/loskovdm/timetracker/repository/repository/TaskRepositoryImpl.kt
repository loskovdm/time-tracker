package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import io.github.loskovdm.timetracker.repository.datasource.TaskLocalDataSource
import io.github.loskovdm.timetracker.repository.model.toDomain
import io.github.loskovdm.timetracker.repository.model.toRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TaskRepositoryImpl(
    private val localDataSource: TaskLocalDataSource,
): TaskRepository {
    override suspend fun addTask(
        task: Task,
        addedAt: Instant
    ) {
        localDataSource.addTask(task.toRepo(addedAt))
    }

    override suspend fun updateTask(
        task: Task,
        updatedAt: Instant
    ) {
        localDataSource.updateTask(task.toRepo(updatedAt))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getTaskById(id: Uuid): Task? {
        return localDataSource.getTaskById(id)?.toDomain()
    }

    override fun getTasks(): Flow<List<Task>> {
        return localDataSource.getTasks().map { repoTasks ->
            repoTasks.map { repoTask -> repoTask.toDomain() }
        }
    }
}