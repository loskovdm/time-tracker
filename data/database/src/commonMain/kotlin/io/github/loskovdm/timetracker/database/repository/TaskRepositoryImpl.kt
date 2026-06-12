package io.github.loskovdm.timetracker.database.repository

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import io.github.loskovdm.timetracker.database.dao.TaskDao
import io.github.loskovdm.timetracker.database.mapper.TaskMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

internal class TaskRepositoryImpl(
    private val dao: TaskDao,
    private val mapper: TaskMapper,
) : TaskRepository {
    override suspend fun addTask(task: Task) {
        dao.insertTask(mapper.toEntityForInsert(task))
    }

    override suspend fun updateTask(task: Task) {
        val existing = dao.getTaskById(task.id) ?: return
        dao.updateTask(mapper.toEntityForUpdate(task, existing))
    }

    override suspend fun deleteTask(task: Task) {
        val existing = dao.getTaskById(task.id) ?: return
        dao.deleteTask(mapper.toEntityForUpdate(task, existing))
    }

    override suspend fun deleteTasksByProjectId(projectId: Uuid) {
        dao.deleteTasksByProjectId(projectId)
    }

    override suspend fun getTaskById(id: Uuid): Task? {
        return dao.getTaskById(id)?.let { mapper.toDomain(it) }
    }

    override fun getActiveTasks(projectId: Uuid): Flow<List<Task>> {
        return dao.getActiveTasks(projectId).map { mapper.toDomain(it) }
    }

    override fun getCompletedTasks(projectId: Uuid): Flow<List<Task>> {
        return dao.getCompletedTasks(projectId).map { mapper.toDomain(it) }
    }
}