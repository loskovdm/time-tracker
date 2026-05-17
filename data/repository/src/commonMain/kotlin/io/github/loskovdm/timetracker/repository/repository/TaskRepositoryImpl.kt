package io.github.loskovdm.timetracker.repository.repository

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import io.github.loskovdm.timetracker.repository.datasource.TaskLocalDataSource
import io.github.loskovdm.timetracker.repository.mapper.TaskMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class TaskRepositoryImpl(
    private val localDataSource: TaskLocalDataSource,
    private val mapper: TaskMapper,
): TaskRepository {
    override suspend fun addTask(task: Task) {
        localDataSource.addTask(mapper.toRepo(task))
    }

    override suspend fun updateTask(task: Task) {
        localDataSource.updateTask(mapper.toRepo(task))
    }

    override suspend fun getTaskById(id: Uuid): Task? {
        return localDataSource.getTaskById(id)?.let { mapper.toDomain(it) }
    }

    override fun getTasks(projectId: Uuid): Flow<List<Task>> {
        return localDataSource.getTasks(projectId).map { mapper.toDomain(it)}
    }
}