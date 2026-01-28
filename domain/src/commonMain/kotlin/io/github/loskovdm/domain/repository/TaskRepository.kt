package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun getTaskById(id: Long): Flow<Task>?
    suspend fun getAllTasks(): Flow<List<Task>>
}