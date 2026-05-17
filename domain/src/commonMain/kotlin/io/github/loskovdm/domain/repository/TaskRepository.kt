package io.github.loskovdm.domain.repository

import io.github.loskovdm.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TaskRepository {
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun getTaskById(id: Uuid): Task?
    fun getTasks(projectId: Uuid): Flow<List<Task>>
}