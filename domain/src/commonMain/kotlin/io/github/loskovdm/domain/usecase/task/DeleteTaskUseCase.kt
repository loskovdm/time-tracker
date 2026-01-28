package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(
        id: Long,
        name: String,
        projectId: Long,
    ) {
        val task = Task(
            id = id,
            name = name,
            projectId = projectId,
        )
        repository.deleteTask(task)
    }
}