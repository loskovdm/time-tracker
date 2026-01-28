package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(): Flow<List<Task>> {
        return repository.getAllTasks()
    }
}