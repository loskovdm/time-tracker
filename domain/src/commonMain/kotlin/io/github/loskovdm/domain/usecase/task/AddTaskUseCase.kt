package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository

class AddTaskUseCase(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(
        name: String,
        projectId: Long,
    ) {
        val task = Task(
            name = name,
            projectId = projectId,
        )
        repository.addTask(task)
    }
}