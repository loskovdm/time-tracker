package io.github.loskovdm.timetracker.database.dataSource

import io.github.loskovdm.timetracker.database.dao.TaskDao
import io.github.loskovdm.timetracker.database.entity.toEntity
import io.github.loskovdm.timetracker.database.entity.toRepo
import io.github.loskovdm.timetracker.repository.datasource.TaskLocalDataSource
import io.github.loskovdm.timetracker.repository.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

class TaskLocalDataSourceImpl(private val dao: TaskDao): TaskLocalDataSource {
    override suspend fun addTask(task: Task) {
        dao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task.toEntity())
    }

    override suspend fun getTaskById(id: Uuid): Task? {
        return dao.getTaskById(id)?.toRepo()
    }

    override fun observeTasks(): Flow<List<Task>> {
        return dao.observeTasks().map { taskEntities ->
            taskEntities.map { taskEntity -> taskEntity.toRepo() }
        }
    }
}