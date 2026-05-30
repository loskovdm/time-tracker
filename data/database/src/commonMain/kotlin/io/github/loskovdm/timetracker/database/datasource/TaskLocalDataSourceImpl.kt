package io.github.loskovdm.timetracker.database.datasource

import io.github.loskovdm.timetracker.database.dao.TaskDao
import io.github.loskovdm.timetracker.database.mapper.TaskMapper
import io.github.loskovdm.timetracker.repository.datasource.TaskLocalDataSource
import io.github.loskovdm.timetracker.repository.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

internal class TaskLocalDataSourceImpl(
    private val dao: TaskDao,
    private val mapper: TaskMapper,
) : TaskLocalDataSource {
    override suspend fun addTask(task: Task) {
        dao.insertTask(mapper.toEntity(task))
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(mapper.toEntity(task))
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(mapper.toEntity(task))
    }

    override suspend fun getTaskById(id: Uuid): Task? {
        return dao.getTaskById(id)?.let { mapper.toRepo(it) }
    }

    override fun getActiveTasks(projectId: Uuid): Flow<List<Task>> {
        return dao.getActiveTasks(projectId).map { mapper.toRepo(it) }
    }

    override fun getCompletedTasks(projectId: Uuid): Flow<List<Task>> {
        return dao.getCompletedTasks(projectId).map { mapper.toRepo(it) }
    }

    override suspend fun deleteTasksByProjectId(projectId: Uuid) {
        dao.deleteTasksByProjectId(projectId)
    }
}
