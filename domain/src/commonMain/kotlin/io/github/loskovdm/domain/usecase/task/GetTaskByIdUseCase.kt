package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskByIdUseCase(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(id: Long): Flow<Task>? {
        return repository.getTaskById(id)
    }
}