package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetActiveTasksUseCase(
    private val repository: TaskRepository,
) {
    operator fun invoke(projectId: Uuid): Flow<List<Task>> {
        return repository.getActiveTasks(projectId)
    }
}