package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TaskRepository {
    suspend fun addTask(
        task: Task,
        addedAt: Instant,
    )
    suspend fun updateTask(
        task: Task,
        updatedAt: Instant,
    )
    suspend fun deleteTask(
        task: Task,
        deletedAt: Instant,
    )
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getTaskById(id: Uuid): Task?
    fun observeTasks(): Flow<List<Task>>
}