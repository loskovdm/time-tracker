package io.github.loskovdm.timetracker.repository.datasource

import io.github.loskovdm.timetracker.repository.model.Task
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TaskLocalDataSource {
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun getTaskById(id: Uuid): Task?
    fun getTasks(projectId: Uuid): Flow<List<Task>>
}