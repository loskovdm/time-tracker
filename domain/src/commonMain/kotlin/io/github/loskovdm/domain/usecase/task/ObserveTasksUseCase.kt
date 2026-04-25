package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(
    private val repository: TaskRepository,
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.getTasks()
    }
}