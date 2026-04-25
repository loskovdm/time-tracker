package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.Task
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TaskLocalDataSource {
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getTaskById(id: Uuid): Task?
    fun getTasks(): Flow<List<Task>>
}